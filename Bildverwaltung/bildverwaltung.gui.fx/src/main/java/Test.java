import javafx.application.Application;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Test extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane bp = new BorderPane();
        Button bt = new Button("Import");
        bt.setAlignment(Pos.CENTER);
        bt.addEventHandler(MouseEvent.MOUSE_CLICKED,(e)->{

            new ImportWindow(primaryStage);
        });

        bp.setBottom(bt);

        Scene sc = new Scene(bp);
        bp.setPrefSize(300.0,300.0);
        primaryStage.setScene(sc);
        primaryStage.show();
    }
}
