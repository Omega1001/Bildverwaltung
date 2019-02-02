package bildverwaltung.gui.fx.masterview;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import bildverwaltung.dao.entity.Album;
import bildverwaltung.dao.entity.Album_;
import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.entity.Picture_;
import bildverwaltung.gui.fx.search.SearchCategory;
import bildverwaltung.gui.fx.search.SearchCategoryBuilder;
import bildverwaltung.gui.fx.search.SearchManager;
import bildverwaltung.gui.fx.util.RebuildebleSubComponent;
import bildverwaltung.localisation.Messenger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SearchArea extends RebuildebleSubComponent {

	private Supplier<PictureArea> viewArea;
	private SearchManager<Picture> sm;

	public SearchArea(Messenger msg, Supplier<PictureArea> viewArea) {
		super(msg);
		this.viewArea = viewArea;
		sm = generateSearchManager();
	}

	private SearchManager<Picture> generateSearchManager() {
		List<SearchCategory<Picture>> cathegories = Arrays.asList(buildPictureCathegory(), buildAlbumCathegory());
		return new SearchManager<>(cathegories, Picture.class, msg().getTranslator());
	}

	private SearchCategory<Picture> buildAlbumCathegory() {
		return new SearchCategoryBuilder<Album>()
				.addEntry(msg().translate("labelMasterViewInfoAreaSearchAreaPictureName"), Album_.name)
				.asForeignOwned(msg().translate("headerTextMasterViewInfoAreaSearchAreaPicture"), Picture_.alben);
	}

	private SearchCategory<Picture> buildPictureCathegory() {
		return new SearchCategoryBuilder<Picture>()
				.addEntry(msg().translate("labelMasterViewInfoAreaSearchAreaAlbumName"), Picture_.name)
				.asEntityOwned(msg().translate("headerTextMasterViewInfoAreaSearchAreaAlbum"));
	}

	private Node generateButtons() {
		Label l = new Label();
		l.setMaxWidth(Double.MAX_VALUE);
		Button search = new Button(msg().translate("btnMasterViewInfoAreaSearchAreaSearch"));
		Button reset = new Button(msg().translate("btnMasterViewInfoAreaSearchAreaReset"));
		reset.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				sm.reset();
			}
		});

		HBox.setHgrow(l, Priority.ALWAYS);
		HBox.setHgrow(search, Priority.NEVER);
		HBox.setHgrow(reset, Priority.NEVER);
		HBox buttons = new HBox(l, search, reset);
		return buttons;
	}

	@Override
	protected Node build() {
		TitledPane body = new TitledPane();
		body.setText(msg().translate("headerTextMasterViewInfoAreaSearchAreaHeader"));

		VBox vbox = new VBox(sm.getGuiElement(), generateButtons());
		body.setContent(vbox);
		return body;
	}

}
