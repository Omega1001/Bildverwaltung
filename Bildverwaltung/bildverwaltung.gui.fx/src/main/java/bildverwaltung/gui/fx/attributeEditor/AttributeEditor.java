package bildverwaltung.gui.fx.attributeEditor;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AttributeEditor {
    private BorderPane bp;
    private TilePane grid;
    private Stage st2;
    private Scene sc2;
    private DatePicker dp;
    private TextArea txtAr;
    private TextField txtFld;
    private Label nameLb;
    private Label dateLb;
    private Label commentLb;
    private Label title;


    public AttributeEditor(Stage parent){
        this.bp = new BorderPane();
        this.grid = new TilePane();
        this.st2 = new Stage();
        this.sc2 = new Scene(bp);
        this.dp = new DatePicker();
        this.commentLb = new Label("Comment: ");
        this.nameLb = new Label("Name: ");
        this.dateLb = new Label("Date: ");
        this.txtAr = new TextArea();
        this.txtFld = new TextField();
        this.title = new Label("header");

        grid.getChildren().addAll(nameLb,txtFld,dateLb,dp,commentLb,txtAr);
        grid.setPrefColumns(2);
        grid.setPrefRows(3);
        grid.setPrefTileWidth(200.0);
        grid.setPrefTileHeight(50.0);

        bp.setPadding(new Insets(5.0,5.0,5.0,5.0));
        dp.setMaxWidth(200.0);
        bp.setTop(title);
        bp.setCenter(grid);
        st2.setScene(sc2);
        st2.initOwner(parent);
        st2.initModality(Modality.APPLICATION_MODAL);
    }


    public void show(){
        st2.show();
    }
}
