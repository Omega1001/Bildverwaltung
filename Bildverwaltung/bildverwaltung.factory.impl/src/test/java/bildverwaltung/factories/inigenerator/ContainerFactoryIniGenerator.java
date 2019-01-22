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
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bildverwaltung.container.Factory;
import bildverwaltung.container.init.ContainerIniFileFieldNames;
import bildverwaltung.container.startup.StartupTask;
import bildverwaltung.utils.IniFileBuilder;

public class ContainerFactoryIniGenerator {

	private static final Logger LOG = LoggerFactory.getLogger(ContainerFactoryIniGenerator.class);

	public static void main(String[] args) throws URISyntaxException, IOException, ClassNotFoundException {
		generateIni(new File(args[0]), Arrays.asList(args).subList(1, args.length));
	}

	public static void generateIni(File output, List<String> baseDirs)
			throws URISyntaxException, IOException, ClassNotFoundException {
		List<File> files = getFileList(baseDirs);
		ClassLoader loader = generateClassLoader(files);

		IniFileBuilder iniFileBuilder = new IniFileBuilder();
		addFactorySection(iniFileBuilder, baseDirs, loader);
		addStartupSection(iniFileBuilder, baseDirs, loader);

		PrintWriter out = null;
		try {
			if (!output.getParentFile().exists()) {
				output.getParentFile().mkdirs();
			}
			out = new PrintWriter(new FileWriter(output, false));
			out.print(iniFileBuilder.generate().toString());
		} catch (IOException e) {
			LOG.error("Error during writing factory list to file {} : ", output, e);
		} finally {
			if (out != null) {
				out.close();
			}
		}

	}

	private static void addStartupSection(IniFileBuilder iniFileBuilder, List<String> baseDirs, ClassLoader loader) {
		iniFileBuilder.beginSection(ContainerIniFileFieldNames.STARTUP_SECTION_NAME);
		List<Class<?>> startups = listClassesOfInterface(baseDirs, StartupTask.class, loader);
		iniFileBuilder.addEntry(ContainerIniFileFieldNames.STARTUP_CLASSES,
				toCSV(new IteratorConverter<>(startups.iterator(), (c) -> c.getName())));

	}

	private static void addFactorySection(IniFileBuilder iniFileBuilder, List<String> baseDirs, ClassLoader loader) {
		iniFileBuilder.beginSection(ContainerIniFileFieldNames.FACTORY_SECTION_NAME);
		@SuppressWarnings("rawtypes")
		List<Factory> factories = listInstancesOfInterfaces(baseDirs, Factory.class, loader);
		Map<String, List<String>> factoryMapping = mapFactorieToInterface(factories);
		for (Entry<String, List<String>> factoryName : factoryMapping.entrySet()) {
			iniFileBuilder.addEntry(factoryName.getKey(), toCSV(factoryName.getValue().iterator()));
		}
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

	private static void searchDir(File dir, int basePathLength, Class<?> interfaceClass, ClassLoader loader,
			List<Class<?>> res) {
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				searchDir(f, basePathLength, interfaceClass, loader, res);
			} else if (f.getName().endsWith(".class")) {
				String path = f.getAbsolutePath();
				path = path.substring(
						basePathLength
								+ ((path.charAt(basePathLength) == '/' || path.charAt(basePathLength) == '\\') ? 1 : 0),
						path.length() - 6);
				path = path.replace('/', '.').replace('\\', '.');
				try {
					Class<?> c = loader.loadClass(path);
					if(interfaceClass.isAssignableFrom(c)) {
						res.add(c);
					}
				} catch (ClassNotFoundException e) {
					LOG.warn("Unable to load class : ", e);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> List<T> listInstancesOfInterfaces(List<String> baseDirs, Class<T> interfaceClass,
			ClassLoader loader) {
		List<T> res = new LinkedList<>();
		for (Class<?> c : listClassesOfInterface(baseDirs, interfaceClass, loader)) {
			if (!Modifier.isAbstract(c.getModifiers()) && !c.isInterface()) {
				try {
					res.add((T) c.newInstance());
				} catch (IllegalAccessException e) {
					LOG.error("Found factory {}, but it has no public standard constructor, skipping", c.getName(), e);
				} catch (InstantiationException e) {
					LOG.error("Found factory {}, but it threw an exception while instanciation, skipping", c.getName(),
							e);
				}
			}
		}
		return res;
	}

	private static List<Class<?>> listClassesOfInterface(List<String> baseDirs, Class<?> interfaceClass,
			ClassLoader loader) {
		List<Class<?>> res = new LinkedList<>();
		for (String s : baseDirs) {
			File f = new File(s);
			if (f.exists() && f.isDirectory()) {
				searchDir(f, s.length(), interfaceClass, loader, res);
			}
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	private static Map<String, List<String>> mapFactorieToInterface(List<Factory> factories) {
		Map<String, List<String>> res = new HashMap<>();
		for (Factory<?> f : factories) {
			String key = f.getInterfaceType().getName();
			List<String> l = res.get(key);
			if (l == null) {
				l = new LinkedList<>();
			}
			l.add(f.getClass().getName());
			res.put(key, l);
		}
		return res;
	}

	private static String toCSV(Iterator<String> it) {
		StringBuilder sb = new StringBuilder();
		while (it.hasNext()) {
			if (sb.length() > 0) {
				sb.append(',');
			}
			sb.append(it.next());
		}
		return sb.toString();
	}

	private static class IteratorConverter<S, T> implements Iterator<T> {
		private Iterator<S> source;
		private Function<S, T> converter;

		public IteratorConverter(Iterator<S> source, Function<S, T> converter) {
			super();
			this.source = source;
			this.converter = converter;
		}

		@Override
		public boolean hasNext() {
			return source.hasNext();
		}

		@Override
		public T next() {
			S s = source.next();
			return s != null ? converter.apply(s) : null;
		}

	}
}
