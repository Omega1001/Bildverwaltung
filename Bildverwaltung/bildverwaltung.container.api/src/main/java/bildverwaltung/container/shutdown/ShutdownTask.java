package bildverwaltung.container.shutdown;

public interface ShutdownTask {

	public void execute() throws ContainerShutdownException;
	
	public default ShutdownPhase getExecutionPhase() {
		return ShutdownPhase.PREPAIR_APP_SHUTDOWN;
	}
	
	public default String getTaskName() {
		return this.getClass().getSimpleName();
	}
	
}
