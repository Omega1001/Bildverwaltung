package bildverwaltung.dao.exception;

public class BildverwaltungException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7705234368377219830L;

	private ExceptionType type;

	public BildverwaltungException(ExceptionType type, Throwable cause) {
		super(type != null ? type.getMessage() : "", cause);
		if(type == null) {
			throw new IllegalArgumentException("Exception Type must not be Empty",cause);
		}
		this.type = type;
	}

	public BildverwaltungException(ExceptionType type) {
		super(type != null ? type.getMessage() : "");
		if(type == null) {
			throw new IllegalArgumentException("Exception Type must not be Empty");
		}
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public ExceptionType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ExceptionType type) {
		this.type = type;
	}
	
}
