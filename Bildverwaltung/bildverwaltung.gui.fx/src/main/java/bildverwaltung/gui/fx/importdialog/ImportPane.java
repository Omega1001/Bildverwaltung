package bildverwaltung.gui.fx.importdialog;

import bildverwaltung.container.Container;
import bildverwaltung.container.Scope;
import bildverwaltung.dao.entity.Picture;
import bildverwaltung.facade.PictureImportFacade;
import bildverwaltung.localisation.Messenger;
import bildverwaltung.utils.ApplicationIni;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImportPane{
    private final Messenger msg;
    private Stage importWindow;
    private Stage parentStage;
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

    private List<Picture> pictures;

    /**
     *  constructor to build and insert the ImportPane
     * @param parentStage @NotNull ParentStage
     * @param msg @NotNull Messenger to take resourceStrings from
     */
    public  ImportPane(Stage parentStage, Messenger msg){
        this.msg = msg;
        this.parentStage = parentStage;

        this.pictures = new ArrayList<>();

        initializeNodes();
        setEventHandlers();
        setAppearance();
        putNodesTogether();
    }

    public List<Picture> show() {
        if (ApplicationIni.get()
                .get("directory","picturesDirectory") == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(msg.translate("titleImportAlertError"));
            alert.setHeaderText(msg.translate("headerImportAlertErrorInImport"));
            alert.setContentText(msg.translate("appIniDirectoryPathMissing"));
            alert.showAndWait();
        } else {
            importWindow.showAndWait();
        }
        return pictures;
    }


    /**
     * Method to create every Node and subStructures
     */
    private void initializeNodes(){
        this.importWindow = new Stage();
        this.plusImgView = new ImageView(new Image("plus2.png"));
        this.minusImgView = new ImageView(new Image("minus2.png"));
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
            List<File> fileList = fc.showOpenMultipleDialog(importWindow); //unmodifiable
            if (fileList != null) {
                List<File> modifiableList = new ArrayList<>(fileList); //modifiable
                ol.addAll(removeDuplicates(modifiableList));
            }
        });

        confirmBt.setOnAction((e)->{
            List<File> li = ol;
            PictureImportFacade pi = Container.getActiveContainer().materialize(PictureImportFacade.class, Scope.APPLICATION,null);
             pictures.addAll(pi.importAll(li));

             if(pictures.size() != li.size()) {

                 List<String> successfullyImported = new ArrayList<>();
                 for(Picture pic:pictures) {
                     successfullyImported.add(pic.getName() + pic.getExtension());
                 }

                 //List<File> failedImport = new ArrayList<>();
                 StringBuilder failedText = new StringBuilder(msg.translate("infoTextImportAlertFilesCouldNotGetImported") + "\n");
                 for(File file:li) {
                    if(!successfullyImported.contains(file.getName())) {
                        failedText.append("\n " + file.getName());
                    }
                 }

                 Alert alert = new Alert(Alert.AlertType.ERROR);
                 alert.setTitle(msg.translate("titleImportAlertError"));
                 alert.setHeaderText(msg.translate("headerImportAlertErrorInImport"));
                 alert.setContentText(failedText.toString());
                 alert.showAndWait();
             }
             importWindow.close();
        });

        cancelBt.setOnAction((e)-> importWindow.close());

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
        //AnchorPane.setRightAnchor(chooseBt,5.0);
        AnchorPane.setLeftAnchor(chooseBt,5.0);
        //plusImgView.setFitHeight(50d);
        //plusImgView.setFitWidth(50d);
        //plusImgView.setPreserveRatio(true);
        chooseBt.setGraphic(plusImgView);

        AnchorPane.setTopAnchor(removeBt,100.0);
        //AnchorPane.setRightAnchor(removeBt,5.0);
        AnchorPane.setLeftAnchor(removeBt,5.0);
        //minusImgView.setFitHeight(50d);
        //minusImgView.setFitWidth(50d);
        //minusImgView.setPreserveRatio(true);
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
        importWindow.initModality(Modality.APPLICATION_MODAL);
        importWindow.setX(parentStage.getX()+50.0);
        importWindow.setY(parentStage.getY()+50.0);
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
        importWindow.initOwner(parentStage);
    }
}