package bildverwaltung.container.shutdown;

import bildverwaltung.container.ContainerException;

public class ContainerShutdownException extends ContainerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -536901765595057275L;

	public ContainerShutdownException(String message, Throwable cause) {
		this(message,true, cause);
	}

	public ContainerShutdownException(String message) {
		this(message,true);
	}

	public ContainerShutdownException(Throwable cause) {
		this(true,cause);
	}
	
	public ContainerShutdownException(String message,boolean aboardStartup, Throwable cause) {
		super(message, cause);
	}

	public ContainerShutdownException(String message,boolean aboardStartup) {
		super(message);
	}

	public ContainerShutdownException(boolean aboardStartup,Throwable cause) {
		super(cause);
	}	
	
}
