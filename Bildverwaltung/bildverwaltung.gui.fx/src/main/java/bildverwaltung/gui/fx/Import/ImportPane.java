package bildverwaltung.gui.fx.Import;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ImportPane {

    /**
     *  Methode zum aufbauen und einfügen der importPane
     *
     * @param importWindow die stage ,die die ImportPane enthalten soll
     */
    public static void putImportPaneTo(Stage importWindow){
        //Nodes
        BorderPane bp = new BorderPane();
        Scene sc = new Scene(bp);
        ListView lv = new ListView();
        Button chooseBt = new Button("choose Files");
        Button confirmBt = new Button("confirm");
        Button cancelBt = new Button("cancel");
        Button removeBt = new Button("remove");
        AnchorPane buttonBxRight = new AnchorPane();
        VBox centerBx = new VBox();
        Label lb = new Label("choose or drag files to import");
        AnchorPane lbPane = new AnchorPane();

        //putting together
        lv.setCellFactory(l -> new ListCell<File>() {
            @Override
            public void updateItem(File item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });


        chooseBt.setAlignment(Pos.CENTER);
        removeBt.setAlignment(Pos.CENTER);
        confirmBt.setAlignment(Pos.CENTER);
        cancelBt.setAlignment(Pos.CENTER);
        chooseBt.setAlignment(Pos.CENTER);
        buttonBxRight.getChildren().addAll(chooseBt,removeBt,confirmBt,cancelBt);
        buttonBxRight.setPadding(new Insets(4,4,4,4));
        buttonBxRight.setBottomAnchor(cancelBt,0.0);
        buttonBxRight.setRightAnchor(cancelBt,5.0);
        buttonBxRight.setLeftAnchor(cancelBt,5.0);
        buttonBxRight.setBottomAnchor(confirmBt,45.0);
        buttonBxRight.setRightAnchor(confirmBt,5.0);
        buttonBxRight.setLeftAnchor(confirmBt,5.0);

        buttonBxRight.setTopAnchor(chooseBt,30.0);
        buttonBxRight.setRightAnchor(chooseBt,5.0);
        buttonBxRight.setLeftAnchor(chooseBt,5.0);
        buttonBxRight.setTopAnchor(removeBt,75.0);
        buttonBxRight.setRightAnchor(removeBt,5.0);
        buttonBxRight.setLeftAnchor(removeBt,5.0);

        bp.setCenter(centerBx);
        bp.setRight(buttonBxRight);
        bp.setPadding(new Insets(5.0,5.0,5.0,5.0));
        importWindow.setScene(sc);
        importWindow.setTitle("Import");

        lb.setPadding(new Insets(5.0,5.0,5.0,5.0));
        lv.setPrefWidth(400.0);
        lbPane.getChildren().add(lb);
        lbPane.setLeftAnchor(lb,5.0);
        centerBx.getChildren().addAll(lbPane,lv);
        VBox.setVgrow(lv,Priority.ALWAYS);
        lv.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lv.setMaxHeight(Double.MAX_VALUE);
        importWindow.setMinHeight(290.0);
        importWindow.setMinWidth(380.0);

        ObservableList ol = FXCollections.observableArrayList();
        //button Eventhandler
        chooseBt.addEventHandler(MouseEvent.MOUSE_CLICKED,(e)->{
            FileChooser fc= new FileChooser();
            fc.setInitialDirectory(new File(System.getProperty("user.home")));
            ArrayList<String> extensions = new ArrayList<>();

            final String JPG_EXT ="*.jpg";
            final String PNG_EXT ="*.png";
            final String GIF_EXT ="*.gif";
            final String JPEG_EXT ="*.jpeg";
            final String BPM_EXT ="*.bmp";
            extensions.add(JPG_EXT);
            extensions.add(PNG_EXT);
            extensions.add(GIF_EXT);
            extensions.add(JPEG_EXT);
            extensions.add(BPM_EXT);

            FileChooser.ExtensionFilter allFilter = new FileChooser.ExtensionFilter("All Pictures",extensions);
            fc.getExtensionFilters().add(allFilter);
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG",JPG_EXT));
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG",PNG_EXT));
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("GIF",GIF_EXT ));
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG",JPEG_EXT ));
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("BPM",BPM_EXT ));
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("ANY","*.*"));
            fc.setSelectedExtensionFilter(allFilter);

            List<File> fileList = fc.showOpenMultipleDialog(importWindow);
            if (fileList != null) {
                ol.addAll(fileList);
                lv.setItems(ol);
            }
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

        removeBt.addEventHandler(MouseEvent.MOUSE_CLICKED,e->{
            Object file= lv.getSelectionModel().getSelectedItem();
            if(file!=null){
                lv.getItems().remove(file);
            }
            e.consume();
        });

        //Drag and Drop
        lv.setOnDragOver((e)->{
            if (e.getDragboard().hasFiles()) {
                e.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            e.consume();
        });

        lv.setOnDragDropped((e)->{
            Dragboard db = e.getDragboard();
            List<File> fileList = db.getFiles();
            ol.addAll(fileList);
            lv.setItems(ol);
            e.setDropCompleted(true);
            e.consume();
        });
    }
}