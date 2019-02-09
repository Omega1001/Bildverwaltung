package bildverwaltung.gui.fx.masterview.dialogs;

import bildverwaltung.dao.entity.Album;
import bildverwaltung.localisation.Messenger;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class AlbumCreationDialog extends Stage{

	public static Album createAlbum(Messenger msg, Window parent) {
		AlbumCreationDialog dlg = new AlbumCreationDialog(msg,parent);
		dlg.showAndWait();
		return dlg.acepted ? dlg.generate() : null;
	}

	private TextField name;
	private TextArea comment;
	private boolean acepted = false;

	private AlbumCreationDialog(Messenger msg, Window parent) {
		initModality(Modality.APPLICATION_MODAL);
		initStyle(StageStyle.UTILITY);
		setResizable(false);
		if (parent != null) {
			initOwner(parent);
		}else {
			centerOnScreen();
		}

		VBox layout = new VBox(10d);
		layout.setPadding(new Insets(10d, 10d, 10d, 10d));
		
		GridPane gp = new GridPane();
		gp.setHgap(5d);
		gp.setVgap(10d);
		
		Label lblName = new Label(msg.translate("labelMasterViewAlbumSelecionDlgAlbumName"));
		name = new TextField();
		gp.add(lblName, 0, 0);
		gp.add(name, 1, 0);
		
		Label lblComment = new Label(msg.translate("labelMasterViewAlbumSelecionDlgAlbumComment"));
		comment = new TextArea();
		gp.add(lblComment, 0, 1);
		gp.add(comment, 1, 1);
		
		
		layout.getChildren().addAll(gp, createButtons(msg));
		setScene(new Scene(layout));
	}

	private Node createButtons(Messenger msg) {
		HBox buttons = new HBox(10d);
		Label spacer = new Label();
		spacer.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(spacer, Priority.ALWAYS);
		Button accept = new Button(msg.translate("btnAppOk"));
		accept.setOnAction((e) -> {
			acepted = true;
			this.hide();
		});
		HBox.setHgrow(accept, Priority.NEVER);
		Button cancle = new Button(msg.translate("btnAppCancel"));
		cancle.setOnAction((e) -> this.hide());
		HBox.setHgrow(cancle, Priority.NEVER);
		buttons.getChildren().addAll(spacer, accept, cancle);
		return buttons;
	}

	private Album generate() {
		Album res = new Album();
		res.setName(name.getText());
		res.setComment(comment.getText());
		return res;
	}

}
