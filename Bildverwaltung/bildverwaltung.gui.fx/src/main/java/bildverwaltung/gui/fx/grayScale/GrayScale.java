package bildverwaltung.gui.fx.grayScale;

import bildverwaltung.container.Container;
import bildverwaltung.container.Scope;
import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.facade.PictureFacade;
import bildverwaltung.localisation.Messenger;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class GrayScale{
	private PictureFacade pictureFacade = Container.getActiveContainer().materialize(PictureFacade.class, Scope.APPLICATION);
    private double mouseX;
    private double mouseY;
    private boolean mousePressed;
    private Stage parent;
    private Picture picture;
    private Messenger msg;
    private Stage primaryStage;
    private Scene scene;
    private BorderPane borderPane;
    private Label size;
    private ToolBar toolBar;
    private Button saveButton;
    private Button cancelButton;     
	private Group centerGroup;
    
  public GrayScale(Stage parent, Picture picture, Messenger msg) {
  	this.parent  = parent;
    this.picture = picture;
    this.msg     = msg;


      initialize();
      addHandlers();
      pictureGrayScale();
      putTogether();
  }
  
  private void initialize(){
      primaryStage = new Stage();
      borderPane   = new BorderPane();
      scene        = new Scene(borderPane);
      toolBar      = new ToolBar();
      size         = new Label(msg.translate("grayScaleSizeLabel"));
      saveButton   = new Button(msg.translate("grayScaleSave"));
      cancelButton = new Button(msg.translate("buttonCancel"));
      centerGroup  = new Group;
      BufferedImage img = new BufferedImage(picture.getWidth(), picture.getHeigth(), BufferedImage.TYPE_INT_ARGB);
  }
          
  		private void pictureGrayScale() {
        BufferedImage picture = null;
		Image img = null; 
		
		//read image
		try(InputStream is = pictureFacade.resolvePictureURI(pictureUri())){
			img = new Image(is);
			//picture = ImageIO.read(img);
			
		}catch(IOException e){
			e.printStackTrace();
		}
		
		//define height and width of the image
		int width  = picture.getWidth();
		int height = picture.getHeight();

		//from rgb into graysacale
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				int p = picture.getRGB(x,y);
				
				int a = (p>>24) & 0xff;
				int r = (p>>16) & 0xff;
				int g = (p>>8)  & 0xff;
				int b = p       & 0xff;
				
				//calculate
				int avg = (r + g + b)/3;
				
				//put RGB for avg
				p = (a<<24) | (avg << 16) | (avg << 8) | avg;
				
				picture.setRGB(x, y, p);
			}
		}
		
		//write image
		try(InputSream is = pictureFacade.resolvePictureURI(pictureUri())){
		    img = new Image(is);
			//ImageIO.write(picture,"jpg", img);
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private void addHandlers(){
      cancelButton.setOnAction(event -> {
         primaryStage.close();
      });

     // saveButton.setOnAction(event -> {
      //    grayScale();
      //});
      
      centerGroup.addEventFilter(MouseEvent.ANY,event -> {
          mouseX = event.getX();
          mouseY = event.getY();
          mousePressed = event.isPrimaryButtonDown();
      });
	}

      
      private void putTogether(){
    	  borderPane.setCenter(centerGroup);
    	  centerGroup.getChildren().addAll(grayScale);
          borderPane.setTop(toolBar);
          toolBar.getItems().addAll(size,saveButton, cancelButton);
          primaryStage.setScene(scene);
          primaryStage.initOwner(parent);
          primaryStage.initModality(Modality.APPLICATION_MODAL);
      }

      public void show(){
          primaryStage.showAndWait();
      }
  }
}
