package bildverwaltung.gui.fx.util;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bildverwaltung.localisation.Messenger;
import javafx.scene.Node;

public abstract class RebuildebleSubComponent {
	private static final Logger LOG = LoggerFactory.getLogger(RebuildebleSubComponent.class);
	private final Messenger msg;

	private Node graphic = null;
	private ReadWriteLock lock = new ReentrantReadWriteLock(true);

	public RebuildebleSubComponent(Messenger msg) {
		super();
		this.msg = msg;
	}

	protected final Messenger msg() {
		return msg;
	}

	public final Node getGraphic() {
		try {
			lock.readLock().lock();
			if (graphic == null) {
				lock.readLock().unlock();
				rebuild();
				lock.readLock().lock();
			}
			return graphic;
		} finally {
			lock.readLock().unlock();
		}
	}

	protected abstract Node build();

	protected void rebuildSubComponents() {
		//Do nothing
	}

	public final void rebuild() {
		LOG.trace("Enter rebuild");
		try {
			lock.writeLock().lock();
			LOG.debug("{} : gui component is being rebuild",this.getClass().getSimpleName());
			rebuildSubComponents();
			graphic = build();
		} finally {
			lock.writeLock().unlock();
		}
		LOG.trace("Exit rebuild");
	}
}
