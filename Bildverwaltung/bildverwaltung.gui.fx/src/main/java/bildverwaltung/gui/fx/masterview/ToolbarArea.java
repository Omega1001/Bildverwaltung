package bildverwaltung.gui.fx.masterview;

import java.io.InputStream;
import java.util.List;
import java.util.function.Supplier;

import bildverwaltung.container.Container;
import bildverwaltung.dao.entity.Album;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.facade.AlbumFacade;
import bildverwaltung.gui.fx.masterview.dialogs.AlbumCreationDialog;
import bildverwaltung.gui.fx.masterview.dialogs.AlbumSelectionDialog;
import bildverwaltung.gui.fx.masterview.dialogs.PictureSelectionDialog;
import bildverwaltung.gui.fx.util.ConfirmationDialog;
import bildverwaltung.gui.fx.util.IconLoader;
import bildverwaltung.container.Container;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ToolbarArea extends RebuildebleSubComponent {

	private AlbumFacade albumFacade = Container.getActiveContainer().materialize(AlbumFacade.class);
	private Supplier<Stage> masterStage;
	private Supplier<PictureArea> viewArea;
	private Supplier<AlbumArea> albumArea;

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
		Menu file = new Menu(msg().translate("labelMasterViewToolbarFile"));
		file.setGraphic(IconLoader.loadIcon("Datei.png"));

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
		album.setGraphic(IconLoader.loadIcon("Bearbeiten.png"));

		MenuItem show = new MenuItem(msg().translate("menuItemMasterViewToolbarOrganiseAlbumDisplay"));
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

		MenuItem add = new MenuItem(msg().translate("menuItemMasterViewToolbarOrganiseAlbumCreate"));
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

		MenuItem del = new MenuItem(msg().translate("menuItemMasterViewToolbarOrganiseAlbumDelete"));
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
		Menu picture = new Menu(msg().translate("menuItemMasterViewToolbarOrganisePicture"));
		
		MenuItem show = new MenuItem(msg().translate("menuItemMasterViewToolbarOrganisePictureDisplay"));
		show.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public handle(ActionEvent event){
				List<Picture> list = pictureFacade.getImages();
				Picture pic = PictureSelecetionDialog.selectPicture(msg(),
					             "msgMasterViewPictureSelectionDialogSelectPictureToDisplay", picture);
				if(a != null){
					pictureArea.get()selectPicture(pic);
				} 
				catch (FacadeException e){
					msg().showExceptionMessage(e);
				}
			}
		});
		
		MenuItem toAlbum = new MenuItem(msg().translate("menuItemMasterViewToolbarOrganisePictureToAlbum"));
		
		MenuItem del = new MenuItem(msg().translate("menuItemMasterViewToolbarOrganisePictureDelete"));
		del.setOnAction(new EventHandler<ActionEvent>() {
		
			@Override
			public void handle(ActionEvent event) {
				try {
					List<Picture> pic = pictureFacade.getImages();
					Picture pic = PictureSelectionDialog.selectPicture(msg(),
								"msgMasterViewPictureSelectionDialogPictureToDisplay", picture);
					if(pic != null){
						if(ConfirmationDialog.requestConfirmation(msg(),
								"msgMasterViewToolbarViewDeletePictureConfirm")){
							pictureArea.get().removePicture(pic.getImage());
							pictureFacade.delete(pic.getImage());
						}
					}
				} catch(FacadeException e){
					msg().showexceptionMessage(e);
				}
			}
		});

		picture.getItems().addAll(show, toAlbum, del);
		return picture;
	}

	private Menu buildImportMenu() {
		Menu importM = new Menu(msg().translate("labelMasterViewToolbarImport"));
		importM.setGraphic(IconLoader.loadIcon("Import.png"));
		
		MenuItem importPictures = new MenuItem(msg().translate("menuItemMasterViewToolbarImport"));
		importPictures.setOnAction(event -> {
			ImportPane importDialog =
					new ImportPane(masterStage.get(), Container.getActiveContainer()
							.materialize(Messenger.class, Scope.APPLICATION));

			importDialog.show();
			// update the picture area
			viewArea.get().loadAllPictures();
		});
		importM.getItems().addAll(importPictures);
		return importM;
	}
}
