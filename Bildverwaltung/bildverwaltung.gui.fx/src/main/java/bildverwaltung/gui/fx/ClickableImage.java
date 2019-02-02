package bildverwaltung.gui.fx;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ClickableImage extends Application {

    private ArrayList<String> clickedImages = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {     
        String imgUrl = getClass().getResource("https://bit.ly/1GvKLBX").toExternalForm();
        assert imgUrl != null;
        Image img = new Image(imgUrl);
        assert img != null;

        BorderPane root = new BorderPane();
        ImageView imgView = new ImageView(img);
        imgView.setUserData(imgUrl);
        root.setCenter(imgView);

        Scene scene = new Scene(root, 600, 400);

        primaryStage.setScene(scene);
        primaryStage.setTitle("ClickableImage");
        primaryStage.show();

        //-------------

        imgView.setOnMouseClicked(e -> {
            String clickedImgUrl = (String)((ImageView)e.getSource()).getUserData();
            System.out.println("Image was clicked: " + clickedImgUrl);
            clickedImages.add(clickedImgUrl);
        });                
    }
}