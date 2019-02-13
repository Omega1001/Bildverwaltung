package bildverwaltung.gui.fx.attributeEditor;

import bildverwaltung.container.Container;
import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.facade.PictureFacade;
import bildverwaltung.localisation.Messenger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.ZoneId;
import java.util.Date;

public class AttributeEditor {
    private BorderPane bp;
    private GridPane grid;
    private Stage st2;
    private Scene sc2;
    private DatePicker dp;
    private TextField nameFld;
    private Label nameLb;
    private Label dateLb;
    private Label commentLb;
    private TextArea commentField;
    private Label title;
    private Stage parentStage;

    private Button confirmBt;
    private Button cancelBt;
    private final Messenger msg;
    private PictureFacade facade;
    private AnchorPane buttonPane;

    public AttributeEditor(Stage parentStage, Picture picture,Messenger msg){
        this.parentStage = parentStage;
        this.msg = msg;

        facade = Container.getActiveContainer().materialize(PictureFacade.class);

        initializeNodes(picture);
        setEventHandlers(picture);
        setAppearance();
        putNodesTogether();
    }


    private void initializeNodes(Picture picture) {
        this.bp = new BorderPane();
        this.title = new Label(msg.translate("editAttributesDialogHeader"));
        this.grid = new GridPane();
        this.st2 = new Stage();
        this.sc2 = new Scene(bp);

        this.nameLb = new Label(msg.translate("editAttributesName"));
        this.nameFld = new TextField(picture.getName());

        this.dateLb = new Label(msg.translate("editAttributesCreationDate"));
        this.dp = new DatePicker(picture.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        this.commentLb = new Label(msg.translate("editAttributesComment"));
        this.commentField = new TextArea(picture.getComment());

        this.confirmBt = new Button(msg.translate("confirmBtnImportConfirm"));
        this.cancelBt = new Button(msg.translate("cancelBtnImportCancel"));

        buttonPane = new AnchorPane();
    }

    private void setEventHandlers(Picture picture) {
        cancelBt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                st2.close();
            }
        });

        confirmBt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                nameFld.setText(nameFld.getText().trim());
                if (!nameFld.getText().equals("")) {
                    picture.setName(nameFld.getText());

                    picture.setCreationDate(Date.from(dp.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    picture.setComment(commentField.getText().trim());
                    try {
                        facade.save(picture);
                        st2.close();
                    } catch (FacadeException e) {
                        e.printStackTrace();
                    }
                } else {
                    Alert alr = new Alert(Alert.AlertType.WARNING);
                    alr.setTitle(msg.translate("editAttributesAlertTitle"));
                    alr.setHeaderText(msg.translate("editAttributesAlertHeaderNameIsEmpty"));
                    alr.showAndWait();

                }

            }
        });
    }

    private void setAppearance() {
    	buttonPane.setPadding(new Insets(4,4,4,4));

        commentLb.setAlignment(Pos.TOP_CENTER);

        grid.setGridLinesVisible(false);
        ColumnConstraints col1 = new ColumnConstraints(80);
        grid.getColumnConstraints().addAll(col1);

        grid.setHgrow(commentField,Priority.ALWAYS);
        grid.setVgrow(commentField,Priority.ALWAYS);

        GridPane.setValignment(commentLb, VPos.TOP);

        nameFld.setMaxWidth(Double.MAX_VALUE);
        commentField.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        dp.setMaxWidth(Double.MAX_VALUE);

        AnchorPane.setBottomAnchor(cancelBt,0.0);
        AnchorPane.setRightAnchor(cancelBt,5.0);
        AnchorPane.setLeftAnchor(cancelBt,5.0);

        AnchorPane.setBottomAnchor(confirmBt,45.0);
        AnchorPane.setRightAnchor(confirmBt,5.0);
        AnchorPane.setLeftAnchor(confirmBt,5.0);

        st2.setMaxWidth(900.0);
        st2.setMaxHeight(600.0);
        st2.setMinWidth(500.0);
        st2.setMinHeight(400.0);
        grid.setHgap(5.0);
        grid.setVgap(5.0);
    }

    private void putNodesTogether() {
        //grid.getChildren().addAll(nameLb,txtFld,dateLb,dp,commentLb,txtAr);
        bp.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        bp.setTop(title);
        bp.setCenter(grid);
        bp.setBottom(buttonPane);


        grid.add(nameLb,0,0);
        grid.add(nameFld,1,0);

        grid.add(dateLb,0,1);
        grid.add(dp, 1,1);

        grid.add(commentLb,0,2);
        grid.add(commentField,1,2);

        st2.setScene(sc2);
        st2.initOwner(parentStage);
        st2.initModality(Modality.APPLICATION_MODAL);
        buttonPane.getChildren().addAll(confirmBt,cancelBt);
    }

    public void show(){
        st2.show();
    }
}
