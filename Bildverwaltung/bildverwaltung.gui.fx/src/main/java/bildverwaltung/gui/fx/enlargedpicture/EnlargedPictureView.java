package bildverwaltung.gui.fx.enlargedpicture;

import bildverwaltung.container.Container;
import bildverwaltung.container.Scope;
import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.facade.PictureFacade;
import bildverwaltung.gui.fx.util.PictureIterator;
import bildverwaltung.gui.fx.util.WrappedImageView;
import bildverwaltung.gui.fx.util.IconLoader;
import bildverwaltung.localisation.Messenger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.io.InputStream;

public class EnlargedPictureView {

	private final Messenger msg;
	private Stage mainWindow;

	private PictureFacade facade;

	private Picture currentPicture;
	private PictureIterator it;

	private BorderPane mainBorderPane;
	private StackPane bpImg;
	private Scene scene;
	private HBox navBtnBox;
	private BorderPane toolbar;

	private Button btnBack;
	private Button btnNext;
	private Button btnFullScreen;
	private Button btnDiashow;
	private Label curPictureCount;
	private BorderPane toolBarFullscreen;
	
	private boolean diashowStatus = false;
	private Timeline timeline;
	private HBox navBtnBoxLeft;
	private ChoiceBox<Integer> secondsBox;

	private static final double BTN_SIZE = 50.0;

	public EnlargedPictureView(Messenger msg) {
		facade = Container.getActiveContainer().materialize(PictureFacade.class,Scope.APPLICATION);
		this.msg = msg;
		mainWindow = new Stage();

		initializeNodes();
		setEventHandlersAndListeners();
		setAppearance();
		putNodesTogether();
		
		secondsBox.setValue(5);
		secondsBox.getItems().addAll(1, 2, 3, 5, 10);
	}

	private void initializeNodes() {
		mainBorderPane = new BorderPane();
		bpImg = new StackPane();
		scene = new Scene(mainBorderPane, 1280,720);
		navBtnBox = new HBox();
		toolbar = new BorderPane();
		toolBarFullscreen = new BorderPane();

		btnNext = new Button();
		btnBack = new Button();
		btnFullScreen = new Button();
		
		
		btnDiashow = new Button();
		navBtnBoxLeft = new HBox();
		secondsBox = new ChoiceBox<>();

		curPictureCount = new Label();
	}

	private void setEventHandlersAndListeners() {
		btnNext.setOnAction(actionEvent -> nextPicture());
		btnBack.setOnAction(actionEvent -> previousPicture());
		btnFullScreen.setOnAction(actionEvent -> mainWindow.setFullScreen(!mainWindow.isFullScreen()));
		btnDiashow.setOnAction(actionEvent -> diashow());

		// Keyboard stuff
        // Left or Right Key: next / previous picture
		scene.setOnKeyPressed(keyEvent -> {
			if(keyEvent.getCode() == KeyCode.RIGHT) {
				nextPicture();
			} else if(keyEvent.getCode() == KeyCode.LEFT) {
				previousPicture();
			}
		});

		// When enlargedPicture switches to fullscreen, hide the toolbar
		mainWindow.fullScreenProperty().addListener((v,o,n) -> {

				if(mainWindow.isFullScreen()) {
					double size = toolbar.getHeight();

					toolbar.setVisible(false);
					toolbar.setManaged(false);

					mainBorderPane.getChildren().remove(toolbar);

					toolBarFullscreen.setBottom(toolbar);
					bpImg.getChildren().addAll(toolBarFullscreen);

					bpImg.setStyle("-fx-background-color: Black; -fx-border-color: Black; -fx-border-width: 0px;");
					toolbar.setStyle("-fx-background-color: White; -fx-border-color: White; -fx-padding: 5 5 5 5;");

					scene.setOnMouseMoved(mouseEvent -> {
						if (mouseEvent.getY() > mainBorderPane.getHeight() - (size * 1.25)) {
							toolbar.setManaged(true);
							toolbar.setVisible(true);
						} else {
							toolbar.setManaged(false);
							toolbar.setVisible(false);
						}});
				} else {
					scene.setOnMouseMoved(null);

					bpImg.getChildren().remove(toolBarFullscreen);
					toolBarFullscreen.getChildren().remove(toolbar);
					mainBorderPane.setBottom(toolbar);
					toolbar.setVisible(true);
					toolbar.setManaged(true);

					bpImg.setStyle("-fx-background-color: White; -fx-border-color: White; -fx-border-width: 10px;");
					toolbar.setStyle("-fx-background-color: White; -fx-border-color: White; -fx-padding: 0 5 5 5;");
				}
		});
	}

	private void setAppearance() {
		mainWindow.setResizable(true);
		mainWindow.setMinHeight(450.0);
		mainWindow.setMinWidth(800.0);
		mainWindow.setTitle(msg.translate("enlargedPictureTitle"));
		mainWindow.setFullScreenExitHint(msg.translate("enlargedPictureFullscreenMsg"));

		btnNext.setGraphic(IconLoader.loadIcon("Weiter.png"));
		btnNext.setPrefSize(BTN_SIZE,BTN_SIZE);
		btnNext.setTooltip(new Tooltip(msg.translate("buttonEnlargedPictureToolBarNextPicture")));

		btnBack.setGraphic(IconLoader.loadIcon("Zurueck.png"));
		btnBack.setPrefSize(BTN_SIZE,BTN_SIZE);
		btnBack.setTooltip(new Tooltip(msg.translate("buttonEnlargedPictureToolBarLastPicture")));

		navBtnBox.setSpacing(5.0);
		navBtnBox.setAlignment(Pos.BOTTOM_CENTER);

		btnFullScreen.setGraphic(IconLoader.loadIcon("Grossansicht.png"));
		btnFullScreen.setPrefSize(BTN_SIZE, BTN_SIZE);
		btnFullScreen.setTooltip(new Tooltip(msg.translate("buttonEnlargedPictureToolBarFullscreen")));	
		
		btnDiashow.setGraphic(IconLoader.loadIcon("Diashow.png"));
		btnDiashow.setPrefSize(BTN_SIZE, BTN_SIZE);
		btnDiashow.setTooltip(new Tooltip(msg.translate("buttonEnlargedPictureToolBarDiashow")));
		
		navBtnBoxLeft.setSpacing(5.0);
		navBtnBoxLeft.setAlignment(Pos.BOTTOM_LEFT);
		
		BorderPane.setAlignment(curPictureCount,Pos.BOTTOM_CENTER);
		BorderPane.setAlignment(navBtnBox,Pos.BOTTOM_CENTER);
		BorderPane.setAlignment(btnFullScreen, Pos.BOTTOM_RIGHT);

		curPictureCount.setFont((Font.font(BTN_SIZE/2.0)));
		curPictureCount.setTextAlignment(TextAlignment.CENTER);
		
		bpImg.setStyle("-fx-background-color: White; -fx-border-color: White; -fx-border-width: 10px;");
		toolbar.setStyle("-fx-background-color: White; -fx-border-color: White; -fx-padding: 0 5 5 5;");
	}

	private void putNodesTogether() {
        mainWindow.setScene(scene);
        mainWindow.initModality(Modality.APPLICATION_MODAL);
		navBtnBox.getChildren().addAll(btnBack,curPictureCount,btnNext);
		toolbar.setRight(btnFullScreen);
		toolbar.setCenter(navBtnBox);
		
		
		toolbar.setLeft(navBtnBoxLeft);
		navBtnBoxLeft.getChildren().addAll(btnDiashow, secondsBox);
		

		mainBorderPane.setBottom(toolbar);
	}

	public void showEnlargedPicture(PictureIterator it) {
		this.it = it;
		curPictureCount.setText(it.nextIndex() + 1 + "/" + it.getIteratorSize());
		updateCurrentEnlargedPicture(it.next());
        mainWindow.showAndWait();
	}

	private void updateCurrentEnlargedPicture(Picture picture) {
		bpImg.getChildren().clear();

		try {
			InputStream is = facade.resolvePictureURI(picture.getUri())	;
			WrappedImageView image = new WrappedImageView();
			image.setImage(new Image(is));
			image.setPreserveRatio(true);
			image.setCache(true);
			image.isSmooth();
			bpImg.getChildren().addAll(image);

			if(mainWindow.isFullScreen()) {
				bpImg.getChildren().add(toolBarFullscreen);
			}

			mainBorderPane.setCenter(bpImg);

			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FacadeException e) {
			e.printStackTrace();
		}
	}

	private void nextPicture() {
			Picture newPicture = it.next();
			if(newPicture == currentPicture) {
				newPicture = it.next();
			}
			currentPicture = newPicture;

			updateCurrentEnlargedPicture(currentPicture);
			curPictureCount.setText(it.nextIndex() + "/" + it.getIteratorSize());
	}

	private void previousPicture() {
			Picture newPicture = it.previous();
			if(newPicture == currentPicture) {
				newPicture = it.previous();
			}
			currentPicture = newPicture;

			updateCurrentEnlargedPicture(currentPicture);
			curPictureCount.setText((it.nextIndex()+1) + "/" + it.getIteratorSize());
	}
	
	/**
	 * Method for starting a diashow
	 */
	private void diashow() {
		diashowStatus = !diashowStatus;
		if(diashowStatus == true) {
			timeline = new Timeline(new KeyFrame(Duration.seconds(getSeconds()), actionEvent -> nextPicture()));
			timeline.setCycleCount(Animation.INDEFINITE);
			timeline.play();
		}
		if(diashowStatus == false) {
			timeline.stop();
		}
	}
	
	/**
	 * Method which returns the current value of the secondsBox
	 * @return
	 */
	private Integer getSeconds() {
		return secondsBox.getValue();
	}
}
