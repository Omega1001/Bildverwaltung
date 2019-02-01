package bildverwaltung.container;

public class ContainerException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7420016984803861256L;

	public ContainerException(String message) {
		super(message);
	}

	public ContainerException(Throwable cause) {
		super(cause);
	}

	public ContainerException(String message, Throwable cause) {
		super(message, cause);
	}

}
