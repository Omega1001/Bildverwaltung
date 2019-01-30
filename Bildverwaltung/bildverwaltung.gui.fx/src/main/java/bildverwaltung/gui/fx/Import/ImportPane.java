package bildverwaltung.gui.fx.Import;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImportPane {
    private BorderPane bp;
    private Scene sc;
    private ListView<File> lv;
    private Button chooseBt;
    private Button confirmBt;
    private Button cancelBt;
    private Button removeBt;
    private AnchorPane buttonPane;
    private VBox centerBx;
    private Label lb;
    private AnchorPane lbPane;
    private ObservableList ol;
    private ImageView minusImgView;
    private ImageView plusImgView;

    //I18N  constants
    private final static String STAGE_TITLE = "Import";
    private final static String CHOOSE_BUTTON_TXT = "choose Files";
    private final static String CONFIRM_BUTTON_TXT = "confirm";
    private final static String CANCLE_BUTTON_TXT = "cancel";
    private final static String REMOVE_BUTTON_TXT = "remove";
    private final static String LABEL_TXT = "choose or drag files to import";
    private final static String ALL_FILTER_TEXT = "Pictures";
    private final static String ANY_FILTER_TXT = "ANY";

    /**
     * Method to create every Node and subStructures
     */
    private void initializeNodes(){
        //this.plusImgView = new ImageView(new Image("plus.png"));
        //this.minusImgView = new ImageView(new Image("minus.png"));
        this.bp = new BorderPane();
        this.sc = new Scene(bp);
        this.lv = new ListView<>();
        this.chooseBt = new Button(CHOOSE_BUTTON_TXT);
        this.confirmBt = new Button(CONFIRM_BUTTON_TXT);
        this.cancelBt = new Button(CANCLE_BUTTON_TXT);
        this.removeBt = new Button(REMOVE_BUTTON_TXT);
        this.buttonPane = new AnchorPane();
        this.centerBx = new VBox();
        this.lb = new Label(LABEL_TXT);
        this.lbPane = new AnchorPane();
        this.ol = FXCollections.observableArrayList();
    }

    /**
     * Method to set EventHandlers for every Button and for Drag and Drop
     * @param importWindow @NotNull Stage to close in the closeButton-eventHandler
     */
    private void setEventHandlers(Stage importWindow){
        chooseBt.setOnAction((e)->{
            FileChooser fc = getPreparedFileChooser();
            List<File> fileList = fc.showOpenMultipleDialog(importWindow);
            if (fileList != null) {
                ol.addAll(fileList);
            }
        });

        confirmBt.setOnAction((e)->{
            List li = Arrays.asList(ol.toArray());
            //PictureImportServiceImpl pi = Container.getActiveContainer().materialize(PictureImportServiceImpl.class,Scope.Application,null);
            //pi.importAll(li);
            //todo "PictureImportScrviceImpl" durch api ersetzen und kommentare entfernen
        });

        cancelBt.setOnAction((e)->{
            importWindow.close();
        });

        removeBt.setOnAction(e->{
            List<File> files= lv.getSelectionModel().getSelectedItems() ;
            lv.getItems().removeAll(files);//removes duplicates
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
            e.setDropCompleted(true);
            e.consume();
        });
    }

    /**
     * Method to get a prepared FileChooser with HOME as initial directory
     * and Extensionfilters for Pictures
     * @return prepared FileChooser
     */
    private FileChooser getPreparedFileChooser(){
        FileChooser fc= new FileChooser();
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

        FileChooser.ExtensionFilter allFilter = new FileChooser.ExtensionFilter(ALL_FILTER_TEXT,extensions);
        fc.getExtensionFilters().add(allFilter);
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG",JPG_EXT));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG",PNG_EXT));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("GIF",GIF_EXT ));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG",JPEG_EXT ));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("BPM",BPM_EXT ));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(ANY_FILTER_TXT,"*.*"));
        fc.setSelectedExtensionFilter(allFilter);

        fc.setInitialDirectory(new File(System.getProperty("user.home")));

        return fc;
    }

    /**
     * This Method sets values for appearance and behavior
     * @param importWindow @NotNull Stage to set values to
     */
    private void setAppearance(Stage importWindow){
        //buttons
        buttonPane.setPadding(new Insets(4,4,4,4));

        AnchorPane.setBottomAnchor(cancelBt,0.0);
        AnchorPane.setRightAnchor(cancelBt,5.0);
        AnchorPane.setLeftAnchor(cancelBt,5.0);

        AnchorPane.setBottomAnchor(confirmBt,45.0);
        AnchorPane.setRightAnchor(confirmBt,5.0);
        AnchorPane.setLeftAnchor(confirmBt,5.0);

        AnchorPane.setTopAnchor(chooseBt,30.0);
        AnchorPane.setRightAnchor(chooseBt,5.0);
        AnchorPane.setLeftAnchor(chooseBt,5.0);
        //chooseBt.setGraphic(plusImgView);

        AnchorPane.setTopAnchor(removeBt,75.0);
        AnchorPane.setRightAnchor(removeBt,5.0);
        AnchorPane.setLeftAnchor(removeBt,5.0);
        //removeBt.setGraphic(minusImgView);

        //Label
        AnchorPane.setLeftAnchor(lb,5.0);
        lb.setPadding(new Insets(5.0,5.0,5.0,5.0));

        //listView
        VBox.setVgrow(lv,Priority.ALWAYS);
        lv.setPrefWidth(400.0);
        lv.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lv.setMaxHeight(Double.MAX_VALUE);
            //Cell items shows filename instead of Absolute path
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

        //Border pane and Stage
        bp.setPadding(new Insets(5.0,5.0,5.0,5.0));
        importWindow.setTitle(STAGE_TITLE);
        importWindow.setMinHeight(290.0);
        importWindow.setMinWidth(380.0);
    }

    /**
     * Method to put every Element of the import Dialog logical together
     * @param importWindow @NotNull Stage to set The Import Scene to
     */
    private void putNodesTogether(Stage importWindow){
        lbPane.getChildren().add(lb);
        lv.setItems(ol);
        centerBx.getChildren().addAll(lbPane, lv);
        buttonPane.getChildren().addAll(chooseBt, removeBt, confirmBt, cancelBt);
        bp.setCenter(centerBx);
        bp.setRight(buttonPane);
        importWindow.setScene(sc);
    }

    /**
     *  constructor to build and insert the ImportPane
     * @param importWindow @NotNull Stage to put the ImportPane in
     */
    public  ImportPane(Stage importWindow){
        initializeNodes();
        setEventHandlers(importWindow);
        setAppearance(importWindow);
        putNodesTogether(importWindow);
    }
}