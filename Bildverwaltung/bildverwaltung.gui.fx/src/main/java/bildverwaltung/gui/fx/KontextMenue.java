package bildverwaltung.gui.fx;

import java.util.Optional;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class KontextMenue extends Application {
	private static final int SPACING = 5;
	private static final String SAVE = "Speichern";
	private static final String SAVE_AS = "Speichern Unter";
	private static final String MOVE_PIC = "In Album Verschieben";
	private static final String CHANGE = "Ändern";
	private static final String CHANGE_NAME = "Namen Ändern";
	private static final String CHANGE_COMMENT = "Kommentar Ändern";
	private static final String CHANGE_INFOS = "Informationen ändern";
	private static final String DELETE = "Löschen";
	private static final String DELETE_SINGLE = "Einzeln Löschen";
	private static final String DELETE_EVERYWHERE = "Dieses Bild komplett Löschen";
	private static final String DELETE_ALL = "Alle Bilder Löschen";
	private static final String WARNING_SINGLE = "Wollen Sie dieses Bild hier löschen?";
	private static final String WARNING_EVERYWHERE = "Wollen Sie dieses Bild hier komplett entfernen";
	private static final String WARNING_ALL = "Wollen Sie alle Bilder hier löschen?";
	private static final String WARNING_TITLE = "SICHERHEITSFRAGE: LÖSCHEN";

	private static final double HEIGHT = 200.0;
	private static final double WIDTH = 400.0;

	@Override
	public void start(Stage stage) {

		Label label = new Label();

		// Test mit Kreis, die Bilder muessen erst noch auswaehlbar werden.
		Circle image = new Circle();
		image.setRadius(80);
		image.setFill(Color.GREY);

		VBox root = new VBox();
		root.setPadding(new Insets(SPACING));
		root.setSpacing(SPACING);

		root.getChildren().addAll(label, image);

		ContextMenu contextMenu = new ContextMenu();

		Menu menuSave = new Menu(SAVE);
		MenuItem saveAs = new MenuItem(SAVE_AS);
		MenuItem movePic = new MenuItem(MOVE_PIC);
		menuSave.getItems().addAll(saveAs, movePic);
		saveAs.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// zuaetzliches speichern in anderem Ordner);
			}
		});

		movePic.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// Verschieben des Bildes in anderen Ordner
			}
		});

		Menu menuAttribut = new Menu(CHANGE);
		MenuItem changeName = new MenuItem(CHANGE_NAME);
		MenuItem changeComment = new MenuItem(CHANGE_COMMENT);
		MenuItem changeInfos = new MenuItem(CHANGE_INFOS);
		menuAttribut.getItems().addAll(changeName, changeComment, changeInfos);

		changeName.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// Namen des Bildes aendern?
			}
		});

		changeComment.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// Kommentar einfuegen/aendern
			}
		});

		changeInfos.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// Informationen = saemtliche Attribute, die manuell geandert werden koennen wie
				// bspw. Filter/Suchkriterien
			}
		});

		Menu menuDelete = new Menu(DELETE);
		MenuItem deleteSingle = new MenuItem(DELETE_SINGLE);
		MenuItem deleteAll = new MenuItem(DELETE_ALL);
		MenuItem deleteEverywhere = new MenuItem(DELETE_EVERYWHERE);
		menuDelete.getItems().addAll(deleteSingle, deleteEverywhere, deleteAll);

		deleteSingle.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// Einzelnes Bild loeschen
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle(WARNING_TITLE);
				alert.setHeaderText(WARNING_SINGLE);

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					// Bild loeschen
				} else {
					// bei Abbrechen und Meldung schliessen: passiert nichts
				}
			}
		});
		deleteEverywhere.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// Bild komplett aus der Datenbank loeschen
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle(WARNING_TITLE);
				alert.setHeaderText(WARNING_EVERYWHERE);

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					// Bild loeschen
				} else {
					// bei Abbrechen und Meldung schliessen: passiert nichts
				}
			}
		});
		deleteAll.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// Alle Bilder loeschen; aber nur aus diesem Album, bzw, mit diesem
				// Filterkriterien..
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle(WARNING_TITLE);
				alert.setHeaderText(WARNING_ALL);

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					// Bild loeschen
				} else {
					// bei Abbrechen und Meldung schliessen: passiert nichts
				}
			}
		});

		contextMenu.getItems().addAll(menuSave, menuAttribut, menuDelete);

		image.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {

			@Override
			public void handle(ContextMenuEvent event) {

				contextMenu.show(image, event.getScreenX(), event.getScreenY());
			}
		});

		Scene scene = new Scene(root, WIDTH, HEIGHT);

		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
