package bildverwaltung.gui.fx.enlargepicture;

import java.util.List;


import bildverwaltung.container.Container;
import bildverwaltung.container.Scope;
import bildverwaltung.gui.fx.masterview.PictureArea;
import bildverwaltung.gui.fx.util.IconLoader;
import bildverwaltung.gui.fx.util.RebuildebleSubComponent;
import bildverwaltung.localisation.Messenger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class EnlargedPictureToolBar extends RebuildebleSubComponent{
	
	private Messenger msg = Container.getActiveContainer().materialize(Messenger.class, Scope.APPLICATION);
	
	Stage newWindow = new Stage();
	private PictureArea viewablePicture = new PictureArea(msg);
	List<ImageView> list = viewablePicture.getList();
	public EnlargedPictureToolBar(Messenger msg, Stage newWindow, ImageView image) {
		super(msg);
		this.newWindow = newWindow;
	}

	
	private ImageView getImage(int index) {		
		return list.get(index);
	}
	
	private int index = viewablePicture.getIndex();
	
	private final String LAST_PIC = msg().translate("buttonEnlargedPictureToolBarLastPicture");
	private final String FULL_PIC = msg().translate("buttonEnlargedPictureToolBarFullPicture");
	private final String NEXT_PIC = msg().translate("buttonEnlargedPictureToolBarNextPicture");
	
	private static final double BUTTON_SIZE = 40.0;
	private static final double BTN_SELECT_SPACING= 10.0;
	
	public ToolBar buildToolBar(){
		
		
		newWindow.setResizable(true);
		//Bottom toolBar contains buttons to select a pic 
	    ToolBar toolBarBottom = new ToolBar();
		
		//show last Picture 
	    Button buttonBack = new Button(msg().translate("buttonEnlargedPictureToolBarLastPicture"));
	    buttonBack.setGraphic(IconLoader.loadIcon("Zurueck.png")); 
	    showButtonwithFunction(buttonBack, LAST_PIC, BUTTON_SIZE);
	    buttonBack.setOnAction(new EventHandler<ActionEvent>() {	  

            public void handle(ActionEvent event) {
            	if(index>0 && index<list.size()){
            		ImageView image = new ImageView();
            		image = getImage(index-1);
            		EnlargedPictureView pic = new EnlargedPictureView();
					pic.enlargePicture(image);            		
            	}            	
            }
        });
	    
	    //show Picture in FullScreen Modus
	    Button buttonFullScreen = new Button (msg().translate("buttonEnlargedPictureToolBarNextPicture"));
	    buttonFullScreen.setGraphic(IconLoader.loadIcon("Grossansicht.png"));
	    showButtonwithFunction(buttonFullScreen, FULL_PIC, BUTTON_SIZE);
	    buttonFullScreen.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	newWindow.setFullScreen(true);
                 
            }
	    });	
	    
	    // show the next Picture 
	    Button buttonNext = new Button ();
	    buttonNext.setGraphic(IconLoader.loadIcon("Weiter.png"));
	    showButtonwithFunction(buttonNext, NEXT_PIC, BUTTON_SIZE);
	    buttonNext.setOnAction(new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent event) {
        		if(index>0 && index<list.size()){
            		ImageView image = new ImageView();
            		image = getImage(index+1);
            		EnlargedPictureView pic = new EnlargedPictureView();
					pic.enlargePicture(image);   
					
        		}}});
	    
	    HBox centerButtons = new HBox();
	    centerButtons.setSpacing(BTN_SELECT_SPACING);
	    centerButtons.setMaxWidth(3.0*BUTTON_SIZE);		   
	    centerButtons.getChildren().addAll(buttonBack, buttonFullScreen, buttonNext);
        
        
        HBox components = new HBox();
	    components.getChildren().addAll(centerButtons);
	    components.setSpacing(90.0);
	    
	    HBox position = new HBox();
	    position.setAlignment(Pos.BASELINE_LEFT);
	    position.setMinWidth(Double.MAX_VALUE);
	   
	    toolBarBottom.getItems().addAll(position,centerButtons);
	    
	    return toolBarBottom;
	    
	}
	
	private static void showButtonwithFunction(Button btn, String viewText, double size){
		btn.setMinWidth(size);
	    btn.setPrefWidth(size);
	    btn.setMaxWidth(size);
	    
		Tooltip toolTip = new Tooltip(viewText);
	    btn.setTooltip(toolTip);
	    toolTip.setTextAlignment(TextAlignment.RIGHT);
	}

	@Override
	protected Node build() {
		ToolBar tbar = new ToolBar( buildToolBar());
		return tbar;
	}

}
