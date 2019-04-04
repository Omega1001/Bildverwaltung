package bildverwaltung.gui.fx.masterview;

import java.awt.Dimension;
import java.awt.Frame;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bildverwaltung.container.Container;
import bildverwaltung.container.ManagedContainer;
import bildverwaltung.container.Scope;
import bildverwaltung.facade.AlbumFacade;
import bildverwaltung.facade.PictureFacade;
import bildverwaltung.localisation.Messenger;
import bildverwaltung.localisation.MessengerImpl;
import bildverwaltung.localisation.TranslatorImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class BildverwaltungRunner extends Application {
	private static final Logger LOG = LoggerFactory.getLogger(BildverwaltungRunner.class);

	public static void main(String[] args) {
		LOG.info("Bildverwaltung is starting up ...");
		
		
		tryStartupContainer();
		
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

	private InfoArea infoArea;
	private PictureArea viewArea;
	private Stage masterStage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setMinWidth(800);
		primaryStage.setMinHeight(600);
		// Init components
		LOG.debug("Begin building application gui");
		this.masterStage = primaryStage;
		ToolbarArea toolbar = new ToolbarArea(msg, () -> masterStage, () -> viewArea, () -> infoArea.getAlbumArea());
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
		primaryStage.setTitle("Bilderverwaltung");
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
	/**
	 * Method to startup the container and throw a visible error message containing the cause in case any errors happen.
	 */
	private static void tryStartupContainer() {
		try {
			Container.startupContainer();
	    } 
		catch (Exception exception) {
            StringBuffer sb = new StringBuffer();
            while (exception != null) {
                sb.append(exception.getMessage()).append("\r\n");
                exception = (Exception) exception.getCause();
            }
            String cause = sb.toString();
	    	Frame frame = new Frame();
	    	JTextArea textArea = new JTextArea(cause);
	    	JSplitPane splitPane;
	    	JScrollPane scrollPane = new JScrollPane(textArea);
	    	textArea.setLineWrap(true);  
	    	textArea.setWrapStyleWord(true);
	    	scrollPane.setPreferredSize(new Dimension(1000, 250));

	    	Locale locale = Locale.getDefault();
	    	if (locale.equals(Locale.GERMANY)) {
	    		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
	    									new JLabel("Fehler beim Starten."), 
	    									scrollPane);
	    		JOptionPane.showMessageDialog(frame,
	    				splitPane, "FEHLER!", JOptionPane.ERROR_MESSAGE);
	    	}
	    	else {
	    		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
	    									new JLabel("Error on startup."), 
	    									scrollPane);
	    		JOptionPane.showMessageDialog(frame,
	    				splitPane,
	    				"ERROR!", JOptionPane.ERROR_MESSAGE);
	    	}
	    }
	}

}
