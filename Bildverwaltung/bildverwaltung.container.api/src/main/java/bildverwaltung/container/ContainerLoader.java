package bildverwaltung.container;

import java.util.List;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bildverwaltung.container.shutdown.ContainerShutdownException;
import bildverwaltung.container.shutdown.ShutdownPhase;
import bildverwaltung.container.shutdown.ShutdownTask;
import bildverwaltung.container.shutdown.ShutdownTaskFactory;
import bildverwaltung.container.startup.ContainerStartupException;
import bildverwaltung.container.startup.StartUpPhase;
import bildverwaltung.container.startup.StartupTask;
import bildverwaltung.container.startup.StartupTaskFactory;
import bildverwaltung.utils.IniFile;
import bildverwaltung.utils.IniFileReader;

import static bildverwaltung.container.init.ContainerIniFileFieldNames.*;

/**
 * This class is used to handle {@link ManagedContainer} startup and
 * shutdown<br>
 * In order to do this, this class provides the following public methods:<br>
 * <table>
 * <tr>
 * <td>Method name</td>
 * <td>Usage</td
 * </tr>
 * <tr>
 * <td>{@link #loadContainer()}</td>
 * <td>Create a new {@link ManagedContainer} and perform startup</td>
 * </tr>
 * <tr>
 * <td>{@link #handleContainerShutdown(ManagedContainer)}</td>
 * <td>Perform a regular shutdown on the passed container</td
 * </tr>
 * </table>
 * 
 * @author Jannik
 *
 */
public class ContainerLoader {
	private static final Logger LOG = LoggerFactory.getLogger(ContainerLoader.class);

	/*
	 * Prevent instantiation
	 */
	private ContainerLoader() {
	};

	/**
	 * This method is used to instantiate a new {@link ManagedContainer}<br>
	 * To do that, this container loads the container.ini and factory.ini files<br>
	 * Then it instantiates a new {@link ManagedContainer} using the class name from
	 * the ini<br>
	 * Then all {@link Factory}s from the factory.ini are beeing constructed and
	 * added to the {@link ManagedContainer}<br>
	 * In the end, all {@link StartupTask} are being materialised, an eecuted, based
	 * on there {@link StartUpPhase}<br>
	 * <p>
	 * If an exception occurred during initialization, an {@link ContainerException}
	 * is thrown<br>
	 * Except if the exception is a {@link ContainerStartupException} whose
	 * isAboardStartup method returns false
	 * 
	 * @return an new, fully initialized {@link ManagedContainer}
	 * @throws ContainerException
	 *             if there was an error during startup
	 */
	public static ManagedContainer loadContainer() {
		LOG.trace("Enter loadContainer");
		IniFile ini = loadContainerIni();
		ManagedContainer container = instanciateContainer(ini);
		LOG.info("Instanciated ManagedContainer from ini");
		IniFile factoryInit = loadFactoryList();
		registerFactories(container, factoryInit);
		registerStartups(container, factoryInit);
		registerShutdowns(container, factoryInit);
		LOG.debug("Finished registering Factories");
		runContainerSideStartup(container);
		LOG.info("Managed Container startup completed successfully");
		LOG.trace("Exit loadContainer container={}", container);
		return container;
	}

	/**
	 * This method is used to perform an ordinary shutdown on the passed
	 * {@link ManagedContainer}<br>
	 * In order to do that, all {@link ShutdownTask} are injected and executed,
	 * based on there {@link ShutdownPhase}<br>
	 * After that, the close method of the {@link ManagedContainer} is called, to
	 * release any resources associated with it
	 * <p>
	 * If an exception is cought during doing so, it is stored, so the shutdown can
	 * continue as good as possible<br>
	 * After the execution is completed a single {@link ContainerShutdownException}
	 * is thrown, that incorporates all intercepted exceptions as suppressed
	 * exceptions
	 * <p>
	 * 
	 * @param container
	 *            to be closed
	 * @throws ContainerShutdownException
	 *             if there was en error during shuting down
	 */
	public static void handleContainerShutdown(ManagedContainer container) {
		LOG.trace("Enter handleContainerShutdown container={}", container);
		ContainerShutdownException ex = null;
		List<ShutdownTask> shutdownTasks = container.materializeAll(ShutdownTask.class, Scope.DEFAULT);
		LOG.debug("Recieved {} shutdown tasks from container", shutdownTasks != null ? shutdownTasks.size() : 0);
		for (ShutdownPhase phase : ShutdownPhase.values()) {
			LOG.debug("Begin shutdown phase {}", phase);
			for (ShutdownTask task : shutdownTasks) {
				if (phase.equals(task.getExecutionPhase())) {
					try {
						LOG.debug("Executiong shutdown task {}", task.getTaskName());
						task.execute();
					} catch (Exception e) {
						LOG.error("Error during executing shutdown task {}: ", task.getTaskName(), e);
						if (ex == null) {
							ex = new ContainerShutdownException("Error during shuting down container", e);
						} else {
							ex.addSuppressed(e);
						}
					}
				}
			}
		}
		LOG.debug("Invoking container side shutdown");
		try {
			container.close();
		} catch (Exception e) {
			LOG.error("Error during shutting down the container : ", e);
			if (ex == null) {
				ex = new ContainerShutdownException("Error during shuting down container", e);
			} else {
				ex.addSuppressed(e);
			}
		}
		LOG.info("Container shutdown completed with {} incidents", ex == null ? 0 : ex.getSuppressed().length);
		if (ex != null) {
			throw ex;
		}
		LOG.trace("Exit handleContainerShutdown");
	}

	private static void runContainerSideStartup(ManagedContainer container) {
		LOG.trace("Enter runContainerSideStartup container={}", container);
		List<StartupTask> startupTasks = container.materializeAll(StartupTask.class, Scope.DEFAULT);
		LOG.debug("Recieved {} startup tasks from container", startupTasks != null ? startupTasks.size() : 0);
		for (StartUpPhase phase : StartUpPhase.values()) {
			LOG.debug("Begin startup phase {}", phase);
			for (StartupTask task : startupTasks) {
				if (phase.equals(task.getExecutionPhase())) {
					try {
						LOG.debug("Executiong shutdown task {}", task.getTaskName());
						task.execute();
					} catch (Exception e) {
						LOG.error("Error during executing startup task {} : ", task.getTaskName(), e);
						if (!(e instanceof ContainerStartupException)
								|| ((ContainerStartupException) e).isAboardStartup()) {
							LOG.error("Error has set the aboard condition, canceling startup");
							throw e;
						}
					}
				}
			}
		}
		LOG.trace("Exit runContainerSideStartup");
	}

	private static void registerFactories(ManagedContainer container, IniFile ini) {
		LOG.trace("Enter registerFactories container={}, ini={}", container, ini);
		ClassLoader loader = ContainerLoader.class.getClassLoader();
		for (Entry<String, String> entry : ini.getSectionAsMap(FACTORY_SECTION_NAME).entrySet()) {
			String[] factoryNames = entry.getValue().split(",");
			LOG.debug("Found {} potential factory implementations for interface {}", factoryNames.length,
					entry.getKey());
			for (String factoryName : factoryNames) {
				try {
					Object rawFactory = loader.loadClass(factoryName).newInstance();
					if (rawFactory instanceof Factory<?>) {
						LOG.debug("Adding Factory {} for interface {}", rawFactory, entry.getKey());
						container.addFactory((Factory<?>) rawFactory);
					} else {
						LOG.error("{} class is not an implementation of Factory", rawFactory.getClass().getName());
					}
				} catch (Exception e) {
					LOG.error("Error during instantiating factory {} for interface {}, skipping : ", factoryName,
							entry.getKey(), e);
					throw new ContainerStartupException(e);
				}
			}
		}
		LOG.trace("Exit registerFactories");
	}

	@SuppressWarnings("unchecked")
	private static void registerStartups(ManagedContainer container, IniFile factoryInit) {
		LOG.trace("Enter registerStartups container={}, factoryInit={}", container, factoryInit);
		String[] types = factoryInit.getOrDefault(STARTUP_SECTION_NAME, STARTUP_CLASSES, "").split(",");
		LOG.debug("Found {} potential startup tasks");
		ClassLoader loader = ContainerLoader.class.getClassLoader();
		for (String className : types) {
			try {
				Class<?> c = loader.loadClass(className);
				if (StartupTask.class.isAssignableFrom(c)) {
					LOG.debug("Adding startup task {}", c.getSimpleName());
					container.addFactory(new StartupTaskFactory((Class<? super StartupTask>) c));
				} else {
					LOG.warn("Listed startup task does not implements StartupTask");
				}
			} catch (ClassNotFoundException e) {
				LOG.error("Unable to locate declared Startup");
				throw new RuntimeException(e);
			}
		}
		LOG.trace("Exit registerStartups");
	}

	@SuppressWarnings("unchecked")
	private static void registerShutdowns(ManagedContainer container, IniFile factoryInit) {
		LOG.trace("Enter registerShutdowns container={}, factoryInit={}", container, factoryInit);
		String[] types = factoryInit.getOrDefault(SHUTDOWN_SECTION_NAME, SHUTDOWN_CLASSES, "").split(",");
		LOG.debug("Found {} potential shutdown tasks");
		ClassLoader loader = ContainerLoader.class.getClassLoader();
		for (String className : types) {
			try {
				Class<?> c = loader.loadClass(className);
				if (ShutdownTask.class.isAssignableFrom(c)) {
					LOG.debug("Adding shutdown task {}", c.getSimpleName());
					container.addFactory(new ShutdownTaskFactory((Class<? super ShutdownTask>) c));
				} else {
					LOG.warn("Listed shutdown task does not implements ShutdownTask");
				}
			} catch (ClassNotFoundException e) {
				LOG.error("Unable to locate declared shutdown");
				throw new RuntimeException(e);
			}
		}
		LOG.trace("Exit registerShutdowns");
	}

	private static IniFile loadFactoryList() {
		return IniFileReader
				.readFromFile(ContainerLoader.class.getClassLoader().getResourceAsStream("META-INF/factories.ini"));
	}

	private static ManagedContainer instanciateContainer(IniFile ini) {
		LOG.trace("Enter instanciateContainer ini={}", ini);
		String className = ini.get(CONTAINER_CONFIG_SECTION_NAME, CONTAINER_CONFIG_IMPL_CLASS);
		if (className == null) {
			throw new IllegalArgumentException("Missing container impl class name");
		}
		ClassLoader loader = ContainerLoader.class.getClassLoader();
		ManagedContainer res = null;
		try {
			Class<?> rawContainer = loader.loadClass(className);
			if (ManagedContainer.class.isAssignableFrom(rawContainer)) {
				LOG.debug("About to Instantiate ManagedContainer implementation {}", rawContainer.getSimpleName());
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
		LOG.trace("Exit instanciateContainer res={}", res);
		return res;
	}

	private static IniFile loadContainerIni() {
		return IniFileReader
				.readFromFile(ContainerLoader.class.getClassLoader().getResourceAsStream("META-INF/container.ini"));
	}

}
