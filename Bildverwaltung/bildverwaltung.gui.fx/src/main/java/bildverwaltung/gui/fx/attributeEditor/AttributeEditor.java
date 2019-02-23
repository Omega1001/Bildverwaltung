package bildverwaltung.gui.fx.attributeEditor;

import bildverwaltung.container.Container;
import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.facade.PictureFacade;
import bildverwaltung.localisation.Messenger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.ZoneId;
import java.util.Date;

import static javafx.scene.layout.Priority.ALWAYS;

public class AttributeEditor {
    private BorderPane bp;
    private GridPane grid;
    private Stage stage2;
    private Scene scene2;
    private DatePicker dp;
    private TextField nameFld;
    private Label nameLb;
    private Label dateLb;
    private Label commentLb;
    private TextArea commentField;
    private String title;
    private Stage parentStage;

    private Button confirmBt;
    private Button cancelBt;
    private final Messenger msg;
    private PictureFacade facade;
    private HBox buttonPane;

    private static final Double STAGE_MIN_HEIGH = 400.0;
    private static final Double STAGE_MIN_WIDTH = 600.0;

    public AttributeEditor(Stage parentStage, Picture picture,Messenger msg) {
        this.msg = msg;
        this.parentStage = parentStage;
        facade = Container.getActiveContainer().materialize(PictureFacade.class);

        initializeNodes(picture);
        setEventHandlers(picture);
        setAppearance();
        putNodesTogether();
    }


    private void initializeNodes(Picture picture) {
        this.bp = new BorderPane();
        this.title = msg.translate("editAttributesDialogHeader");
        this.grid = new GridPane();
        this.stage2 = new Stage();
        this.scene2 = new Scene(bp);

        this.nameLb = new Label(msg.translate("editAttributesName"));
        this.nameFld = new TextField(picture.getName());

        this.dateLb = new Label(msg.translate("editAttributesCreationDate"));
        this.dp = new DatePicker(picture.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        this.commentLb = new Label(msg.translate("editAttributesComment"));
        this.commentField = new TextArea(picture.getComment());

        this.confirmBt = new Button(msg.translate("confirmBtnImportConfirm"));
        this.cancelBt = new Button(msg.translate("cancelBtnImportCancel"));

        buttonPane = new HBox();
    }

    private void setEventHandlers(Picture picture) {

        // Cancel Button
        cancelBt.setOnAction(actionEvent -> stage2.close());

        // Confirming Button
        confirmBt.setOnAction(actionEvent -> {
            nameFld.setText(nameFld.getText().trim());
            if (!nameFld.getText().equals("")) {
                picture.setName(nameFld.getText());

                picture.setCreationDate(Date.from(dp.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                picture.setComment(commentField.getText().trim());
                try {
                    facade.save(picture);
                    stage2.close();
                } catch (FacadeException e) {
                    e.printStackTrace();
                }
            } else {
                Alert alr = new Alert(Alert.AlertType.WARNING);
                alr.setTitle(msg.translate("editAttributesAlertTitle"));
                alr.setHeaderText(msg.translate("editAttributesAlertHeaderNameIsEmpty"));
                alr.showAndWait();

            }

        });
    }

    private void setAppearance() {
    	buttonPane.setPadding(new Insets(5,5,5,5));
    	buttonPane.setAlignment(Pos.CENTER_RIGHT);
    	buttonPane.setSpacing(5.0);

        commentLb.setAlignment(Pos.TOP_CENTER);

        ColumnConstraints col1 = new ColumnConstraints(120);
        grid.getColumnConstraints().addAll(col1);
        grid.setGridLinesVisible(false);

        GridPane.setHgrow(commentField, ALWAYS);
        GridPane.setVgrow(commentField, ALWAYS);

        GridPane.setValignment(commentLb, VPos.TOP);

        nameFld.setMaxWidth(Double.MAX_VALUE);
        commentField.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        dp.setMaxWidth(Double.MAX_VALUE);

        stage2.setMinWidth(STAGE_MIN_WIDTH);
        stage2.setMinHeight(STAGE_MIN_HEIGH);
        grid.setHgap(5.0);
        grid.setVgap(5.0);

        bp.setPadding(new Insets(5, 5, 5, 5));
    }

    private void putNodesTogether() {
        //grid.getChildren().addAll(nameLb,txtFld,dateLb,dp,commentLb,txtAr);
        //bp.setTop(title);
        stage2.setTitle(title);
        bp.setCenter(grid);
        bp.setBottom(buttonPane);


        grid.add(nameLb,0,0);
        grid.add(nameFld,1,0);

        grid.add(dateLb,0,1);
        grid.add(dp, 1,1);

        grid.add(commentLb,0,2);
        grid.add(commentField,1,2);

        stage2.setScene(scene2);
        stage2.initOwner(parentStage);
        stage2.initModality(Modality.APPLICATION_MODAL);
        buttonPane.getChildren().addAll(confirmBt,cancelBt);
    }

    public void show(){
        stage2.showAndWait();
    }
}
