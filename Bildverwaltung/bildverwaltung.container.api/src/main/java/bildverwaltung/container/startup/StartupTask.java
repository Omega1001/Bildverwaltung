package bildverwaltung.container.startup;

public interface StartupTask {

	public void execute() throws ContainerStartupException;
	
	public default StartUpPhase getExecutionPhase() {
		return StartUpPhase.PREPAIR_APP;
	}
	
}
