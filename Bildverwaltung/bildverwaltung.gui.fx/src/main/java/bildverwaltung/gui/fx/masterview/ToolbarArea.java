package bildverwaltung.gui.fx.masterview;

import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Supplier;

import bildverwaltung.container.Container;
import bildverwaltung.dao.entity.Album;
import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.facade.AlbumFacade;
import bildverwaltung.facade.PictureFacade;
import bildverwaltung.gui.fx.masterview.dialogs.AlbumCreationDialog;
import bildverwaltung.gui.fx.masterview.dialogs.AlbumSelectionDialog;
import bildverwaltung.gui.fx.util.ConfirmationDialog;
import bildverwaltung.gui.fx.util.IconLoader;
import bildverwaltung.container.Scope;
import bildverwaltung.gui.fx.importdialog.ImportPane;
import bildverwaltung.gui.fx.util.RebuildebleSubComponent;
import bildverwaltung.localisation.Messenger;
import bildverwaltung.utils.DBDataRefference;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class ToolbarArea extends RebuildebleSubComponent {

	private AlbumFacade albumFacade = Container.getActiveContainer().materialize(AlbumFacade.class);
	private PictureFacade pictureFacade = Container.getActiveContainer().materialize(PictureFacade.class);
	private Supplier<Stage> masterStage;
	private Supplier<PictureArea> viewArea;
	private Supplier<AlbumArea> albumArea;
	private String resourcelocation = "resources/Bildverwaltung";		//Bildverwaltung_en einsetzen für englischen Text.
	private ResourceBundle bundle = ResourceBundle.getBundle(resourcelocation);

	public ToolbarArea(Messenger msg, Supplier<Stage> masterStage, Supplier<PictureArea> viewArea,
			Supplier<AlbumArea> albumArea) {
		super(msg);
		this.masterStage = masterStage;
		this.viewArea = viewArea;
		this.albumArea = albumArea;
	}

	@Override
	protected Node build() {
		MenuBar mBar = new MenuBar(buildFileMenu(), buildOrganiseMenu(), buildImportMenu());
		return mBar;
	}

	private Menu buildFileMenu() {
//		Menu file = new Menu(msg().translate("labelMasterViewToolbarFile"));
		Menu file = new Menu((bundle.getString("main-window-toolbar-file-menu")));			//Translation
		file.setGraphic(IconLoader.loadIcon("Datei.png"));

//		MenuItem quit = new MenuItem(msg().translate("labelMasterViewToolbarFileQuit"));
		MenuItem quit = new MenuItem(bundle.getString("main-window-toolbar-file-quit"));			//Translation
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
//		Menu organise = new Menu(msg().translate("menuItemMasterViewToolbarOrganise"));
		Menu organise = new Menu(bundle.getString("main-window-toolbar-manage-menu"));			//Translation
		Menu album = buildOrganiseAlbumMenu();
		Menu picture = buildOrganisePictureMenu();
		organise.getItems().addAll(album, picture);
		return organise;
	}

	private Menu buildOrganiseAlbumMenu() {
//		Menu album = new Menu(msg().translate("menuItemMasterViewToolbarOrganiseAlbum"));
		Menu album = new Menu(bundle.getString("main-window-toolbar-album-submenu"));			//Translation
		album.setGraphic(IconLoader.loadIcon("Bearbeiten.png"));

//		MenuItem show = new MenuItem(msg().translate("menuItemMasterViewToolbarOrganiseAlbumDisplay"));
		MenuItem show = new MenuItem(bundle.getString("main-window-toolbar-album-sm-show"));			//Translation
		show.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					List<Album> albums = albumFacade.getAllAlbums();
					Album a = AlbumSelectionDialog.selectAlbum(msg(),
							"msgMasterViewAlbumSelecionDlgSelectAlbumToDisplay", albums, masterStage.get());
					if (a != null) {
						albumArea.get().selectAlbum(a);
					}
				} catch (FacadeException e) {
					msg().showExceptionMessage(e);
				}
			}
		});

//		MenuItem add = new MenuItem(msg().translate("menuItemMasterViewToolbarOrganiseAlbumCreate"));
		MenuItem add = new MenuItem(bundle.getString("main-window-toolbar-album-sm-create"));			//Translation
		add.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					Album album = AlbumCreationDialog.createAlbum(msg(), masterStage.get());
					if (album != null) {
						albumFacade.save(album);
						albumArea.get().getAlbums().add(new DBDataRefference<String>(album.getName(), album.getId()));
					}
				} catch (FacadeException e) {
					msg().showExceptionMessage(e);
				}
			}
		});

//		MenuItem del = new MenuItem(msg().translate("menuItemMasterViewToolbarOrganiseAlbumDelete"));
		MenuItem del = new MenuItem(bundle.getString("main-window-toolbar-album-sm-delete"));			//Translation
		del.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					List<Album> albums = albumFacade.getAllAlbums();
					Album a = AlbumSelectionDialog.selectAlbum(msg(),
							"msgMasterViewAlbumSelecionDlgSelectAlbumToDisplay", albums, masterStage.get());
					if (a != null) {
						if (ConfirmationDialog.requestConfirmation(msg(),
								"msgMasterViewToolbarViewDeleteAlbumConfirm")) {
							albumArea.get().removeAlbumWithId(a.getId());
							albumFacade.delete(a.getId());
						}
					}
				} catch (FacadeException e) {
					msg().showExceptionMessage(e);
				}
			}
		});
		album.getItems().addAll(show, add, del);
		return album;
	}

	private Menu buildOrganisePictureMenu() {
//		Menu picture = new Menu(msg().translate("menuItemMasterViewToolbarOrganisePicture"));
		Menu picture = new Menu(bundle.getString("main-window-toolbar-picture-submenu"));			//Translation
		

//		MenuItem show = new MenuItem(msg().translate("menuItemMasterViewToolbarOrganisePictureDisplay"));
		MenuItem show = new MenuItem(bundle.getString("main-window-toolbar-picture-sm-showpicture"));			//Translation
		show.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Picture pic = viewArea.get().getSelectedPicture().get();
				// TODO Actually showing the picture
			}
		});

//		MenuItem toAlbum = new MenuItem(msg().translate("menuItemMasterViewToolbarOrganisePictureToAlbum"));
		MenuItem toAlbum = new MenuItem(bundle.getString("main-window-toolbar-picture-sm-addtoalbum"));	

//		MenuItem del = new MenuItem(msg().translate("menuItemMasterViewToolbarOrganisePictureDelete"));
		MenuItem del = new MenuItem(bundle.getString("main-window-toolbar-picture-sm-deletepicture"));			//Translation
		del.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					Picture pic = viewArea.get().getSelectedPicture().get();
					if (ConfirmationDialog.requestConfirmation(msg(), "msgMasterViewToolbarViewDeletePictureConfirm")) {
						viewArea.get().getPictures().remove(pic);
						pictureFacade.delete(pic);
					}

				} catch (FacadeException e) {
					msg().showExceptionMessage(e);
				}
			}
		});

		picture.getItems().addAll(show, toAlbum, del);
		return picture;
	}

	private Menu buildImportMenu() {
//		Menu importM = new Menu(msg().translate("labelMasterViewToolbarImport"));
		Menu importM = new Menu(bundle.getString("main-window-toolbar-import-menu"));			//Translation
		importM.setGraphic(IconLoader.loadIcon("Import.png"));

//		MenuItem importPictures = new MenuItem(msg().translate("menuItemMasterViewToolbarImport"));
		MenuItem importPictures = new MenuItem(bundle.getString("main-window-toolbar-import-importpictures"));			//Translation
		importPictures.setOnAction(event -> {
			ImportPane importDialog = new ImportPane(masterStage.get(),
					Container.getActiveContainer().materialize(Messenger.class, Scope.APPLICATION));

			importDialog.show();
			// update the picture area
			viewArea.get().loadAllPictures();
		});
		importM.getItems().addAll(importPictures);
		return importM;
	}
}
