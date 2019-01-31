package bildverwaltung.dao.exception;

public class FacadeException extends BildverwaltungException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4327530936107104212L;
	private final boolean rollback;
	
	public FacadeException(ExceptionType type,boolean rollback, Throwable cause) {
		super(type, cause);
		this.rollback = rollback;
	}

	public FacadeException(ExceptionType type,boolean rollback) {
		super(type);
		this.rollback = rollback;
	}
	
	public FacadeException(ExceptionType type, Throwable cause) {
		this(type,true,cause);
	}
	
	public FacadeException(ExceptionType type) {
		this(type,true);
	}

	public boolean isRollback() {
		return rollback;
	}
}
