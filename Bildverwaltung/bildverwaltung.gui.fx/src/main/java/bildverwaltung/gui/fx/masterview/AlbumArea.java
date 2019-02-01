package bildverwaltung.gui.fx.masterview;

import java.util.function.Supplier;

import bildverwaltung.container.Container;
import bildverwaltung.container.Scope;
import bildverwaltung.dao.exception.FacadeException;
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
		albums.clear();
		try {
			albums.addAll(albumFacade.getAllAlbumNameReferences());
		} catch (FacadeException e) {
			msg().showExceptionMessage(e);
		}
		TitledPane title = new TitledPane();
		title.setText(msg().translate("headerTextMasterViewInfoAreaAlbumArea"));
		title.setContent(albumList);
		return title;
	}

}
