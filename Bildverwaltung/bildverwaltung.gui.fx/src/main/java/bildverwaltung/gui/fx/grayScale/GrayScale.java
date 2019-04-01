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
    private ToolBar toolBar;
    private Button saveButton;
    private Button cancelButton;            
	
  public GrayScale(Stage parent, Picture picture) {
  	this.parent  = parent;
    this.picture = picture;


      initialize();
      addHandlers();
     // setAppearance();
     // putTogether();
  }
  
  private void initialize(){
      primaryStage = new Stage();
      borderPane   = new BorderPane();
      scene        = new Scene(borderPane);
      toolBar      = new ToolBar();
      saveButton   = new Button(msg.translate("grayScaleSave"));
      cancelButton = new Button(msg.translate("buttonCancel"));
      BufferedImage img = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
  }
          
	public static void main(String[] args)throws IOException{
		BufferedImage img = null;
		File f = null; 
		
		//read image
		try{
			f = new File(".png");
			img = ImageIO.read(f);
			
		}catch(IOException e){
			System.out.println(e);
		}
		
		//define height and width of the image
		int width  = img.getWidth();
		int height = img.getHeight();

		//from rgb into graysacale
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				int p = img.getRGB(x,y);
				
				int a = (p>>24) & 0xff;
				int r = (p>>16) & 0xff;
				int g = (p>>8)  & 0xff;
				int b = p       & 0xff;
				
				//calculate
				int avg = (r + g + b)/3;
				
				//put RGB for avg
				p = (a<<24) | (avg << 16) | (avg << 8) | avg;
				
				img.setRGB(x, y, p);
			}
		}
		
		//write image
		try{
			f = new File("blablabla.png");
			ImageIO.write(img,"jpg", f);
			
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

      
//      private void putTogether(){
//          borderPane.setTop(toolBar);
//          toolBar.getItems().addAll(size,saveButton, cancelButton);
//          primaryStage.setScene(scene);
//          primaryStage.initOwner(parent);
//          primaryStage.initModality(Modality.APPLICATION_MODAL);
//      }

//      public void show(){
//          primaryStage.showAndWait();
//      }
  }
}
