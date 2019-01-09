package bildverwaltung.dao.exception;

public enum ExceptionType {

	;
	private final String message;
	private final String errorRs;
	private final String errorDetailRs;
	
	
	private ExceptionType(String message, String errorRs,
			String errorDetailRs) {
		this.message = message;
		this.errorRs = errorRs;
		this.errorDetailRs = errorDetailRs;
	}


	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}


	/**
	 * @return the errorRs
	 */
	public String getErrorRs() {
		return errorRs;
	}


	/**
	 * @return the errorDetailRs
	 */
	public String getErrorDetailRs() {
		return errorDetailRs;
	}
	
	
}
