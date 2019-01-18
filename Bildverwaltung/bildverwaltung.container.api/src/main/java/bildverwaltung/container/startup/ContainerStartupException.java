package bildverwaltung.container.startup;

import bildverwaltung.container.ContainerException;

public class ContainerStartupException extends ContainerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -536901765595057275L;
	private final boolean aboardStartup;

	public ContainerStartupException(String message, Throwable cause) {
		this(message,true, cause);
	}

	public ContainerStartupException(String message) {
		this(message,true);
	}

	public ContainerStartupException(Throwable cause) {
		this(true,cause);
	}
	
	public ContainerStartupException(String message,boolean aboardStartup, Throwable cause) {
		super(message, cause);
		this.aboardStartup = aboardStartup;
	}

	public ContainerStartupException(String message,boolean aboardStartup) {
		super(message);
		this.aboardStartup = aboardStartup;
	}

	public ContainerStartupException(boolean aboardStartup,Throwable cause) {
		super(cause);
		this.aboardStartup = aboardStartup;
	}

	public boolean isAboardStartup() {
		return aboardStartup;
	}
	
	
	
}
