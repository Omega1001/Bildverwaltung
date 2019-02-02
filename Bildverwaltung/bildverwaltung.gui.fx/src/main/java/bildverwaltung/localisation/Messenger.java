package bildverwaltung.localisation;

import bildverwaltung.dao.exception.FacadeException;
import javafx.scene.control.Alert.AlertType;

public interface Messenger {
	
	public Translator getTranslator();

	public String translate(String resourceKey);

	public void showInfoMessage(String headerText, String details);

	public void showWarningMessage(String headerText, String details);

	public void showErrorMessage(String headerText, String details);

	public void showMessage(String headerText, String details, MessageType type);

	public void showNtInfoMessage(String headerText, String details);

	public void showNtWarningMessage(String headerText, String details);

	public void showNtErrorMessage(String headerText, String details);

	public void showNtMessage(String headerText, String details, MessageType type);

	public void showExceptionMessage(FacadeException ex);

	public static enum MessageType {
		/**
		 * Indicates, that a message is an information
		 */
		INFO(AlertType.INFORMATION, "aleartHeaderInfoMessage"),
		/**
		 * Indicates, that a message is a warning
		 */
		WARNING(AlertType.WARNING, "aleartHeaderWarningMessage"),
		/**
		 * Indicates, that a message describes an error
		 */
		ERROR(AlertType.ERROR, "aleartHeaderErrorMessage");
		private AlertType type;
		private String titleBarRs;

		private MessageType(AlertType type, String titleBarRs) {
			this.type = type;
			this.titleBarRs = titleBarRs;
		}

		public AlertType getType() {
			return type;
		}

		public String getTitleBarRs() {
			return titleBarRs;
		}
		
		
	}

}
