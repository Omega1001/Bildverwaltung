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
	private static final String SAVE = "Speichern";
	private static final String LAST_PIC = "Letztes Bild";
	private static final String FULL_PIC = "Vollbild";
	private static final String NEXT_PIC = "Nächstes Bild";
	private static final String DELETE_PIC = "Löschen";
	private static final String COMMENTAR = "Kommentar";
	
	private static final String WARNING_DELETE = "Wollen Sie dieses Bild löschen?";
	
	private static final String HEIGHT = 800.0;
	private static final String WIDTH = 600.0;
	private static final String PAD = 15;
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
				Button buttonSave = new Button(SAVE);
				toolBarTop.getItems().addAll(buttonSave);
			     
			    ToolBar toolBarBottom = new ToolBar();
			    Pane tBPane = new Pane();
			    Button buttonBack = new Button("<");
			    
			    buttonBack.setOnAction(new EventHandler<ActionEvent>() {
		
		            public void handle(ActionEvent event) {
		                //letztes Bild anzeigen
		            }
		        });
			    
			    Tooltip tooltipBack = new Tooltip(LAST_PIC);
			    buttonBack.setTooltip(tooltipBack);
			    tooltipBack.setTextAlignment(TextAlignment.RIGHT);
			    buttonBack.setOnAction(new EventHandler<ActionEvent>() {
		
		            public void handle(ActionEvent event) {
		            	 //naechstes Bild.
		                 
		            }
		        });
			    
			    Button buttonFullScreen = new Button ("O");
			    
			    Tooltip tooltipFullScreen = new Tooltip(FULL_PIC);
			    buttonFullScreen.setTooltip(tooltipFullScreen);
			    tooltipFullScreen.setTextAlignment(TextAlignment.RIGHT);
			    buttonFullScreen.setOnAction(new EventHandler<ActionEvent>() {
		
		            public void handle(ActionEvent event) {
		            	 stage.setFullScreen(true);
		                 
		            }
		        });
			    
			    
			    Button buttonNext = new Button (">");
			    Tooltip tooltipNext = new Tooltip(NEXT_PIC);
			    buttonNext.setTooltip(tooltipNext);
			    tooltipNext.setTextAlignment(TextAlignment.RIGHT);
			    
			    Button buttonComment = new Button ("C");
			    Tooltip tooltipComment = new Tooltip(COMMENTAR);
			    buttonComment.setTooltip(tooltipComment);
			    tooltipComment.setTextAlignment(TextAlignment.RIGHT);
			    
			    
			    Button buttonDelete = new Button ("X");
		
			    Tooltip tooltipDelete = new Tooltip(DELETE);
			    buttonDelete.setTooltip(tooltipDelete);
			    tooltipDelete.setTextAlignment(TextAlignment.RIGHT);
			    
			    
			    
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
