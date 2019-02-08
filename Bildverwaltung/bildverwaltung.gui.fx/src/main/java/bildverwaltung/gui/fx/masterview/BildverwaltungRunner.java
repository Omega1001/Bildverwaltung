package bildverwaltung.gui.fx.masterview;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bildverwaltung.container.Container;
import bildverwaltung.container.ManagedContainer;
import bildverwaltung.container.Scope;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.facade.AlbumFacade;
import bildverwaltung.facade.PictureFacade;
import bildverwaltung.localisation.Messenger;
import bildverwaltung.localisation.MessengerImpl;
import bildverwaltung.localisation.TranslatorImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class BildverwaltungRunner extends Application {
	private static final Logger LOG = LoggerFactory.getLogger(BildverwaltungRunner.class);

	public static void main(String[] args) throws FacadeException {
		LOG.info("Bildverwaltung is starting up ...");
		Container.startupContainer();
		addAdditionalFactories(Container.getActiveContainer());
		launch(args);
		LOG.debug("Application close trigered");
		Container.shutdown();
		LOG.info("Bildverwaltung will exit now");
	}

	private PictureFacade pictureFacade = Container.getActiveContainer().materialize(PictureFacade.class,
			Scope.APPLICATION);
	private AlbumFacade albumFacade = Container.getActiveContainer().materialize(AlbumFacade.class, Scope.APPLICATION);
	private Messenger msg = Container.getActiveContainer().materialize(Messenger.class, Scope.APPLICATION);

	private ToolbarArea toolbar;
	private InfoArea infoArea;
	private PictureArea viewArea;
	private Stage masterStage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Init components
		LOG.debug("Begin building application gui");
		this.masterStage = primaryStage;
		this.toolbar = new ToolbarArea(msg, () -> masterStage, () -> viewArea,()->infoArea.getAlbumArea());
		this.infoArea = new InfoArea(msg, () -> viewArea);
		this.viewArea = new PictureArea(msg);
		LOG.debug("Sub-component initialisation complete");
		// Generate Layout
		BorderPane layout = new BorderPane();
		layout.setTop(toolbar.getGraphic());
		layout.setLeft(infoArea.getGraphic());
		layout.setCenter(viewArea.getGraphic());
		LOG.debug("All components generated");
		// Load Data
		viewArea.getPictures().addAll(pictureFacade.getAllPictures());
		infoArea.getAlbumArea().getAlbums().addAll(albumFacade.getAllAlbumNameReferences());
		LOG.debug("Finished populating data");
		// Put in service
		Scene s = new Scene(layout);
		primaryStage.setScene(s);
		//Give a name for this project
		primaryStage.setTitle("Bildverwaltung");      
		// Show
		LOG.debug("Prepairing to show main window");
		primaryStage.show();
	}

	private static void addAdditionalFactories(ManagedContainer activeContainer) {
		LOG.trace("Enter addAdditionalFactories");
		activeContainer.addFactory(TranslatorImpl.FACTORY);
		activeContainer.addFactory(MessengerImpl.FACTORY);
		LOG.trace("Exit addAdditionalFactories");
	}

}
