package bildverwaltung.dao.exception;

public class DaoException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6370668579979022769L;

	public DaoException(ExceptionType type, Throwable cause) {
		super(type, cause);
	}

	public DaoException(ExceptionType type) {
		super(type);
	}

	public DaoException(ExceptionType type, boolean rollback, Throwable cause) {
		super(type, rollback, cause);
	}

	public DaoException(ExceptionType type, boolean rollback) {
		super(type, rollback);
	}
	
	

}
