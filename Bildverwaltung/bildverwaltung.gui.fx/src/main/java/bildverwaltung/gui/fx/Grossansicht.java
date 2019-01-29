import java.awt.Panel;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler; 
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
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
	
	//int Bewertung =0;
	  Stage stage; 
	@Override
	public void start(final Stage stage) throws Exception {
		log.info( "Grossansicht" );
		try {
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
		        
				ToolBar toolBarTop = new ToolBar();
				Button buttonSave = new Button("Speichern");
				toolBarTop.getItems().addAll(buttonSave);
			     
			    ToolBar toolBarBottom = new ToolBar();
			    Pane tBPane = new Pane();
			    Button buttonBack = new Button("<");
			    
			    buttonBack.setOnAction(new EventHandler<ActionEvent>() {
		
		            public void handle(ActionEvent event) {
		                //letztes Bild anzeigen
		            }
		        });
			    
			    Tooltip tooltipBack = new Tooltip("Letztes Bild");
			    buttonBack.setTooltip(tooltipBack);
			    tooltipBack.setTextAlignment(TextAlignment.RIGHT);
			    buttonBack.setOnAction(new EventHandler<ActionEvent>() {
		
		            public void handle(ActionEvent event) {
		            	 //naechstes Bild.
		                 
		            }
		        });
			    
			    Button buttonFullScreen = new Button ("O");
			    
			    Tooltip tooltipFullScreen = new Tooltip("Vollbild");
			    buttonFullScreen.setTooltip(tooltipFullScreen);
			    tooltipFullScreen.setTextAlignment(TextAlignment.RIGHT);
			    buttonFullScreen.setOnAction(new EventHandler<ActionEvent>() {
		
		            public void handle(ActionEvent event) {
		            	 stage.setFullScreen(true);
		                 
		            }
		        });
			    
			    
			    Button buttonNext = new Button (">");
			    Tooltip tooltipNext = new Tooltip("Nächstes Bild");
			    buttonNext.setTooltip(tooltipNext);
			    tooltipNext.setTextAlignment(TextAlignment.RIGHT);
			    
			    Button buttonComment = new Button ("C");
			    Tooltip tooltipComment = new Tooltip("Kommentar einfügen");
			    buttonComment.setTooltip(tooltipComment);
			    tooltipComment.setTextAlignment(TextAlignment.RIGHT);
			    
			    
			    Button buttonDelete = new Button ("X");
		
			    Tooltip tooltipDelete = new Tooltip("Löschen");
			    buttonDelete.setTooltip(tooltipDelete);
			    tooltipDelete.setTextAlignment(TextAlignment.RIGHT);
			    
			    
			    
			    //toolBarBottom.getItems().addAll(buttonBack,buttonFullScreen,buttonNext,buttonComment,buttonDelete);
			    
				//Wie funktioniert das Positionieren der Buttons??
			    toolBarBottom.getItems().addAll(leftSpacer,midSpacer,buttonBack,buttonFullScreen,buttonNext,middleSpacer, buttonComment, rightSpacer, buttonDelete);
				TilePane view = new TilePane(); 
			    view.setPadding(new Insets(15, 15, 15, 15)); 
			    view.setHgap(15); 
		
					BorderPane borderPane = new BorderPane();
			 	borderPane.setTop(toolBarTop);
			 	borderPane.setBottom(toolBarBottom);
			 	Scene scene = new Scene(borderPane, 500, 600);
			 	
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
