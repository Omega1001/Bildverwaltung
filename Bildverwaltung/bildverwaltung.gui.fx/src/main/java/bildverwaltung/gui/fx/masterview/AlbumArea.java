package bildverwaltung.gui.fx.masterview;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bildverwaltung.container.Container;
import bildverwaltung.container.Scope;
import bildverwaltung.facade.AlbumFacade;
import bildverwaltung.gui.fx.util.RebuildebleSubComponent;
import bildverwaltung.localisation.Messenger;
import bildverwaltung.utils.DBDataRefference;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;

public class AlbumArea extends RebuildebleSubComponent {
	private AlbumFacade albumFacade = Container.getActiveContainer().materialize(AlbumFacade.class,Scope.APPLICATION);
	private Supplier<PictureArea> viewArea;

	private ObservableList<DBDataRefference<String>> albums = FXCollections.observableArrayList();
	private ListView<DBDataRefference<String>> albumList = new ListView<>(albums);

	public AlbumArea(Messenger msg, Supplier<PictureArea> viewArea) {
		super(msg);
		this.viewArea = viewArea;
	}

	@Override
	protected Node build() {
		TitledPane title = new TitledPane();
		title.setText(msg().translate("headerTextMasterViewInfoAreaAlbumArea"));
		title.setContent(albumList);
		return title;
	}

	public ObservableList<DBDataRefference<String>> getAlbums() {
		return albums;
	}
	
	

}
