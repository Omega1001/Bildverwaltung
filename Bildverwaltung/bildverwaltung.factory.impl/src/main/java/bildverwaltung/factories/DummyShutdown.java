package bildverwaltung.factories;

import bildverwaltung.container.shutdown.ContainerShutdownException;
import bildverwaltung.container.shutdown.ShutdownTask;

public class DummyShutdown implements ShutdownTask {

	@Override
	public void execute() throws ContainerShutdownException {
		// TODO Auto-generated method stub

	}

}
