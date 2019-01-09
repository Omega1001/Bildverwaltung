package bildverwaltung.gui.fx;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class Main extends Application {

	int HEIGTH = 800;
	int WIDTH = 1000;
	Color FARBE = Color.WHITE;

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Bilderverwaltung");
		Scene scene = new Scene(new VBox(), WIDTH, HEIGTH, FARBE);
		primaryStage.setResizable(true);
		// MenueLeiste
		MenuBar menuBar = new MenuBar();
		// Menu File
		Menu menuDatei = new Menu("Datei");
		// Menu Hilfe
		Menu menuHilfe = new Menu("Hilfe");
		// Menu Ansicht: Dunkel/Hell, Groﬂe/Kleine Kacheln oder Liste ??
		Menu menuAnsicht = new Menu("Ansicht");
		menuBar.getMenus().addAll(menuDatei, menuHilfe, menuAnsicht);
		((VBox) scene.getRoot()).getChildren().addAll(menuBar);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
