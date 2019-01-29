	import java.io.File;
import java.util.List;

import javafx.application.Application;
	import javafx.event.ActionEvent;
	import javafx.event.EventHandler;
	import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
	import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
	import javafx.scene.paint.Color;
	import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
	 
	public class KontextMenue extends Application { 
	    @Override
	    public void start(Stage stage) {
	 
	        Label label = new Label();
	 
	        	//Test mit Kreis:
	        Circle image = new Circle();
	        image.setRadius(80);
	        image.setFill(Color.GREY);
	 
	        VBox root = new VBox();
	        root.setPadding(new Insets(5));
	        root.setSpacing(5);
	 
	        root.getChildren().addAll(label, image);
	        
	        ContextMenu contextMenu = new ContextMenu();
	 
	        Menu menuSave = new Menu("Speichern");
	        MenuItem saveAs = new MenuItem("Speichern Unter");
	        MenuItem movePic= new MenuItem("In Album Verschieben");
	        menuSave.getItems().addAll(saveAs, movePic);
	        //menuSave.getItems().add(new CheckMenuItem("Speichern Unter"));
	        //menuSave.getItems().add(new CheckMenuItem("In Album Verschieben"));
	        saveAs.setOnAction(new EventHandler<ActionEvent>() {
	        
	        	
	            @Override
	            public void handle(ActionEvent event) {
	                //zuaetzliches speichern in anderem Ordner);
	            }
	        });
	        
	        movePic.setOnAction(new EventHandler<ActionEvent>() {
	        
	        	
	            @Override
	            public void handle(ActionEvent event) {
	                //Verschieben des Bildes in anderen Ordner
	            }
	        });
	        
	        Menu menuAttribut = new Menu("Ändern");
	        MenuItem changeName = new MenuItem("Namen Ändern");
	        MenuItem changeComment = new MenuItem("Kommentar ändern");
	        MenuItem changeInfos = new MenuItem("Informationen ändern"); 
	        menuAttribut.getItems().addAll(changeName, changeComment, changeInfos);
	        
	        changeName.setOnAction(new EventHandler<ActionEvent>() {
	        
	        	
	            @Override
	            public void handle(ActionEvent event) {
	                //Namen des Bildes aendern?
	            }
	        });
	        
	        changeComment.setOnAction(new EventHandler<ActionEvent>() {
	        
	        	
	            @Override
	            public void handle(ActionEvent event) {
	                //Kommentar einfuegen/aendern
	            }
	        });
	        
	        changeInfos.setOnAction(new EventHandler<ActionEvent>() {
	        
	        	
	            @Override
	            public void handle(ActionEvent event) {
	                //Informationen = saemtliche Attribute, die manuell geandert werden koennen wie bspw. Filter/Suchkriterien
	            }
	        });
	        
	        
	        Menu menuDelete = new Menu("Löschen");
	        MenuItem deleteSingle = new MenuItem("Einzeln Löschen"); 
	        MenuItem deleteAll = new MenuItem("Überall Löschen"); 
	        menuDelete.getItems().addAll(deleteSingle, deleteAll);
	        
	        deleteSingle.setOnAction(new EventHandler<ActionEvent>() {
	        
	            @Override
	            public void handle(ActionEvent event) {
	               //Einzelnes Bild loeschen
	            }
	        });
	 
	        deleteAll.setOnAction(new EventHandler<ActionEvent>() {
		        
	            @Override
	            public void handle(ActionEvent event) {
	               //Bild komplett aus der Datenbank loeschen
	            	//Sicherhaitsfrage? PopUp: Wirklich Löschen?
	            }
	        });
	 
	        contextMenu.getItems().addAll(menuSave, menuAttribut, menuDelete);
	
	        // Rechtsklick Event Menue
	        image.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
	 
	            @Override
	            public void handle(ContextMenuEvent event) {
	 
	                contextMenu.show(image, event.getScreenX(), event.getScreenY());
	            }
	        });
	 
	        Scene scene = new Scene(root, 400, 200);
	 
	        stage.setScene(scene);
	        stage.show();
	    }
	 
	    public static void main(String[] args) {
	        Application.launch(args);
	    }
	 
	}

