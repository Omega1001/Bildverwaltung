package bildverwaltung.gui.fx.util;

import java.util.Optional;

import bildverwaltung.localisation.Messenger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

public class ConfirmationDialog {

	public static boolean requestConfirmation(Messenger msg, String query) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(msg.translate("headlineAppConfirmationDlg"));
		alert.setHeaderText(msg.translate(query));
		
		ButtonType ok = new ButtonType(msg.translate("btnAppOk"),ButtonData.YES);
		ButtonType canel = new ButtonType(msg.translate("btnAppCancel"),ButtonData.NO);
		alert.getButtonTypes().setAll(canel,ok);
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ok) {
			return true;
		} else {
			return false;
		}
	}

}
