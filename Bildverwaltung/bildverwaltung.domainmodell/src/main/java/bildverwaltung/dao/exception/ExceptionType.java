package bildverwaltung.dao.exception;

public enum ExceptionType {
	UNKNOWN("Unknown error",null,null), 
	
	URI_RESOLUTION_0001("Unable to resolve uri : no compatible handler",null,null),
	URI_RESOLUTION_0002("Unable to resolve uri : error during reading target",null,null),
	
	
	ABS_DAO_0001("Can not save null object",null,null),
	ABS_DAO_0002("Can not delete key null",null,null),
	ABS_DAO_0003("Can not get key null",null,null), 
	ABS_DAO_0004("Error during saving entity",null,null),
	ABS_DAO_0005("Error during retrieving entity",null,null),
	ABS_DAO_0006("Error during deleting entity",null,null), 
	ABS_DAO_0007("Error during retrieving all Elements",null,null),
	

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
