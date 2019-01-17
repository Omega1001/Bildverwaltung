import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class ImportWindow extends Stage {
    ImportWindow(Stage parentStage){
        BorderPane bp = new BorderPane();
        Scene sc = new Scene(bp);
        ListView lv= new ListView();
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
        Button cancelBt = new Button("cancel");
        cancelBt.addEventHandler(MouseEvent.MOUSE_CLICKED,(e)->{
            this.close();
            e.consume();
        });

        bp.setOnDragDropped((e)->{
            Dragboard db = e.getDragboard();
            List<File> fileList = db.getFiles();
            ObservableList ol = FXCollections.observableArrayList();
            ol.addAll(fileList);
            lv.setItems(ol);
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
        this.showAndWait();
    }
}
