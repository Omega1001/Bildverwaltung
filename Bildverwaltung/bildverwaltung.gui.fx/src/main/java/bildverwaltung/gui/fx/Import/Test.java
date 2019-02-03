package bildverwaltung.gui.fx.Import;

import bildverwaltung.container.Container;
import bildverwaltung.container.Scope;
import bildverwaltung.localisation.Messenger;
import javafx.application.Application;
import javafx.stage.Stage;

public class Test extends Application {
    @Override
    public void start(Stage primaryStage){
        new ImportPane(primaryStage, Container.getActiveContainer().materialize(Messenger.class, Scope.APPLICATION));
        primaryStage.show();
    }

    public static void main(String [] args){
        launch(args);
    }
}
