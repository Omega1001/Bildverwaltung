package bildverwaltung.gui.fx.drawOnPicture;

import bildverwaltung.container.Container;
import bildverwaltung.container.Scope;
import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.facade.PictureFacade;
import bildverwaltung.localisation.Messenger;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class DrawOnPicture {
    private PictureFacade pictureFacade = Container.getActiveContainer().materialize(PictureFacade.class, Scope.APPLICATION);
    private double mouseX;
    private double mouseY;
    private boolean mousePressed;
    private Stage parent;
    private Picture picture;
    private Messenger msg;
    private Stage primaryStage;
        private Scene scene;
            private BorderPane borderPane;
                private ToolBar toolBar;
                    private ColorPicker colorPicker;
                    private Label size;
                    private Spinner<Integer> sizePicker;
                    private Button saveButton;
                    private Button cancelButton;
                private Group centerGroup;
                    private Canvas paintingCanvas;
                    private Canvas toolCanvas;

    public DrawOnPicture(Stage parent, Picture picture, Messenger msg){
        this.parent = parent;
        this.picture =picture;
        this.msg = msg;

        initialize();
        drawPictureOnPaintingCanvas();
        addHandlers();
        setAppearance();
        animate();
        putTogether();
    }

    private void initialize(){
        primaryStage = new Stage();
        borderPane = new BorderPane();
        scene = new Scene(borderPane);
        toolBar = new ToolBar();
        colorPicker = new ColorPicker();
        size = new Label(msg.translate("drawOnPictureSizeLabel"));
        sizePicker = new Spinner<>(1,200,10);
        saveButton = new Button(msg.translate("drawOnPictureSave"));
        cancelButton = new Button(msg.translate("btnAppCancel"));
        centerGroup = new Group();
        paintingCanvas = new Canvas(picture.getWidth(),picture.getHeigth());
        toolCanvas = new Canvas(picture.getWidth(),picture.getHeigth());
    }

    private void drawPictureOnPaintingCanvas(){
        GraphicsContext gcp = paintingCanvas.getGraphicsContext2D();
        try(InputStream is = pictureFacade.resolvePictureURI(picture.getUri())){
            Image img = new Image(is);
            gcp.drawImage(img,0,0);
        }catch (IOException| FacadeException e){
            e.printStackTrace();
        }
    }

    private void addHandlers(){
        cancelButton.setOnAction(event -> {
            primaryStage.close();
        });

        saveButton.setOnAction(event -> {
            paintingCanvas.snapshot(null,null);
        });

        centerGroup.addEventFilter(MouseEvent.ANY,event -> {
            mouseX = event.getX();
            mouseY = event.getY();
            mousePressed = event.isPrimaryButtonDown();
        });
    }

    private void setAppearance(){
        paintingCanvas.toBack();
        toolCanvas.toFront();
        sizePicker.setEditable(true);
    }

    private void animate(){
        AnimationTimer ani = new AnimationTimer() {
            GraphicsContext gct = toolCanvas.getGraphicsContext2D();
            GraphicsContext gcp = paintingCanvas.getGraphicsContext2D();
            @Override
            public void handle(long now) {
                double size = sizePicker.getValue();
                gcp.setFill(Paint.valueOf(colorPicker.getValue().toString()));
                gct.clearRect(0,0,picture.getWidth(),picture.getHeigth());
                gct.strokeOval(mouseX-size/2,mouseY-size/2,size,size);

                if(mousePressed){
                    gcp.fillOval(mouseX-size/2,mouseY-size/2,size,size);
                }
            }
        };
        ani.start();
    }

    private void putTogether(){
        borderPane.setCenter(centerGroup);
        centerGroup.getChildren().addAll(paintingCanvas,toolCanvas);
        borderPane.setTop(toolBar);
        toolBar.getItems().addAll(colorPicker,size,sizePicker,saveButton, cancelButton);
        primaryStage.setScene(scene);
        primaryStage.initOwner(parent);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
    }

    public void show(){
        primaryStage.showAndWait();
    }
}
