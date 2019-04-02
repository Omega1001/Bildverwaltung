package bildverwaltung.gui.fx.masterview.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class SavingDialog {
    private Alert alert;
    public enum selected{CANCEL,OVERRIDE,SAVE_NEW}
    private ButtonType override;
    private ButtonType cancel;
    private ButtonType saveNew;

    public static selected showAndWait(){
        SavingDialog sd = new SavingDialog();
        Optional<ButtonType> result = sd.alert.showAndWait();
        if(result.get()==sd.override){
            return selected.OVERRIDE;
        }else if(result.get()==sd.saveNew){
            return selected.SAVE_NEW;
        }else {
            return selected.CANCEL;
        }
    }

    private SavingDialog() {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        override = new ButtonType("override", ButtonBar.ButtonData.OK_DONE);
        cancel = new ButtonType("cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        saveNew = new ButtonType("save new", ButtonBar.ButtonData.OTHER);
        alert.getButtonTypes().removeAll(ButtonType.OK,ButtonType.CLOSE,ButtonType.CANCEL);
        alert.getButtonTypes().addAll(saveNew,override,cancel);
    }
}
