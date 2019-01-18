package bildverwaltung.container;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bildverwaltung.container.startup.ContainerStartupException;
import bildverwaltung.container.startup.StartUpPhase;
import bildverwaltung.container.startup.StartupTask;

class ContainerLoader {
	private static final Logger LOG = LoggerFactory.getLogger(ContainerLoader.class);
	private static final String MANAGED_CONTAINER_IMPL_CLASS_NAME = "managedContainerImplClass";

	public static ManagedContainer loadContainer() {
		Map<String, String> ini = loadContainerIni();
		ManagedContainer container = instanciateContainer(ini);
		Map<String, String> factories = loadFactoryList();
		registerFactories(container, factories);
		runContainerSideStartup(container);
		return container;
	}

	private static void runContainerSideStartup(ManagedContainer container) {
		List<StartupTask> startupTasks = container.materializeAll(StartupTask.class, Scope.APPLICATION);
		for (StartUpPhase phase : StartUpPhase.values()) {
			for (StartupTask task : startupTasks) {
				if (phase.equals(task.getExecutionPhase())) {
					try {
						task.execute();
					} catch (ContainerStartupException e) {
						LOG.error("Error during executing startup task : ",e);
						if(e.isAboardStartup()) {
							LOG.error("Error has set the aboard condition, canceling startup");
							throw e;
						}
					}
				}
			}
		}

	}

	private static void registerFactories(ManagedContainer container, Map<String, String> factories) {
		ClassLoader loader = ContainerLoader.class.getClassLoader();
		for (Entry<String, String> entry : factories.entrySet()) {
			String[] factoryNames = entry.getValue().split(",");
			for (String factoryName : factoryNames) {
				try {
					Object rawFactory = loader.loadClass(factoryName).newInstance();
					if(rawFactory instanceof Factory<?>) {
						container.addFactory((Factory<?>) rawFactory);
					}else {
						LOG.error("Specified factory class is not an implementation of Factory<?>");
					}
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					LOG.error("Error during instantiating factory {} for interface {}, skipping : ", factoryName,
							entry.getKey(), e);
				}
			}
		}

	}

	private static Map<String, String> loadFactoryList() {
		return parseIni(ContainerLoader.class.getClassLoader().getResourceAsStream("META-INF/factories.ini"));
	}

	private static ManagedContainer instanciateContainer(Map<String, String> ini) {
		String className = ini.get(MANAGED_CONTAINER_IMPL_CLASS_NAME);
		ClassLoader loader = ContainerLoader.class.getClassLoader();
		ManagedContainer res = null;
		try {
			Class<?> rawContainer = loader.loadClass(className);
			if (ManagedContainer.class.isAssignableFrom(rawContainer)) {
				res = (ManagedContainer) rawContainer.newInstance();
			} else {
				LOG.error(
						"Invalid configuration : The class for ManagedContainer specified by the container.ini does not implements the interface");
				throw new IllegalArgumentException(
						"Specified implementation of ManagedContainer is no implementation of ManagedContainer");
			}
		} catch (ClassNotFoundException e) {
			LOG.error(
					"Invalid configuration : The class for ManagedContainer specified by the container.ini does not exists : ",
					e);
			throw new IllegalArgumentException("Specified ManagedContainer implementation could not be found", e);
		} catch (InstantiationException e) {
			LOG.error("Error during instantiating the ManagedContainer : ", e);
			throw new RuntimeException("Error during instantiating container", e);
		} catch (IllegalAccessException e) {
			LOG.error(
					"Invalid configuration : The class for ManagedContainer specified by the container.ini has no public, no argument constructor",
					e);
			throw new IllegalArgumentException("Specified ManagedContainer has no public no argument constructor", e);
		}
		return res;
	}

	private static Map<String, String> loadContainerIni() {
		return parseIni(ContainerLoader.class.getClassLoader().getResourceAsStream("META-INF/container.ini"));
	}

	private static Map<String, String> parseIni(InputStream in) {
		Map<String, String> res = new HashMap<>();
		Scanner sc = null;
		try {
			sc = new Scanner(in);
			while (sc.hasNextLine()) {
				String line = sc.nextLine().trim();
				int seperator = line.indexOf('=');
				if (seperator < 0) {
					res.put(line, "");
				} else if (seperator == line.length() - 1) {
					res.put(line.substring(0, line.length() - 1), "");
				} else {
					res.put(line.substring(0, seperator), line.substring(seperator + 1));
				}
			}
		} finally {
			if (sc != null) {
				sc.close();
			}
		}
		return res;
	}

}
