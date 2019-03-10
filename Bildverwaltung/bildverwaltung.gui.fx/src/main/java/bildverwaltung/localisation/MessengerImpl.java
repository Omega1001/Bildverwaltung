package bildverwaltung.localisation;

import java.io.PrintWriter;
import java.io.StringWriter;

import bildverwaltung.container.Factory;
import bildverwaltung.container.ManagedContainer;
import bildverwaltung.container.Scope;
import bildverwaltung.dao.exception.FacadeException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;

public class MessengerImpl implements Messenger {

	public static final Factory<Messenger> FACTORY = new Factory<Messenger>() {

		@Override
		public Class<Messenger> getInterfaceType() {
			return Messenger.class;
		}

		@Override
		public Messenger generate(ManagedContainer container, Scope scope) {
			Translator t = container.materialize(Translator.class);
			return new MessengerImpl(t);
		}
	};
	private final Translator translator;

	public MessengerImpl(Translator translator) {
		super();
		if (translator == null) {
			throw new IllegalArgumentException("Translator must be set");
		}
		this.translator = translator;
	}

	@Override
	public Translator getTranslator() {
		return translator;
	}

	public String translate(String resourceKey) {
		return resourceKey != null ? translator.translate(resourceKey) : null;
	}

	public void showInfoMessage(String headerText, String details) {
		showNtInfoMessage(translate(headerText), translate(details));
	}

	public void showWarningMessage(String headerText, String details) {
		showNtWarningMessage(translate(headerText), translate(details));
	}

	public void showErrorMessage(String headerText, String details) {
		showNtErrorMessage(translate(headerText), translate(details));
	}

	public void showMessage(String headerText, String details, MessageType type) {
		showNtMessage(translate(headerText), translate(details), type);
	}

	public void showNtInfoMessage(String headerText, String details) {
		showNtMessage(headerText, details, MessageType.INFO);
	}

	public void showNtWarningMessage(String headerText, String details) {
		showNtMessage(headerText, details, MessageType.WARNING);
	}

	public void showNtErrorMessage(String headerText, String details) {
		showNtMessage(headerText, details, MessageType.ERROR);
	}

	public void showNtMessage(String headerText, String details, MessageType type) {
		showMessage(translate(type.getTitleBarRs()), headerText, details, type.getType());
	}

	private void showMessage(String titleBar, String headerText, String details, AlertType type) {
		Alert msg = new Alert(type);
		msg.setTitle(titleBar);
		msg.setHeaderText(headerText);
		msg.setContentText(details);
		msg.showAndWait();
	}

	public void showExceptionMessage(FacadeException ex) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(translate(MessageType.ERROR.getTitleBarRs()));

		String headerRs = ex.getType().getErrorRs();
		alert.setHeaderText(translate(headerRs != null ? headerRs : "msgExceptionUnknown"));

		String detailRs = ex.getType().getErrorDetailRs();
		if (detailRs != null) {
			alert.setContentText(translate(detailRs));
		}

		String trace = extractStackTrace(ex);
		TextArea traceArea = generateScralableTextContainer(trace);
		alert.getDialogPane().setExpandableContent(traceArea);

		alert.showAndWait();
	}

	private String extractStackTrace(FacadeException ex) {
		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		return sw.toString();
	}

	private TextArea generateScralableTextContainer(String content) {
		TextArea textArea = new TextArea(content);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		return textArea;
	}

}
