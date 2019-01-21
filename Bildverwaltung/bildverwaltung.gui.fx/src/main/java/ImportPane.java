import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.Arrays;
import java.util.List;


public abstract class ImportPane {

    /**
     *  Methode zum aufbauen der importPane
     *
     * @param importWindow die stage ,die die ImportPane enthalten soll
     * @return fertig aufgebaute ImportPane
     */
    public static Pane getImportPane(Stage importWindow){
        //Nodes
        Pane pn = new Pane();
        BorderPane bp = new BorderPane();

        ListView lv = new ListView();
        Button chooseBt = new Button("choose Files");
        Button confirmBt = new Button("confirm");
        Button cancelBt = new Button("cancel");
        HBox buttonBx = new HBox();

        //putting together
        buttonBx.setSpacing(5.0);
        buttonBx.setPadding(new Insets(4,4,4,4));

        buttonBx.getChildren().addAll(chooseBt,confirmBt,cancelBt);
        bp.setCenter(lv);
        bp.setBottom(buttonBx);
        pn.getChildren().add(bp);

        //button Eventhandler
        chooseBt.addEventHandler(MouseEvent.MOUSE_CLICKED,(e)->{
            FileChooser fc= new FileChooser();
            List<File> fileList = fc.showOpenMultipleDialog(importWindow);
            ObservableList ol = FXCollections.observableArrayList();
            ol.addAll(fileList);
            lv.setItems(ol);
            e.consume();

        });

        confirmBt.addEventHandler(MouseEvent.MOUSE_CLICKED,(e)->{
            List li = Arrays.asList(lv.getItems().toArray());
            //PictureImportServiceImpl pi = Container.getActiveContainer().materialize(PictureImportServiceImpl.class,Scope.Application,null);
            //pi.importAll(li);
            //todo "PictureImportScrviceImpl" durch api ersetzen und kommentare entfernen
            e.consume();
        });

        cancelBt.addEventHandler(MouseEvent.MOUSE_CLICKED,(e)->{
            importWindow.close();
            e.consume();
        });

        //Drag and Drop
        bp.setOnDragOver((e)->{
            if (e.getDragboard().hasFiles()) {
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

        return pn;
    }
}
