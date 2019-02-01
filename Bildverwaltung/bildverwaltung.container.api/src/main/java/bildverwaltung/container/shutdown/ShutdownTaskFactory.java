package bildverwaltung.container.shutdown;

import bildverwaltung.container.Factory;
import bildverwaltung.container.ManagedContainer;
import bildverwaltung.container.Scope;

public class ShutdownTaskFactory implements Factory<ShutdownTask> {

	private Class<? super ShutdownTask> type;

	public ShutdownTaskFactory(Class<? super ShutdownTask> type) {
		this.type = type;
	}

	@Override
	public Class<ShutdownTask> getInterfaceType() {
		return ShutdownTask.class;
	}

	@Override
	public ShutdownTask generate(ManagedContainer container, Scope scope) {
		try {
			return (ShutdownTask) type.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
