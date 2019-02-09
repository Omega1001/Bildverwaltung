package bildverwaltung.gui.fx.masterview;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class EnlargedPictureToolBar{
	
	private static final Logger LOG = LoggerFactory.getLogger(EnlargedPictureToolBar.class);
	private static final String LAST_PIC = "Letztes Bild";
	private static final String FULL_PIC = "Vollbild";
	private static final String NEXT_PIC = "Naechstes Bild";
	
	private static final double BUTTON_SIZE = 40.0;
	private static final double BTN_SELECT_SPACING= 5.0;
	
	private Supplier<EnlargedPictureView> enlargedPicture;
	
	public static ToolBar buildToolBar(){
		
		Stage newWindow = new Stage();
		
		//Bottom toolBar contains buttons to select a pic 
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
	    
	    //show Picture in FullScreen Modus
	    Button buttonFullScreen = new Button ("O");
	    showButtonwithFunction(buttonFullScreen, FULL_PIC, BUTTON_SIZE);
	    buttonFullScreen.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	newWindow.setFullScreen(true);
                 
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
	    centerButtons.getChildren().addAll(buttonBack, buttonFullScreen, buttonNext);
        
        
        HBox components = new HBox();
	    components.getChildren().addAll(centerButtons);
	    components.setSpacing(90.0);
	    
	    HBox position = new HBox();
	    position.setAlignment(Pos.BASELINE_LEFT);
	    position.setMinWidth(220.0);
	   
	    toolBarBottom.getItems().addAll(position,components);
	    
	    return toolBarBottom;
	    
	}
	
	private static void showButtonwithFunction(Button btn, String viewText, double size) {
		btn.setMinWidth(size);
	    btn.setPrefWidth(size);
	    btn.setMaxWidth(size);
	    
		Tooltip toolTip = new Tooltip(viewText);
	    btn.setTooltip(toolTip);
	    toolTip.setTextAlignment(TextAlignment.RIGHT);
	}

}
