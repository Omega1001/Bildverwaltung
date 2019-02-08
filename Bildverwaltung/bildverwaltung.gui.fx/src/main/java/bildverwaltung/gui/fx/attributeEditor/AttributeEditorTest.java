package bildverwaltung.gui.fx.attributeEditor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AttributeEditorTest extends Application {

    public void start(Stage primaryStage){
        Button bt = new Button("test");
        BorderPane bp = new BorderPane();
        bp.setCenter(bt);
        Scene sc = new Scene(bp);
                primaryStage.setScene(sc);

        bt.setOnAction(e->{
            AttributeEditor st2 = new AttributeEditor(primaryStage);
            st2.show();
        });

        primaryStage.show();
    }


    public static void main(String [] args){
        launch(args);
    }
}
