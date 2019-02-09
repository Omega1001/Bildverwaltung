package bildverwaltung.gui.fx.masterview.dialogs;

import bildverwaltung.container.Container;
import bildverwaltung.container.Scope;
import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.facade.PictureFacade;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class PictureSelectionDialog {
    private ListView<Picture> listView;
    private ObservableList<Picture> observableList;
    private Stage selectionDialog;
    private Stage parent;
    private BorderPane bp;
    private VBox vbx;
    private Button selectBt;
    private Button cancelBt;
    private Scene sc;
    private List<Picture> output;

    public PictureSelectionDialog(Stage parent){
        initializeNodes(parent);
        setEventhandlers();
        setAppearance();
        putTogether();
    }

    private void initializeNodes(Stage parent){
        this.parent = parent;
        this.observableList= FXCollections.observableArrayList();
        this.listView = new ListView<>(observableList);
        this.bp = new BorderPane();
        this.vbx = new VBox();
        this.selectionDialog = new Stage();
        this.cancelBt= new Button("cancel");
        this.selectBt = new Button("confirm");
        this.sc = new Scene(bp);
        this.output = new ArrayList<>();

        PictureFacade pifc=  Container.getActiveContainer().materialize(PictureFacade.class, Scope.APPLICATION);
        try {
            observableList.addAll(pifc.getAllPictures());
        }catch(FacadeException e){

        }
    }

    private void setAppearance(){
        bp.setPadding(new Insets(5.0));
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        selectionDialog.initOwner(parent);
        selectionDialog.initModality(Modality.APPLICATION_MODAL);
        vbx.setPadding(new Insets(5.0));
        vbx.setSpacing(5.0);

    }

    private void setEventhandlers(){
        cancelBt.setOnAction(e->{
            selectionDialog.close();
        });

        selectBt.setOnAction(e->{
            output.addAll( listView.getSelectionModel().getSelectedItems());
            selectionDialog.close();
        });
    }

    private void putTogether(){
        selectionDialog.setScene(sc);
        bp.setCenter(listView);
        bp.setRight(vbx);
        vbx.getChildren().addAll(cancelBt,selectBt);
    }

    public List<Picture> select(){
        selectionDialog.show();
        return output;
    }
}
