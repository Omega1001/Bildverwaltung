package bildverwaltung.gui.fx.masterview;

import java.util.function.Supplier;

import bildverwaltung.gui.fx.util.RebuildebleSubComponent;
import bildverwaltung.localisation.Messenger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class ToolbarArea extends RebuildebleSubComponent {

	private Supplier<Stage> masterStage;
	private Supplier<PictureArea> viewArea;
	
	public ToolbarArea(Messenger msg, Supplier<Stage> masterStage, Supplier<PictureArea> viewArea) {
		super(msg);
		this.masterStage = masterStage;
		this.viewArea = viewArea;
	}

	@Override
	protected Node build() {
		MenuBar mBar = new MenuBar(buildFileMenu(), buildOrganiseMenu(), buildImportMenu());
		return mBar;
	}

	private Menu buildFileMenu() {
		Menu file = new Menu(msg().translate("labelMasterViewToolbarFile"));
		MenuItem quit = new MenuItem(msg().translate("labelMasterViewToolbarFileQuit"));
		quit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				masterStage.get().close();
			}
		});
		file.getItems().addAll(quit);
		return file;
	}

	private Menu buildOrganiseMenu() {
		Menu organise = new Menu(msg().translate("menuItemMasterViewToolbarOrganise"));
		Menu album = buildOrganiseAlbumMenu();
		Menu picture = buildOrganisePictureMenu();
		organise.getItems().addAll(album, picture);
		return organise;
	}

	private Menu buildOrganiseAlbumMenu() {
		Menu album = new Menu(msg().translate("menuItemMasterViewToolbarOrganiseAlbum"));
		MenuItem show = new MenuItem(msg().translate("menuItemMasterViewToolbarOrganiseAlbumDisplay"));
		MenuItem add = new MenuItem(msg().translate("menuItemMasterViewToolbarOrganiseAlbumCreate"));
		MenuItem del = new MenuItem(msg().translate("menuItemMasterViewToolbarOrganiseAlbumDelete"));
		album.getItems().addAll(show, add, del);
		return album;
	}

	private Menu buildOrganisePictureMenu() {
		Menu picture = new Menu(msg().translate("menuItemMasterViewToolbarOrganisePicture"));
		MenuItem show = new MenuItem(msg().translate("menuItemMasterViewToolbarOrganisePictureDisplay"));
		MenuItem toAlbum = new MenuItem(msg().translate("menuItemMasterViewToolbarOrganisePictureToAlbum"));
		MenuItem del = new MenuItem(msg().translate("menuItemMasterViewToolbarOrganisePictureDelete"));
		picture.getItems().addAll(show, toAlbum, del);
		return picture;
	}

	private Menu buildImportMenu() {
		Menu importM = new Menu(msg().translate("labelMasterViewToolbarImport"));
		MenuItem importPictures = new MenuItem(msg().translate("menuItemMasterViewToolbarImport"));
		importM.getItems().addAll(importPictures);
		return importM;
	}

}
