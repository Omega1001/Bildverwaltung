package bildverwaltung.gui.fx.masterview;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class EnlargedPictureView {
	private static final double HEIGHT = 800.0;
	private static final double WIDTH = 600.0;
	
	
	private static final Logger LOG = LoggerFactory.getLogger(EnlargedPictureView.class);
	
	
	private static EnlargedPictureToolBar toolbar;
	

	
	public static void enlargePicture(ImageView iView) {
		
		iView.setFitHeight(HEIGHT-100.0);
		iView.setFitWidth(WIDTH);
		Stage newWindow = new Stage();
		newWindow.setResizable(false);
        
        BorderPane borderPane = new BorderPane();
        borderPane.setBottom(EnlargedPictureToolBar.buildToolBar());
        borderPane.setCenter(iView);
        Scene scene = new Scene(borderPane, WIDTH, HEIGHT);
        newWindow.setScene(scene);
	 	newWindow.show();
	}

}
