package bildverwaltung.container.startup;

public interface StartupTask {

	public void execute() throws ContainerStartupException;
	
	public default StartUpPhase getExecutionPhase() {
		return StartUpPhase.PREPAIR_APP;
	}
	
	public default String getTaskName() {
		return this.getClass().getSimpleName();
	}
	
}
