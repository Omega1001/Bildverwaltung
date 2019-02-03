package bildverwaltung.gui.fx.Import;

import bildverwaltung.container.Container;
import bildverwaltung.container.Scope;
import bildverwaltung.facade.PictureImportFacade;
import bildverwaltung.localisation.Messenger;
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
import java.util.Iterator;
import java.util.List;
import bildverwaltung.service.*;

public class ImportPane{
    private final Messenger msg;
    private Stage importWindow;
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
    private ObservableList<File> ol;
    private ImageView minusImgView;
    private ImageView plusImgView;

    /**
     *  constructor to build and insert the ImportPane
     * @param importWindow @NotNull Stage to put the ImportPane in
     * @param msg @NotNull Messenger to take resourceStrings from
     */
    public  ImportPane(Stage importWindow, Messenger msg){
        this.importWindow = importWindow;
        this.msg = msg;

        initializeNodes();
        setEventHandlers();
        setAppearance();
        putNodesTogether();
    }

    /**
     * Method to create every Node and subStructures
     */
    private void initializeNodes(){
        this.plusImgView = new ImageView(new Image("plus.png"));
        this.minusImgView = new ImageView(new Image("minus.png"));
        this.bp = new BorderPane();
        this.sc = new Scene(bp);
        this.lv = new ListView<>();
        this.chooseBt = new Button();
        this.confirmBt = new Button(msg.translate("confirmBtnImportConfirm"));
        this.cancelBt = new Button(msg.translate("cancelBtnImportCancel"));
        this.removeBt = new Button();
        this.buttonPane = new AnchorPane();
        this.centerBx = new VBox();
        this.lb = new Label(msg.translate("headerTextImportChooseOrDrag"));
        this.lbPane = new AnchorPane();
        this.ol = FXCollections.observableArrayList();
    }

    /**
     * Method to set EventHandlers for every Button and for Drag and Drop
     */
    private void setEventHandlers(){
        chooseBt.setOnAction((e)->{
            FileChooser fc = getPreparedFileChooser();
            List<File> fileList = fc.showOpenMultipleDialog(importWindow);
            if (fileList != null) {
                ol.addAll(removeDuplicates(fileList));
            }
        });

        confirmBt.setOnAction((e)->{
            List li = Arrays.asList(ol.toArray());
            PictureImportFacade pi = Container.getActiveContainer().materialize(PictureImportFacade.class, Scope.APPLICATION,null);
            pi.importAll(li);
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
            List<File> fileList = removeDuplicates(db.getFiles());
            ol.addAll(fileList);
            e.setDropCompleted(true);
            e.consume();
        });
    }

    /**
     * Method to check for duplicates in the inputFiles and the ObservableList
     * duplicates were compared by FileName
     *
     * @param inputFiles List which gets checked for duplicates in the ObservableList
     * @return List without any duplicates in the ObservableList
     *      or the unchanged inputList if there are no duplicates
     */
    private List<File> removeDuplicates(List<File> inputFiles){
        Iterator<File> fi = inputFiles.iterator();
        while(fi.hasNext()){
            File f = fi.next();
            for(File insertedFile: ol){
                if(f.getAbsolutePath().equals(insertedFile.getAbsolutePath())){
                    fi.remove();
                }
            }
        }

        return inputFiles;
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

        FileChooser.ExtensionFilter allFilter = new FileChooser.ExtensionFilter
                (msg.translate("descriptorImportFileChooserExtensionFilterPictures"),extensions);
        fc.getExtensionFilters().add(allFilter);
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG",JPG_EXT));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG",PNG_EXT));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("GIF",GIF_EXT ));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG",JPEG_EXT ));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("BPM",BPM_EXT ));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(msg.translate("descriptorImportFileChooserExtensionFilterAny"),"*.*"));
        fc.setSelectedExtensionFilter(allFilter);

        fc.setInitialDirectory(new File(System.getProperty("user.home")));

        return fc;
    }

    /**
     * This Method sets values for appearance and behavior
     */
    private void setAppearance(){
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
        chooseBt.setGraphic(plusImgView);

        AnchorPane.setTopAnchor(removeBt,75.0);
        AnchorPane.setRightAnchor(removeBt,5.0);
        AnchorPane.setLeftAnchor(removeBt,5.0);
        removeBt.setGraphic(minusImgView);

        //Button Tooltips
        chooseBt.setTooltip(new Tooltip(msg.translate("tooltipImportChooseButtonChooseFiles")));
        removeBt.setTooltip(new Tooltip(msg.translate("tooltipImportRemoveButtonRemoveFiles")));
        confirmBt.setTooltip(new Tooltip(msg.translate("tooltipImportConfirmButtonImportChosenFiles")));
        cancelBt.setTooltip(new Tooltip(msg.translate("tooltipImportCancelButtonCloseWindow")));

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
        importWindow.setTitle(msg.translate("stageTitleImport"));
        importWindow.setMinHeight(290.0);
        importWindow.setMinWidth(380.0);
    }

    /**
     * Method to put every Element of the import Dialog logical together
     */
    private void putNodesTogether(){
        lbPane.getChildren().add(lb);
        lv.setItems(ol);
        centerBx.getChildren().addAll(lbPane, lv);
        buttonPane.getChildren().addAll(chooseBt, removeBt, confirmBt, cancelBt);
        bp.setCenter(centerBx);
        bp.setRight(buttonPane);
        importWindow.setScene(sc);
    }
}