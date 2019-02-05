package bildverwaltung.gui.fx.masterview.dialogs;

import java.util.List;

import bildverwaltung.dao.entity.Album;
import bildverwaltung.gui.fx.util.StringProxy;
import bildverwaltung.localisation.Messenger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class AlbumSelectionDialog extends Stage {

	public static Album selectAlbum(Messenger msg, String query, List<Album> albums, Window parent) {
		AlbumSelectionDialog dlg = new AlbumSelectionDialog(msg, query, albums, parent);
		dlg.showAndWait();
		return dlg.acepted ? dlg.choise.getSelectionModel().getSelectedItem().getActual() : null;
	}

	private ComboBox<StringProxy<Album>> choise;
	private boolean acepted = false;

	private AlbumSelectionDialog(Messenger msg, String query, List<Album> albums, Window parent) {
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
		Label queryLabel = new Label(msg.translate(query));

		List<StringProxy<Album>> pAlbums = StringProxy.convertList(albums, (a) -> a.getName());
		ObservableList<StringProxy<Album>> items = FXCollections.observableArrayList(pAlbums);
		choise = new ComboBox<>(items);
		choise.getSelectionModel().selectFirst();
		choise.setMinWidth(200d);

		layout.getChildren().addAll(queryLabel, choise, createButtons(msg));
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

}
