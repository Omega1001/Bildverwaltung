package bildverwaltung.container;

import java.io.InputStream;
import java.util.List;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bildverwaltung.container.startup.ContainerStartupException;
import bildverwaltung.container.startup.StartUpPhase;
import bildverwaltung.container.startup.StartupTask;
import bildverwaltung.container.startup.StartupTaskFactory;
import bildverwaltung.utils.IniFile;
import bildverwaltung.utils.IniFileReader;

import static bildverwaltung.container.init.ContainerIniFileFieldNames.*;

class ContainerLoader {
	private static final Logger LOG = LoggerFactory.getLogger(ContainerLoader.class);

	public static ManagedContainer loadContainer() {
		IniFile ini = loadContainerIni();
		ManagedContainer container = instanciateContainer(ini);
		IniFile factoryInit = loadFactoryList();
		registerFactories(container, factoryInit);
		registerStartups(container,factoryInit);
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

	private static void registerFactories(ManagedContainer container, IniFile ini) {
		ClassLoader loader = ContainerLoader.class.getClassLoader();
		for (Entry<String, String> entry : ini.getSectionAsMap(FACTORY_SECTION_NAME).entrySet()) {
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
					throw new RuntimeException(e);
				}
			}
		}

	}

	@SuppressWarnings("unchecked")
	private static void registerStartups(ManagedContainer container, IniFile factoryInit) {
		String [] types = factoryInit.get(STARTUP_SECTION_NAME, STARTUP_CLASSES).split(",");
		ClassLoader loader = ContainerLoader.class.getClassLoader();
		for(String className : types) {
			try {
				Class<?> c = loader.loadClass(className);
				container.addFactory(new StartupTaskFactory((Class<? super StartupTask>) c));
			} catch (ClassNotFoundException e) {
				LOG.error("Unable to locate declared Startup");
				throw new RuntimeException(e);
			}
		}
		
	}

	private static IniFile loadFactoryList() {
		return IniFileReader.readFromFile(ContainerLoader.class.getClassLoader().getResourceAsStream("META-INF/factories.ini"));
	}

	private static ManagedContainer instanciateContainer(IniFile ini) {
		String className = ini.get(CONTAINER_CONFIG_SECTION_NAME,CONTAINER_CONFIG_IMPL_CLASS);
		if(className == null) {
			throw new IllegalArgumentException("Missing container impl class name");
		}
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

	private static IniFile loadContainerIni() {
		return IniFileReader.readFromFile(ContainerLoader.class.getClassLoader().getResourceAsStream("META-INF/container.ini"));
	}

}
