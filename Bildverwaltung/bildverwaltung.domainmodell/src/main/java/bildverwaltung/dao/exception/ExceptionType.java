package bildverwaltung.dao.exception;

public enum ExceptionType {

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
