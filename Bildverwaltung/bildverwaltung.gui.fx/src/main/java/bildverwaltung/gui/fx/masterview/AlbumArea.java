package bildverwaltung.gui.fx.masterview;

import java.util.function.Supplier;

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
		albumList.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<DBDataRefference<String>>() {

					@Override
					public void changed(ObservableValue<? extends DBDataRefference<String>> observable,
							DBDataRefference<String> oldValue, DBDataRefference<String> newValue) {
						if(ignoreSelectionUpdate) {
							ignoreSelectionUpdate = false;
							return;
						}
						if (newValue == null) {
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

}
