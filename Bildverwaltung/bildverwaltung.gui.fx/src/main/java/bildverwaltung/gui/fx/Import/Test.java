package bildverwaltung.gui.fx.Import;

import javafx.application.Application;
import javafx.stage.Stage;

public class Test extends Application {
    @Override
    public void start(Stage primaryStage){
        new ImportPane(primaryStage,null);
        primaryStage.show();
    }

    public static void main(String [] args){
        launch(args);
    }
}
