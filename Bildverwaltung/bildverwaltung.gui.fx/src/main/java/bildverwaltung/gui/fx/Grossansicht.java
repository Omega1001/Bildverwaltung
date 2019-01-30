import java.awt.Panel;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler; 
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage; 

public class Grossansicht extends Application{
	private static final Logger log = Logger.getLogger( Grossansicht.class.getName() );
	private static final String SAVE = "Speichern";
	private static final String LAST_PIC = "Letztes Bild";
	private static final String FULL_PIC = "Vollbild";
	private static final String NEXT_PIC = "Nächstes Bild";
	private static final String DELETE_PIC = "Löschen";
	private static final String COMMENTAR = "Kommentar";
	
	private static final String WARNING_DELETE = "Wollen Sie dieses Bild löschen?";
	
	private static final double HEIGHT = 800.0;
	private static final double WIDTH = 600.0;
	private static final int PAD = 15;
	Stage stage; 
	@Override
	public void start(final Stage stage) throws Exception {
		log.info( "Grossansicht" );
		try {
			//die Spacer Methoden dienen nur der provisorischen Positionierung der Buttons
			//Sobald die Positionierung stimmt, koennen die dann ausgetauscht/geloescht werden
				stage.setResizable(true);
				final Pane leftSpacer = new Pane();
		        HBox.setHgrow(
		                leftSpacer,
		                Priority.SOMETIMES
		        );
		
		        final Pane rightSpacer = new Pane();
		        HBox.setHgrow(
		                rightSpacer,
		                Priority.SOMETIMES
		        );
		        Pane midSpacer = new Pane();
		        HBox.setHgrow(
		        		midSpacer,
		        		Priority.SOMETIMES
		        );
		        Pane middleSpacer = new Pane();
		        HBox.setHgrow(
		        		middleSpacer,
		        		Priority.SOMETIMES
		        );
			
		        /**
			 * Die ToolBar toolBarTop soll die Buttons wie zum Schnellzugriff enthalten. Vorerst nur Speichern
			 */
			//Statt "Speichern" kann man hier auch Button-Image verwenden.
				ToolBar toolBarTop = new ToolBar();
				Button buttonSave = new Button(SAVE);
				toolBarTop.getItems().addAll(buttonSave);
			
				Tooltip tooltipSave = new Tooltip(SAVE);
			    	buttonSave.setTooltip(tooltipSave);
			    	tooltipSave.setTextAlignment(TextAlignment.RIGHT);
			
			   /**
			    * Die ToolBar toolBarBottom enthaelt die ueblichen Media-Anzeige-Buttons. (Naechstes, Letztes, Vollbild, Loeschen
			    */  
			    ToolBar toolBarBottom = new ToolBar();
			    Pane tBPane = new Pane();
				//"<" ist nur ein Platzhalter
			
			   /**
			    * Der Button Back laesst das vorherige Bild in der Grossansicht anzeigen
			    */  
			    Button buttonBack = new Button("<");
			    
			    buttonBack.setOnAction(new EventHandler<ActionEvent>() {
		
		            public void handle(ActionEvent event) {
		                //letztes Bild anzeigen
		            }
		        });
			    
			   /**
			    * Mit Tooltip kann man neben den Buttons eine Erklaerung anzeigen lassen
			    */ 
			    Tooltip tooltipBack = new Tooltip(LAST_PIC );
			    buttonBack.setTooltip(tooltipBack);
			    tooltipBack.setTextAlignment(TextAlignment.RIGHT);
			    buttonBack.setOnAction(new EventHandler<ActionEvent>() {
		
		            public void handle(ActionEvent event) {
		            	 //naechstes Bild.
		                 
		            }
		        });
			   /**
			    * Der Button FullScreen oeffnet und schliesst den FullScreen.
			    */  
			    //"O" ist nur ein Platzhalter
			    Button buttonFullScreen = new Button ("O");
			    
			    Tooltip tooltipFullScreen = new Tooltip(FULL_PIC);
			    buttonFullScreen.setTooltip(tooltipFullScreen);
			    tooltipFullScreen.setTextAlignment(TextAlignment.RIGHT);
			    buttonFullScreen.setOnAction(new EventHandler<ActionEvent>() {
		
		            public void handle(ActionEvent event) {
		            	if (stage.isFullScreen()==false) {
		            		stage.setFullScreen(true);
		            	}else {
		            		stage.setFullScreen(false);
		            	}
		                 
		            }
		        });
			    
			    /**
			    * Der Button Next laesst das nachste Bild in der Grossansicht anzeigen
			    */ 
			    //">" ist nur ein Platzhalter
			    Button buttonNext = new Button (">");
			    Tooltip tooltipNext = new Tooltip(NEXT_PIC);
			    buttonNext.setTooltip(tooltipNext);
			    tooltipNext.setTextAlignment(TextAlignment.RIGHT);
			    
			   /**
			    * Der Button Comment soll ein Kommentar zu einem Bild speichern und bei druecken erscheinen bzw. ausblenden
			    */ 
			    // Wusste jetzt nicht, ob wir Kommentare schon zulassen, als Attribute?
			    //"C" ist nur ein Platzhalter
			    Button buttonComment = new Button ("C");
			    Tooltip tooltipComment = new Tooltip(COMMENTAR);
			    buttonComment.setTooltip(tooltipComment);
			    tooltipComment.setTextAlignment(TextAlignment.RIGHT);
			    buttonDelete.setOnAction(new EventHandler<ActionEvent>() {
		        	public void handle(ActionEvent event) {	
					
				}
			    /**
			    * Der Button Delete loescht das angezeigte Bild mit Warnung-Dialog. Bei erfolgreichem Loeschen wird das naechste Bild angezeigt, oder die Grossansicht beendet.
			    */ 	
			    //"X" ist nur ein Platzhalter
			    Button buttonDelete = new Button ("X");
		
			    Tooltip tooltipDelete = new Tooltip(DELETE);
			    buttonDelete.setTooltip(tooltipDelete);
			    tooltipDelete.setTextAlignment(TextAlignment.RIGHT);
			    
			     buttonDelete.setOnAction(new EventHandler<ActionEvent>() {
		        	public void handle(ActionEvent event) {		            	
					Alert alert = new Alert(AlertType.CONFIRMATION);
		            		alert.setTitle(DELETE);
		            		alert.setHeaderText(WARNING_DELETE);

		            		Optional<ButtonType> result = alert.showAndWait();
		            		if (result.get() == ButtonType.OK){
		            			//Bild loeschen
		            		} else {
		            			// bei Abbrechen und Meldung schliessen: neachstes verfuegbare Bild anzeigen, oder Grossansicht beenden
		            		}
		                 
		            	}
		       	 });
		            }
		        });
			    
			    //toolBarBottom.getItems().addAll(buttonBack,buttonFullScreen,buttonNext,buttonComment,buttonDelete);
			    
				//Wie funktioniert das Positionieren der Buttons??
			    toolBarBottom.getItems().addAll(leftSpacer,midSpacer,buttonBack,buttonFullScreen,buttonNext,middleSpacer, buttonComment, rightSpacer, buttonDelete);
				TilePane view = new TilePane(); 
			    view.setPadding(new Insets(PAD, PAD, PAD, PAD)); 
			    view.setHgap(PAD); 
		
					BorderPane borderPane = new BorderPane();
			 	borderPane.setTop(toolBarTop);
			 	borderPane.setBottom(toolBarBottom);
			 	Scene scene = new Scene(borderPane, WIDTH, HEIGHT);
			 	
				stage.setScene(scene);
			    stage.show();
		}
		catch(Exception e){
			 log.log( Level.SEVERE, "Fehler ist in der Grossansicht aufgetreten", e );
		    }
	}
	


	 public static void main(String[] args) {
	        Application.launch(args);
	    }
}
