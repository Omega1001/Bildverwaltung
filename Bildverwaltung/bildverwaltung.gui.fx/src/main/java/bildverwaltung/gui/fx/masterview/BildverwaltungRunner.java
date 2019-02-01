package bildverwaltung.gui.fx.masterview;

import bildverwaltung.container.Container;
import bildverwaltung.container.ManagedContainer;
import bildverwaltung.container.Scope;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.facade.PictureFacade;
import bildverwaltung.localisation.Messenger;
import bildverwaltung.localisation.MessengerImpl;
import bildverwaltung.localisation.TranslatorImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BildverwaltungRunner extends Application {

	public static void main(String[] args) throws FacadeException {
		Container.startupContainer();
		addAdditionalFactories(Container.getActiveContainer());
		launch(args);
		Container.shutdown();
	}

	private PictureFacade pictureFacade = Container.getActiveContainer().materialize(PictureFacade.class,Scope.APPLICATION);
	private Messenger msg;

	private ToolbarArea toolbar;
	private InfoArea infoArea;
	private PictureArea viewArea;
	private Stage masterStage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Init components
		this.msg = Container.getActiveContainer().materialize(Messenger.class,Scope.APPLICATION);
		this.masterStage = primaryStage;
		this.toolbar = new ToolbarArea(msg, () -> masterStage, () -> viewArea);
		this.infoArea = new InfoArea(msg, () -> viewArea);
		this.viewArea = new PictureArea(msg);
		// Generate Layout
		BorderPane layout = new BorderPane();
		layout.setTop(toolbar.getGraphic());
		layout.setLeft(infoArea.getGraphic());
		layout.setCenter(viewArea.getGraphic());
		layout.setBorder(new Border(new BorderStroke(Color.BLACK, 
	            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		//Load Data
		viewArea.getPictures().addAll(pictureFacade.getAllPictures());
		// Put in service
		Scene s = new Scene(layout);
		primaryStage.setScene(s);
		// Show
		primaryStage.show();
	}

	private static void addAdditionalFactories(ManagedContainer activeContainer) {
		activeContainer.addFactory(TranslatorImpl.FACTORY);
		activeContainer.addFactory(MessengerImpl.FACTORY);
	}

}
