package bildverwaltung.container.shutdown;

/**
 * This interface is used as a pattern for actions, that are to be executed on
 * container shutdown<br>
 * Every Task can be linked to a specific {@link ShutdownPhase}<br>
 * There is no guaranty about an execution order, inside a
 * {@link ShutdownPhase}<br>
 * 
 * @author Jannik
 *
 */
public interface ShutdownTask {

	public void execute() throws ContainerShutdownException;

	public default ShutdownPhase getExecutionPhase() {
		return ShutdownPhase.PREPAIR_APP_SHUTDOWN;
	}

	public default String getTaskName() {
		return this.getClass().getSimpleName();
	}

}
