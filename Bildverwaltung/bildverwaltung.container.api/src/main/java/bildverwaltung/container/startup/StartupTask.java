package bildverwaltung.container.startup;

/**
 * This interface is used as a pattern for actions, that are to be executed on
 * container start<br>
 * Every Task can be linked to a specific {@link StartUpPhase}<br>
 * There is no guaranty about an execution order, inside a
 * {@link StartUpPhase}<br>
 * 
 * @author Jannik
 *
 */
public interface StartupTask {

	public void execute() throws ContainerStartupException;

	public default StartUpPhase getExecutionPhase() {
		return StartUpPhase.PREPAIR_APP;
	}

	public default String getTaskName() {
		return this.getClass().getSimpleName();
	}

}
