package bildverwaltung.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class acts as provider for the {@link ManagedContainer} of this
 * application<br>
 * This class allows access to the {@link ManagedContainer} singleton<br>
 * That means, this class does not support multiple containers at a time
 * <p>
 * 
 * This class provides the following static methods:
 * <table>
 * <tr>
 * <td>Method name</td>
 * <td>Usage</td>
 * </tr>
 * <tr>
 * <td>{@link #getActiveContainer()}</td>
 * <td>Returnes the active {@link ManagedContainer}, if any is active</td>
 * </tr>
 * <tr>
 * <td>{@link #startupContainer()}</td>
 * <td>Starts a new {@link ManagedContainer} and puts it as the active one</td>
 * </tr>
 * <tr>
 * <td>{@link #shutdown()}</td>
 * <td>Shuts the currently active {@link ManagedContainer} down</td>
 * </tr>
 * </table>
 * <p>
 * 
 * Please note, that this class is <b>not</b> necessarily <b>thread-safe</b>
 * 
 * @author Jannik
 * @see ManagedContainer
 *
 */
public class Container {

	private static final Logger LOG = LoggerFactory.getLogger(Container.class);
	private static ManagedContainer activeContainer = null;
	private static boolean shutdown = false;

	/*
	 * Prevent instantiation
	 */
	private Container() {
	};

	/**
	 * This method can be used to acquire the active {@link ManagedContainer}
	 * <p>
	 * In case that no container has been instantiated yet, a new
	 * {@link ManagedContainer} is created by calling
	 * {@link #startupContainer()}<br>
	 * Please avoid invoking this method, while no {@link ManagedContainer} has been
	 * started, as a start up may take some time<br>
	 * Plus it generates a warning, and you probably don't want either
	 * <p>
	 * 
	 * In case that the active container has been shut down, a
	 * {@link IllegalStateException} is thrown<br>
	 * 
	 * @return the currently opened {@link ManagedContainer}
	 * @throws IllegalStateException
	 *             if the current {@link ManagedContainer} is already closed
	 */
	public static ManagedContainer getActiveContainer() {
		LOG.trace("Enter getActiveContainer");
		if (shutdown) {
			LOG.error("Tried to access the container after it was shutdown");
			throw new IllegalStateException("Container already shut down");
		}
		if (activeContainer == null) {
			LOG.warn("Tried to fetch the container before it was started, starting it now");
			startupContainer();
		}
		LOG.trace("Exit getActiveContainer activeContainer={}", activeContainer);
		return activeContainer;
	}

	/**
	 * This method is used to create a new {@link ManagedContainer} and perform a
	 * regular startup with it<br>
	 * This method will generate a fresh {@link ManagedContainer} using the
	 * {@link ContainerLoader} and add all initial factories<br>
	 * After that, all startup tasks are beein performed<br>
	 * For more informations, please see {@link ContainerLoader#loadContainer()}
	 * <p>
	 * In case an error occurred during instantiation, a {@link ContainerException}
	 * will be thrown
	 * <p>
	 * This method will do nothing, if there currently is a active
	 * {@link ManagedContainer}
	 * <p>
	 * This method will throw an {@link ContainerException}, if there was an error
	 * during instantiating and/or loading the new {@link ManagedContainer}
	 * 
	 * @see ContainerLoader#loadContainer()
	 * @throws ContainerException
	 *             if there was an error during startup
	 */
	public static void startupContainer() {
		LOG.trace("Enter startupContainer");
		if (activeContainer != null) {
			LOG.warn("Tried to start container, but it was already started");
			return;
		}
		activeContainer = ContainerLoader.loadContainer();
		LOG.trace("Exit startupContainer");
	}

	/**
	 * This method will perform an ordinary shutdown on the currently active
	 * {@link ManagedContainer}<br>
	 * All registered shutdown hooks are being invoked, followed by the
	 * {@link ManagedContainer}s own close method<br>
	 * For more informations, please see
	 * {@link ContainerLoader#handleContainerShutdown(ManagedContainer)}
	 * <p>
	 * In case an exception occurs during executing either the shutdown hooks or the
	 * containers close method, it will be stored, to be thrown at the end of the
	 * execution<br>
	 * If such an error exists, the execution will continue as good as it may<br>
	 * At the end of the execution, a {@link ContainerException} will be thrown,
	 * that contains all exceptions caught during the execution as suppressed
	 * exceptions<br>
	 * If no exceptions occurred, this method returns cleanly<br>
	 * <p>
	 * This method will throw an {@link IllegalStateException}, if there currently
	 * is no active container
	 * <p>
	 * This method will do nothing, if the currently active container is already
	 * shutdowned<br>
	 * 
	 * @see ContainerLoader#loadContainer()
	 * @throws ContainerException
	 *             if there was an error during startup
	 * @throws IllegalStateException
	 *             if no open container is availeble
	 */
	public static void shutdown() {
		LOG.trace("Enter shutdown");
		if (activeContainer == null) {
			LOG.error("Tried to shutdown the container, but it was never started");
			throw new IllegalStateException("The container is not started");
		}
		if (shutdown) {
			LOG.warn("Tried to shutdown the container, but it was already shutdowned, do nothing");
			return;
		}
		try {
			ContainerLoader.handleContainerShutdown(activeContainer);
		} finally {
			shutdown = true;
		}
		LOG.trace("Exit shutdown");
	}

}
