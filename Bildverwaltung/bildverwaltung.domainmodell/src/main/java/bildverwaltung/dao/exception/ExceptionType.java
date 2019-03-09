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
	

	APP_INI_PICTURES_DIR_MISSING("PicturesDirectory in section [directory] missing","",""),
	IMPORT_SAVING_PIC_TO_DB_FAILED("Adding picture entity to DB failed", "", ""),
    IMPORT_COPY_PIC_FAILED("Copying picture file to own directory failed","", ""),
	IMPORT_EXTRACT_ATTRIBS_FAILED("Extracting Attributes from picture file failed", "",""),
	NOT_A_PICTURE("Given picture file is not actually a picture", "", "")


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
