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
import java.util.LinkedList;
import java.util.List;
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
		List<String> factoryClassNames = new LinkedList<>();
		for (File f : files) {
			LOG.info("Processing directory {}",f);
			factoryClassNames.addAll(processDir(f, f.getAbsolutePath().length(), loader));
		}
		PrintWriter out =null;
		LOG.info("Writing Factory List");
		try {
			if (!output.getParentFile().exists()) {
				output.getParentFile().mkdirs();
			}
			out = new PrintWriter(new FileWriter(output, false));
			for(String factoryName : factoryClassNames) {
				out.println(factoryName);
			}
			out.flush();
			LOG.info("Finished writing Factory List File");
		}catch(IOException e) {
			LOG.error("Error during writing factory list to file {} : ",output,e);
		}finally {
			if(out != null) {
				out.close();
			}
		}
		
	}

	private static List<String> processDir(File dir, int basePathLength, ClassLoader loader)
			throws ClassNotFoundException {
		List<String> factoryClassNames = new LinkedList<>();
		LOG.debug("Processing directory {}",dir);
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				factoryClassNames.addAll(processDir(f, basePathLength, loader));
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
							c.getConstructor();
							LOG.info("Added Factory {}", c.getName());
							factoryClassNames.add(c.getName());
						} catch (NoSuchMethodException e) {
							LOG.error("Found factory {}, but it has no standard constructor, skipping", c.getName());
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
		LOG.debug("Setting up the ClassLoader for directories {}",files);
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
