package bildverwaltung.gui.fx.masterview;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class ShowPicture extends Application{
	
		private static final String SAVE = "Speichern";
		private static final String LAST_PIC = "Letztes Bild";
		private static final String FULL_PIC = "Vollbild";
		private static final String NEXT_PIC = "Naechstes Bild";
		private static final String DELETE_PIC = "Entfernen";
		private static final String COMMENTAR = "Kommentar";

		private static final double HEIGHT = 800.0;
		private static final double WIDTH = 600.0;
		
		private static final double BUTTON_SIZE = 40.0;
		private static final double MENU_BUTTON_SIZE = 80.0;
		private static final double BTN_SELECT_SPACING= 5.0;
		Stage stage;

		@Override
		public void start(final Stage stage) throws Exception {
			
			stage.setResizable(false);
	        
			ToolBar toolBarTop = new ToolBar();
			Button buttonSave = new Button(SAVE);
			
			toolBarTop.getItems().addAll(buttonSave);
		
			showButtonwithFunction(buttonSave, LAST_PIC, MENU_BUTTON_SIZE);
		
		   //Bottom toolBar contains buttons to select a pic, add a comment or delete it 
		    ToolBar toolBarBottom = new ToolBar();
		    
		
		   //show last Picture 
		    Button buttonBack = new Button("<");
		    //TODO Image imageBack = new Image(getClass().getResourceAsStream("Bildverwaltung/bildverwaltung.gui.fx/src/main/resources/Zurueck.png"));
		    //buttonBack.setGraphic(new ImageView(imageBack));   
		    showButtonwithFunction(buttonBack, LAST_PIC, BUTTON_SIZE);
		    buttonBack.setOnAction(new EventHandler<ActionEvent>() {	  
	
	            public void handle(ActionEvent event) {
	                //letztes Bild anzeigen
	            }
	        });
		    
		    			    
		    // show the next Picture
		   
		    Button buttonNext = new Button (">");
		  //TODO Image imageNext = new Image(getClass().getResourceAsStream("Bildverwaltung/bildverwaltung.gui.fx/src/main/resources/Weiter.png"));
		    //buttonBack.setGraphic(new ImageView(imageNext)); 
		    showButtonwithFunction(buttonNext, NEXT_PIC, BUTTON_SIZE);
		    buttonNext.setOnAction(new EventHandler<ActionEvent>() {
	        	public void handle(ActionEvent event) {
	        }});
		    
		    HBox centerButtons = new HBox();
		    centerButtons.setSpacing(BTN_SELECT_SPACING);
		    centerButtons.setMaxWidth(3.0*BUTTON_SIZE);		   
		    centerButtons.getChildren().addAll(buttonBack,buttonNext);
		    
		   // Comment
		    Button buttonComment = new Button ("C");
		  //TODO Image imageComment = new Image(getClass().getResourceAsStream("Bildverwaltung/bildverwaltung.gui.fx/src/main/resources/Kommentar.png"));
		    //buttonBack.setGraphic(new ImageView(imageComment)); 
		    showButtonwithFunction(buttonComment, COMMENTAR, BUTTON_SIZE);
		    buttonComment.setOnAction(new EventHandler<ActionEvent>() {
	        	public void handle(ActionEvent event) {
	        		//TODO Comment
	        }});
		    
		    //Delete the shown Image 	
		    Button buttonDelete = new Button ("X");
		    //TODO Image imageDelete = new Image(getClass().getResourceAsStream("Bildverwaltung/bildverwaltung.gui.fx/src/main/resources/Loeschen.png"));
		    //buttonBack.setGraphic(new ImageView(imageDelete)); 
		    showButtonwithFunction(buttonDelete, DELETE_PIC, BUTTON_SIZE);
		    buttonDelete.setOnAction(new EventHandler<ActionEvent>() {
	        	public void handle(ActionEvent event) {		            	
	        		//TODO Bild loeschen
	            }
	        });
		    
		    HBox components = new HBox();
		    components.getChildren().addAll(centerButtons, buttonComment,buttonDelete);
		    components.setSpacing(90.0);
		    
		    HBox position = new HBox();
		    position.setAlignment(Pos.BASELINE_LEFT);
		    position.setMinWidth(220.0);
		   
		    toolBarBottom.getItems().addAll(position,components);
		    
		    
			BorderPane borderPane = new BorderPane();
		 	borderPane.setTop(toolBarTop);
		 	borderPane.setBottom(toolBarBottom);
		 	Scene scene = new Scene(borderPane, WIDTH, HEIGHT);
		 	
			stage.setScene(scene);
		    stage.show();

		}

		private static void showButtonwithFunction(Button btn, String viewText, double size) {
			btn.setMinWidth(size);
		    btn.setPrefWidth(size);
		    btn.setMaxWidth(size);
		    
			Tooltip toolTip = new Tooltip(viewText);
		    btn.setTooltip(toolTip);
		    toolTip.setTextAlignment(TextAlignment.RIGHT);
		}

		public static void main(String[] args) {
			Application.launch(args);
		}
	}
