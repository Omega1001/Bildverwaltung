import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.util.Arrays;
import java.util.List;


public class ImportWindow extends Stage {
    private ListView lv = new ListView();

    ImportWindow(Stage parentStage){
        BorderPane bp = new BorderPane();
        Scene sc = new Scene(bp);
        Button chooseBt = new Button("choose Files");
        chooseBt.addEventHandler(MouseEvent.MOUSE_CLICKED,(e)->{
            FileChooser fc= new FileChooser();
            List<File> fileList = fc.showOpenMultipleDialog(this);
            ObservableList ol = FXCollections.observableArrayList();
            ol.addAll(fileList);
            lv.setItems(ol);
            e.consume();
        });
        Button confirmBt = new Button("confirm");
        confirmBt.addEventHandler(MouseEvent.MOUSE_CLICKED,(e)->{
            ListView lv= getListview();
            List li = Arrays.asList(lv.getItems().toArray());


            e.consume();
        });
        Button cancelBt = new Button("cancel");
        cancelBt.addEventHandler(MouseEvent.MOUSE_CLICKED,(e)->{
            this.close();
            e.consume();
        });

        bp.setOnDragOver((e)->{
            if (e.getDragboard().hasFiles()) {
                /* allow for both copying and moving, whatever user chooses */
                e.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            e.consume();
        });

        bp.setOnDragDropped((e)->{
            Dragboard db = e.getDragboard();
            List<File> fileList = db.getFiles();
            ObservableList ol = FXCollections.observableArrayList();
            ol.addAll(fileList);
            lv.setItems(ol);
            e.setDropCompleted(true);
            e.consume();
        });
        HBox buttonBx = new HBox();
        buttonBx.setSpacing(5.0);
        buttonBx.setPadding(new Insets(4,4,4,4));


        bp.setCenter(lv);


        buttonBx.getChildren().addAll(chooseBt,confirmBt,cancelBt);
        bp.setBottom(buttonBx);
        this.setScene(sc);
        this.initOwner(parentStage);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setX(parentStage.getX()+20.0);
        this.setY(parentStage.getY()+20.0);
        this.showAndWait();
    }

    private ListView getListview(){
        return lv;
    }
}
