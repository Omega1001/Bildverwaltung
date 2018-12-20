package bildverwaltung.dao.exception;

public class ServiceException extends FacadeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2575275221019002414L;

	public ServiceException(ExceptionType type, Throwable cause) {
		super(type, cause);
	}

	public ServiceException(ExceptionType type) {
		super(type);
	}

}
