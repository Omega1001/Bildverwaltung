package bildverwaltung.factories.inigenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bildverwaltung.container.Factory;

public class ContainerFactoryIniGenerator {

	private static final Logger LOG = LoggerFactory.getLogger(ContainerFactoryIniGenerator.class);

	public static void main(String[] args) throws URISyntaxException, IOException, ClassNotFoundException {
		generateIni(new File(args[0]), Arrays.asList(args).subList(1, args.length));
	}

	public static void generateIni(File output, List<String> baseDirs)
			throws URISyntaxException, IOException, ClassNotFoundException {
		List<File> files = getFileList(baseDirs);
		ClassLoader loader = generateClassLoader(files);
		Map<String, List<String>> factoryClassNames = new HashMap<>();
		for (File f : files) {
			LOG.info("Processing directory {}", f);
			mergeIntoMap(factoryClassNames, processDir(f, f.getAbsolutePath().length(), loader));

		}
		PrintWriter out = null;
		LOG.info("Writing Factory List");
		try {
			if (!output.getParentFile().exists()) {
				output.getParentFile().mkdirs();
			}
			out = new PrintWriter(new FileWriter(output, false));
			for (Entry<String, List<String>> factoryName : factoryClassNames.entrySet()) {
				out.print(factoryName.getKey());
				out.print('=');
				Iterator<String> it = factoryName.getValue().iterator();
				while (it.hasNext()) {
					out.print(it.next());
					if (it.hasNext()) {
						out.print(',');
					}
				}
				out.println();
			}
			out.flush();
			LOG.info("Finished writing Factory List File");
		} catch (IOException e) {
			LOG.error("Error during writing factory list to file {} : ", output, e);
		} finally {
			if (out != null) {
				out.close();
			}
		}

	}

	private static void mergeIntoMap(Map<String, List<String>> target, Map<String, List<String>> source) {
		for (String key : source.keySet()) {
			List<String> val = target.get(key);
			if (val == null) {
				val = new LinkedList<>();
			}
			val.addAll(source.get(key));
			target.put(key, val);
		}

	}

	private static Map<String, List<String>> processDir(File dir, int basePathLength, ClassLoader loader)
			throws ClassNotFoundException {
		Map<String, List<String>> factoryClassNames = new HashMap<>();
		LOG.debug("Processing directory {}", dir);
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				factoryClassNames.putAll(processDir(f, basePathLength, loader));
			} else if (f.getName().endsWith(".class")) {
				String path = f.getAbsolutePath();
				path = path.substring(basePathLength + ((path.charAt(0) == '/' || path.charAt(0) == '\\') ? 2 : 1),
						path.length() - 6);
				path = path.replace('/', '.').replace('\\', '.');
				Class<?> c = loader.loadClass(path);
				LOG.debug("Processing class {}", c.getName());
				if (!Modifier.isAbstract(c.getModifiers()) || c.isInterface()) {
					LOG.trace("Class is not an interface or abstract");
					if (Factory.class.isAssignableFrom(c)) {
						LOG.debug("Class is a Factory");
						try {
							Factory<?> factory = (Factory<?>) c.newInstance();
							Class<?> intf = factory.getInterfaceType();
							if (intf != null && intf.isInterface()) {
								LOG.info("Added Factory {}", c.getName());
								List<String> l = factoryClassNames.get(intf.getName());
								if (l == null) {
									l = new LinkedList<>();
								}
								l.add(c.getName());
								factoryClassNames.put(intf.getName(), l);
							} else {
								LOG.warn(
										"Found invalid factory {} : interface type is null or not an interface, ignoring",
										c.getName());
							}
						} catch (IllegalAccessException e) {
							LOG.error("Found factory {}, but it has no public standard constructor, skipping",
									c.getName(), e);
						} catch (InstantiationException e) {
							LOG.error("Found factory {}, but it threw an exception while instanciation, skipping",
									c.getName(), e);
						}
					} else {
						LOG.debug("Class is not a factory");
					}
				} else {
					LOG.debug("Class is abstract or interface, ignoring");
				}
			}
		}
		return factoryClassNames;
	}

	private static ClassLoader generateClassLoader(List<File> files) {
		LOG.debug("Setting up the ClassLoader for directories {}", files);
		URL[] urls = new URL[files.size()];
		int cut = 0;
		int i = 0;
		for (File f : files) {
			try {
				urls[i++] = f.toURI().toURL();
			} catch (MalformedURLException e) {
				// Not to likely ...
				LOG.error("Unable to convert Pathname to URL, ignoring");
				cut++;
			}
		}
		if (cut > 0) {
			urls = Arrays.copyOf(urls, urls.length - cut);
		}
		return new URLClassLoader(urls, ContainerFactoryIniGenerator.class.getClassLoader());
	}

	private static List<File> getFileList(List<String> baseDirs) {
		List<File> files = new LinkedList<>();
		for (String dir : baseDirs) {
			File f = new File(dir);
			if (f.exists() && f.isDirectory()) {
				files.add(f);
			} else {
				LOG.warn("Found invalid directory path {}, ignoring it", dir);
			}
		}
		return files;
	}
}
