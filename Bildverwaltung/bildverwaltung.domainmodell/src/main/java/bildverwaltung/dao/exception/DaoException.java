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

}
