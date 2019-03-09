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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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
	private Label curPictureCount;

	private static final double BTN_SIZE = 50.0;

	public EnlargedPictureView(Messenger msg) {
		facade = Container.getActiveContainer().materialize(PictureFacade.class,Scope.APPLICATION);
		this.msg = msg;
		mainWindow = new Stage();

		initializeNodes();
		setEventHandlersAndListeners();
		setAppearance();
		putNodesTogether();
	}


	private void initializeNodes() {
		mainBorderPane = new BorderPane();
		bpImg = new StackPane();
		scene = new Scene(mainBorderPane, 1280,720);
		navBtnBox = new HBox();
		toolbar = new BorderPane();

		btnNext = new Button();
		btnBack = new Button();
		btnFullScreen = new Button();

		curPictureCount = new Label();
	}

	private void setEventHandlersAndListeners() {
		btnNext.setOnAction(actionEvent -> nextPicture());
		btnBack.setOnAction(actionEvent -> previousPicture());
		btnFullScreen.setOnAction(actionEvent -> mainWindow.setFullScreen(!mainWindow.isFullScreen()));

		// Keyboard stuff
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
					//bpImg.setStyle("-fx-background-color: Black;");
					bpImg.getChildren().add(btnFullScreen);
					StackPane.setAlignment(btnFullScreen,Pos.TOP_RIGHT);

					scene.setOnMouseMoved(mouseEvent -> {
						if (mouseEvent.getY() > mainBorderPane.getHeight() - (size * 1.1)) {
							toolbar.setManaged(true);
							toolbar.setVisible(true);
						} else {
							toolbar.setManaged(false);
							toolbar.setVisible(false);
						}});
				} else {
					scene.setOnMouseMoved(null);
					toolbar.setVisible(true);
					toolbar.setManaged(true);
					//bpImg.getChildren().remove(btnFullScreen);
					toolbar.getChildren().add(btnFullScreen);
					//bpImg.setStyle("-fx-background-color: White;");
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
		navBtnBox.setAlignment(Pos.CENTER);

		//btnClose

		btnFullScreen.setGraphic(IconLoader.loadIcon("Grossansicht.png"));
		btnBack.setPrefSize(BTN_SIZE,BTN_SIZE);
		btnFullScreen.setTooltip(new Tooltip(msg.translate("buttonEnlargedPictureToolBarFullscreen")));

		BorderPane.setAlignment(navBtnBox,Pos.CENTER);

		BorderPane.setAlignment(curPictureCount,Pos.CENTER);

		curPictureCount.setFont((Font.font(BTN_SIZE/2.0)));


	}

	private void putNodesTogether() {
        mainWindow.setScene(scene);
		navBtnBox.getChildren().addAll(btnBack,curPictureCount,btnNext);
		toolbar.setRight(btnFullScreen);
		toolbar.setCenter(navBtnBox);



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
			//bpImg.setCenter(image);
			bpImg.getChildren().addAll(image);

			if(mainWindow.isFullScreen()) {
				bpImg.getChildren().add(btnFullScreen);
				StackPane.setAlignment(btnFullScreen,Pos.TOP_RIGHT);
			}

			mainBorderPane.setCenter(bpImg);
			Insets bpImgInsets = new Insets(10);
			BorderPane.setMargin(bpImg,bpImgInsets);

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
}
