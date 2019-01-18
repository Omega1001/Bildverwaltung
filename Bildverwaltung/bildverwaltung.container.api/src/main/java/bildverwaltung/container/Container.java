package bildverwaltung.container;

import java.io.Closeable;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Container {

	private static final Logger LOG = LoggerFactory.getLogger(Container.class);
	private static ManagedContainer activeContainer = null;
	private static boolean shutdown = false;
	
	public static ManagedContainer getActiveContainer() {
		if(shutdown) {
			LOG.error("Tried to access the container after it was shutdown");
			throw new IllegalStateException("Container already shut down");
		}
		if(activeContainer == null) {
			LOG.warn("Tried to fetch the container before it was started, starting it now");
			startupContainer();
		}
		return activeContainer;
	}

	public static void startupContainer() {
		if(activeContainer != null) {
			LOG.warn("Tried to start container, but it was already started");
			return;
		}
		activeContainer = ContainerLoader.loadContainer();
	}
	
	public static void shutdown() {
		if(activeContainer == null) {
			LOG.error("Tried to shutdown the container, but it was never started");
			throw new IllegalStateException("The container is not started");
		}
		if(shutdown) {
			LOG.warn("Tried to shutdown the container, but it was already shutdowned, do nothing");
			return;
		}
		if(activeContainer instanceof Closeable) {
			try {
				((Closeable)activeContainer).close();
			} catch (IOException e) {
				LOG.error("Error during shutting down the container : ",e);
			}
		}else {
			LOG.debug("Container is not closable, marking it as shutdowned without taking aktion");
		}
		shutdown = true;
	}
	
}
