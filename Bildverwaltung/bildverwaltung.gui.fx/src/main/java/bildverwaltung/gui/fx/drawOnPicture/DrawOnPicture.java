package bildverwaltung.gui.fx.drawOnPicture;

import bildverwaltung.container.Container;
import bildverwaltung.container.Scope;
import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.dao.exception.ServiceException;
import bildverwaltung.facade.PictureFacade;
import bildverwaltung.facade.PictureImportFacade;
import bildverwaltung.gui.fx.masterview.PictureArea;
import bildverwaltung.gui.fx.masterview.dialogs.SavingDialog;
import bildverwaltung.localisation.Messenger;
import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
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
    private PictureArea pictureArea ;
    private double zoom;

    private Stage primaryStage;
        private Scene scene;
            private BorderPane borderPane;
                private ToolBar toolBar;
                    private ColorPicker colorPicker;
                    private Label size;
                    private Spinner<Integer> sizePicker;
                    private Button saveButton;
                    private Button cancelButton;
                    private Button zoomIn;
                    private Button zoomOut;
                private ScrollPane scrollPane ;
                    private Group centerGroup;
                        private Canvas paintingCanvas;
                        private Canvas toolCanvas;

    public DrawOnPicture(Stage parent, Picture picture, Messenger msg, PictureArea pictureArea){
        this.parent = parent;
        this.picture =picture;
        this.msg = msg;
        this.pictureArea = pictureArea;

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
        zoomIn= new Button("+");
        zoomOut= new Button("-");
        scrollPane=new ScrollPane();
        zoom=1;
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
            SavingDialog.selected result= SavingDialog.showAndWait();
            toolCanvas.setScaleY(1);
            toolCanvas.setScaleX(1);
            paintingCanvas.setScaleY(1);
            paintingCanvas.setScaleX(1);
            centerGroup.setScaleX(1);
            centerGroup.setScaleY(1);

            if (result == SavingDialog.selected.OVERRIDE) {
                WritableImage ri = paintingCanvas.snapshot(null, new WritableImage(picture.getWidth(),picture.getHeigth()));
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(ri, null), "png", new File(picture.getUri()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int selectedPictureIndex = pictureArea.getPictures().indexOf(picture);
                pictureArea.getPictures().remove(selectedPictureIndex);
                pictureArea.getPictures().add(selectedPictureIndex,picture);
                primaryStage.close();
            }else if(result== SavingDialog.selected.SAVE_NEW){
                WritableImage ri = paintingCanvas.snapshot(null, new WritableImage(picture.getWidth(),picture.getHeigth()));
                PictureImportFacade pi = Container.getActiveContainer().materialize(PictureImportFacade.class,Scope.APPLICATION);
                Picture newPicture=null;
                try {
                    File f=new File(picture.getName()+"new.png");
                    f.createNewFile();
                    ImageIO.write(SwingFXUtils.fromFXImage(ri, null), "png",f);
                    newPicture =pi.importPicture(f);
                    f.delete();
                } catch (ServiceException | IOException e) {
                    e.printStackTrace();
                }

                pictureArea.getPictures().add(newPicture);
                primaryStage.close();
            }
        });

        toolCanvas.addEventFilter(MouseEvent.ANY,event -> {
            mouseX = event.getX();
            mouseY = event.getY();
            mousePressed = event.isPrimaryButtonDown();
        });

        zoomIn.setOnAction(e->{
            zoom*=2;
            toolCanvas.setScaleY(zoom);
            toolCanvas.setScaleX(zoom);
            paintingCanvas.setScaleY(zoom);
            paintingCanvas.setScaleX(zoom);
            centerGroup.setScaleX(zoom);
            centerGroup.setScaleY(zoom);
        });

        zoomOut.setOnAction(e->{
            zoom/=2;
            toolCanvas.setScaleY(zoom);
            toolCanvas.setScaleX(zoom);
            paintingCanvas.setScaleY(zoom);
            paintingCanvas.setScaleX(zoom);
            centerGroup.setScaleX(zoom);
            centerGroup.setScaleY(zoom);
        });
    }

    private void setAppearance(){
        paintingCanvas.toBack();
        toolCanvas.toFront();
        sizePicker.setEditable(true);
        borderPane.setPrefSize(800,800);
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
        borderPane.setCenter(scrollPane);
        scrollPane.setContent(centerGroup);
        centerGroup.getChildren().addAll(paintingCanvas,toolCanvas);
        borderPane.setTop(toolBar);
        toolBar.getItems().addAll(colorPicker,size,sizePicker,zoomIn,zoomOut,saveButton, cancelButton);
        primaryStage.setScene(scene);
        primaryStage.initOwner(parent);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
    }

    public void show(){
        primaryStage.showAndWait();
    }
}
