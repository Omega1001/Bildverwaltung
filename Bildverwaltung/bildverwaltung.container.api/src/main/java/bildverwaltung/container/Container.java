package bildverwaltung.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Container {

	private static final Logger LOG = LoggerFactory.getLogger(Container.class);
	private static ManagedContainer activeContainer = null;
	private static boolean shutdown = false;

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

	public static void startupContainer() {
		LOG.trace("Enter startupContainer");
		if (activeContainer != null) {
			LOG.warn("Tried to start container, but it was already started");
			return;
		}
		activeContainer = ContainerLoader.loadContainer();
		LOG.trace("Exit startupContainer");
	}

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
