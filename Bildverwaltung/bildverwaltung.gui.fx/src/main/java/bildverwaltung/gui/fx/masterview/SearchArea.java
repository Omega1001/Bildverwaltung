package bildverwaltung.gui.fx.masterview;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import bildverwaltung.container.Container;
import bildverwaltung.dao.entity.Album;
import bildverwaltung.dao.entity.Album_;
import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.entity.Picture_;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.facade.PictureFacade;
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

	private PictureFacade pictureFacade = Container.getActiveContainer().materialize(PictureFacade.class);

	private Supplier<PictureArea> viewArea;
	private SearchManager<Picture> sm;
	private Supplier<AlbumArea> albumArea;

	public SearchArea(Messenger msg, Supplier<PictureArea> viewArea, Supplier<AlbumArea> albumArea) {
		super(msg);
		this.viewArea = viewArea;
		this.albumArea = albumArea;
		sm = generateSearchManager();
	}

	private SearchManager<Picture> generateSearchManager() {
		List<SearchCategory<Picture>> cathegories = Arrays.asList(buildPictureCathegory(), buildAlbumCathegory());
		return new SearchManager<>(cathegories, Picture.class, msg().getTranslator());
	}

	private SearchCategory<Picture> buildAlbumCathegory() {
		return new SearchCategoryBuilder<Album>()
				.addEntry(msg().translate("labelMasterViewInfoAreaSearchAreaAlbumName"), Album_.name)
				.asForeignOwned(msg().translate("headerTextMasterViewInfoAreaSearchAreaAlbum"), Picture_.alben);
	}

	private SearchCategory<Picture> buildPictureCathegory() {
		return new SearchCategoryBuilder<Picture>()
				.addEntry(msg().translate("labelMasterViewInfoAreaSearchAreaPictureName"), Picture_.name)
				.asEntityOwned(msg().translate("headerTextMasterViewInfoAreaSearchAreaPicture"));
	}

	private Node generateButtons() {
		Label l = new Label();
		l.setMaxWidth(Double.MAX_VALUE);
		Button search = new Button(msg().translate("btnMasterViewInfoAreaSearchAreaSearch"));
		search.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					List<Picture> pics = pictureFacade.getFiltered(sm.toFilter(), null);
					viewArea.get().getPictures().clear();
					viewArea.get().getPictures().addAll(pics);
					albumArea.get().resetSelection();
				} catch (FacadeException ex) {
					msg().showExceptionMessage(ex);
				}
			}
		});
		Button reset = new Button(msg().translate("btnMasterViewInfoAreaSearchAreaReset"));
		reset.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				sm.reset();
				try {
					List<Picture> pics = pictureFacade.getAllPictures();
					viewArea.get().getPictures().clear();
					viewArea.get().getPictures().addAll(pics);
					albumArea.get().resetSelection();
				} catch (FacadeException ex) {
					msg().showExceptionMessage(ex);
				}
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
