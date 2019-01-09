package bildverwaltung.dao.exception;

public class FacadeException extends BildverwaltungException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4327530936107104212L;

	public FacadeException(ExceptionType type, Throwable cause) {
		super(type, cause);
	}

	public FacadeException(ExceptionType type) {
		super(type);
	}

}
