package bildverwaltung.gui.fx.masterview;

import java.util.Iterator;
import java.util.UUID;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bildverwaltung.dao.entity.Album;
import bildverwaltung.gui.fx.util.RebuildebleSubComponent;
import bildverwaltung.localisation.Messenger;
import bildverwaltung.utils.DBDataRefference;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;

public class AlbumArea extends RebuildebleSubComponent {
	private static final Logger LOG = LoggerFactory.getLogger(AlbumArea.class);
	private Supplier<PictureArea> viewArea;

	private ObservableList<DBDataRefference<String>> albums = FXCollections.observableArrayList();
	private ListView<DBDataRefference<String>> albumList = null;

	private boolean ignoreSelectionUpdate = false;

	public AlbumArea(Messenger msg, Supplier<PictureArea> viewArea) {
		super(msg);
		this.viewArea = viewArea;
	}

	@Override
	protected Node build() {
		TitledPane title = new TitledPane();
		title.setText(msg().translate("headerTextMasterViewInfoAreaAlbumArea"));
		albumList = new ListView<>(albums);
		albums.add(new DBDataRefference<String>(msg().translate("labelMasterViewInfoAreaAlbumAreaAllPictures"), null));
		albumList.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<DBDataRefference<String>>() {

					@Override
					public void changed(ObservableValue<? extends DBDataRefference<String>> observable,
							DBDataRefference<String> oldValue, DBDataRefference<String> newValue) {
						if (ignoreSelectionUpdate) {
							ignoreSelectionUpdate = false;
							return;
						}
						if (newValue == null || newValue.getRefferencedComponentId() == null) {
							if (!viewArea.get().loadAllPictures()) {
								albumList.getSelectionModel().select(oldValue);
							}
						} else {
							if (!viewArea.get().loadAlbumById(newValue.getRefferencedComponentId())) {
								albumList.getSelectionModel().select(oldValue);
							}
						}
					}
				});
		title.setContent(albumList);
		return title;
	}

	public ObservableList<DBDataRefference<String>> getAlbums() {
		return albums;
	}

	public void resetSelection() {
		ignoreSelectionUpdate = true;
		albumList.getSelectionModel().clearSelection();
	}

	public void selectAlbum(Album album) {
		if (album != null) {
			for (DBDataRefference<String> dbref : albums) {
				if (dbref.getRefferencedComponentId().equals(album.getId())) {
					albumList.getSelectionModel().select(dbref);
					return;
				}
			}
			LOG.warn("Tried to select unknown Album {}, resetting selection", album);
		}
		albumList.getSelectionModel().clearSelection();
	}

	public void removeAlbumWithId(UUID id) {
		if (id != null) {
			
			DBDataRefference<String> item = albumList.getSelectionModel().getSelectedItem();
			if (item != null && id.equals(item.getRefferencedComponentId())) {
				albumList.getSelectionModel().clearSelection();
			}
			
			Iterator<DBDataRefference<String>> it = albums.iterator();
			while (it.hasNext()) {
				if (it.next().getRefferencedComponentId().equals(id)) {
					it.remove();
				}
			}
			
		}
	}

}
