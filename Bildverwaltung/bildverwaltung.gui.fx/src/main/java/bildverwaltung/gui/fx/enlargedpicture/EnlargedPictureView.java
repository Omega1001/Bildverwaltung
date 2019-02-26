package bildverwaltung.gui.fx.enlargedpicture;

import bildverwaltung.container.Container;
import bildverwaltung.container.Scope;
import bildverwaltung.localisation.Messenger;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class EnlargedPictureView{
	
	private Stage newWindow = new Stage(); 
	private static final double HEIGHT = 800.0;
	private static final double WIDTH = 600.0;
	
	private Messenger msg = Container.getActiveContainer().materialize(Messenger.class, Scope.APPLICATION);
	private ImageView image;
	
	EnlargedPictureToolBar toolbar = new EnlargedPictureToolBar(msg, newWindow, image);
	
	public void enlargePicture(ImageView image) {
		
		image.setFitHeight(HEIGHT-100.0);
		image.setFitWidth(WIDTH);
		
		
		newWindow.setResizable(false);
        
        BorderPane borderPane = new BorderPane();
        borderPane.setBottom(toolbar.getGraphic());
        borderPane.setCenter(image);
        Scene scene = new Scene(borderPane, WIDTH, HEIGHT);
        newWindow.setScene(scene);
        newWindow.show();
	}
}
