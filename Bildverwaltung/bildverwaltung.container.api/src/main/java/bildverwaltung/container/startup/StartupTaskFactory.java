package bildverwaltung.container.startup;

import bildverwaltung.container.Factory;
import bildverwaltung.container.ManagedContainer;
import bildverwaltung.container.Scope;

public class StartupTaskFactory implements Factory<StartupTask> {

	private Class<? super StartupTask> type;

	public StartupTaskFactory(Class<? super StartupTask> type) {
		this.type = type;
	}

	@Override
	public Class<StartupTask> getInterfaceType() {
		return StartupTask.class;
	}

	@Override
	public StartupTask generate(ManagedContainer container, Scope scope) {
		try {
			return (StartupTask) type.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
