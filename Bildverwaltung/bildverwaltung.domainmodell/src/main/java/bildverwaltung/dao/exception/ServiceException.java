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

	public ServiceException(ExceptionType type, boolean rollback, Throwable cause) {
		super(type, rollback, cause);
	}

	public ServiceException(ExceptionType type, boolean rollback) {
		super(type, rollback);
	}
	
	

}
