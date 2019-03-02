package gui.fx.importdialog;

import bildverwaltung.container.Container;
import bildverwaltung.container.Scope;
import bildverwaltung.gui.fx.importdialog.ImportPane;
import bildverwaltung.localisation.Messenger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Test extends Application {
    @Override
    public void start(Stage primaryStage){
        Button bt = new Button("Import");
        bt.setOnAction(e->{

            ImportPane ip = new ImportPane(primaryStage, Container.getActiveContainer().materialize(Messenger.class,Scope.APPLICATION));
            ip.show();
        });



        BorderPane bp = new BorderPane();
        bp.setCenter(bt);
        Scene sc = new Scene(bp);
        bp.setPrefSize(200.0,200.0);
        primaryStage.setScene(sc);
        primaryStage.show();
    }

    public static void main(String [] args){
        launch(args);
    }
}
