package bildverwaltung.gui.fx.masterview;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import bildverwaltung.container.Container;
import bildverwaltung.dao.entity.Album;
import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.facade.AlbumFacade;
import bildverwaltung.facade.PictureFacade;
import bildverwaltung.gui.fx.attributeEditor.AttributeEditor;
import bildverwaltung.gui.fx.enlargedpicture.EnlargedPictureView;
import bildverwaltung.gui.fx.masterview.dialogs.AlbumCreationDialog;
import bildverwaltung.gui.fx.masterview.dialogs.AlbumSelectionDialog;
import bildverwaltung.gui.fx.util.ConfirmationDialog;
import bildverwaltung.gui.fx.util.IconLoader;
import bildverwaltung.container.Scope;
import bildverwaltung.gui.fx.importdialog.ImportPane;
import bildverwaltung.gui.fx.util.PictureIterator;
import bildverwaltung.gui.fx.util.RebuildebleSubComponent;
import bildverwaltung.localisation.Messenger;
import bildverwaltung.utils.DBDataRefference;
import javafx.collections.ListChangeListener;
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

	public ToolbarArea(Messenger msg, Supplier<Stage> masterStage, Supplier<PictureArea> viewArea,
			Supplier<AlbumArea> albumArea) {
		super(msg);
		this.masterStage = masterStage;
		this.viewArea = viewArea;
		this.albumArea = albumArea;
	}

	@Override
	protected Node build() {
		return new MenuBar(buildFileMenu(), buildOrganiseMenu(), buildImportMenu());
	}

	private Menu buildFileMenu() {
		Menu file = new Menu(msg().translate("labelMasterViewToolbarFile"));
		file.setGraphic(IconLoader.loadIcon("Datei.png"));

		MenuItem quit = new MenuItem(msg().translate("labelMasterViewToolbarFileQuit"));
		quit.setGraphic(IconLoader.loadIcon("Exit.png"));
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
		organise.setGraphic(IconLoader.loadIcon("Bearbeiten.png"));
		Menu album = buildOrganiseAlbumMenu();
		Menu picture = buildOrganisePictureMenu();
		organise.getItems().addAll(album, picture);
		return organise;
	}

	private Menu buildOrganiseAlbumMenu() {
		Menu album = new Menu(msg().translate("menuItemMasterViewToolbarOrganiseAlbum"));
		// album.setGraphic(IconLoader.loadIcon("Bearbeiten.png"));

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
						albumArea.get().getAlbums().add(new DBDataRefference<>(album.getName(), album.getId()));
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
			public void handle(ActionEvent event) {
				EnlargedPictureView enl = new EnlargedPictureView(
						Container.getActiveContainer().materialize(Messenger.class, Scope.APPLICATION));
				Picture selected = viewArea.get().getSelectedPicture().get();
				PictureIterator it = new PictureIterator(viewArea.get().getPictures(),
						viewArea.get().getPictures().indexOf(selected));
				enl.showEnlargedPicture(it);
			}
		});

		MenuItem toAlbum = new MenuItem(msg().translate("menuItemMasterViewToolbarOrganisePictureToAlbum"));
		toAlbum.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					Picture pic = viewArea.get().getSelectedPicture().get();
					Album alb = AlbumSelectionDialog.selectAlbum(msg(), "msgMasterViewAlbumSelecionDlgSelectAlbumToAdd",
							albumFacade.getAllAlbums(), masterStage.get());
					if (alb != null) {
						if (!alb.getPictures().contains(pic)) {
							alb.getPictures().add(pic);
						}
						albumFacade.save(alb);
					}
					pictureFacade.refresh(pic);
				} catch (FacadeException e) {
					e.printStackTrace();
				}
			}
		});

		MenuItem removeFromAlbum = new MenuItem(
				msg().translate("menuItemMasterViewToolbarOrganisePictureRemoveFromAlbum"));
		removeFromAlbum.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					Picture pic = viewArea.get().getSelectedPicture().get();
					UUID albId = albumArea.get().getSelectedAlbumId();
					if (albId != null) {
						Album alb = albumFacade.getAlbumById(albId);
						alb.getPictures().remove(pic);
						albumFacade.save(alb);
						viewArea.get().getPictures().remove(pic);
						viewArea.get().getSelectedPicture().set(null);
					} else if (!pic.getAlben().isEmpty()) {
						Album alb = AlbumSelectionDialog.selectAlbum(msg(),
								"msgMasterViewAlbumSelecionDlgSelectAlbumToRemovePicture", pic.getAlben(),
								masterStage.get());
						// need to get persistent album from DB
						alb = albumFacade.getAlbumById(alb.getId());
						if (alb != null) {
							alb.getPictures().remove(pic);
							albumFacade.save(alb);
						}
					} else {
						msg().showErrorMessage("msgMasterViewToolbarOrganisePictureRemoveFromAlbumNotInAlbum", null);
					}
					pictureFacade.refresh(pic);
				} catch (FacadeException e) {
					e.printStackTrace();
				}
			}
		});

		MenuItem editAttributes = new MenuItem(
				msg().translate("menuItemMasterViewToolbarOrganisePictureEditAttributes"));
		editAttributes.setOnAction(actionEvent -> {
			// Collect Data
			Picture pic = viewArea.get().getSelectedPicture().get();
			int selectedPictureIndex = viewArea.get().getPictures().indexOf(pic);
			// Do edit
			AttributeEditor editor = new AttributeEditor(masterStage.get(), pic, msg());
			editor.show();
			// Write Back
			try {
				pic = pictureFacade.save(pic);
			} catch (FacadeException e) {
				msg().showExceptionMessage(e);
			}
			viewArea.get().getPictures().set(selectedPictureIndex, pic);
			viewArea.get().getSelectedPicture().set(pic);

		});

		MenuItem del = new MenuItem(msg().translate("menuItemMasterViewToolbarOrganisePictureDelete"));
		del.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				// delete picture from db and from the hard drive
				List<Picture> pics = viewArea.get().getSelected().getSelectedPictures();
				if (!pics.isEmpty()) {
					try {
						if (ConfirmationDialog.requestConfirmation(msg(),
								pics.size() == 1 ? "msgMasterViewToolbarViewDeletePictureConfirm"
										: "msgMasterViewToolbarViewDeletePictureMultipleConfirm")) {
							for (Picture p : pics) {
								pictureFacade.delete(p);
								viewArea.get().getPictures().remove(p);
							}
						}

					} catch (FacadeException e) {
						msg().showExceptionMessage(e);
					}
				}else {
					msg().showInfoMessage("msgMasterViewToolbarViewDeletePictureNothingSelected", null);
				}
			}
		});

		editAttributes.setDisable(true);
		toAlbum.setDisable(true);
		del.setDisable(true);
		show.setDisable(true);
		removeFromAlbum.setDisable(true);
		viewArea.get().getSelected().getSelectedPictures().addListener(new ListChangeListener<Picture>() {

			@Override
			public void onChanged(Change<? extends Picture> c) {
				boolean hasNoItens = c.getList().isEmpty();
				editAttributes.setDisable(hasNoItens);
				toAlbum.setDisable(hasNoItens);
				del.setDisable(hasNoItens);
				show.setDisable(hasNoItens);
				removeFromAlbum.setDisable(hasNoItens);
			}
			
		});

		picture.getItems().addAll(show, toAlbum, removeFromAlbum, editAttributes, del);
		return picture;
	}

	private Menu buildImportMenu() {
		Menu importM = new Menu(msg().translate("labelMasterViewToolbarImport"));
		importM.setGraphic(IconLoader.loadIcon("Import.png"));

		MenuItem importPictures = new MenuItem(msg().translate("menuItemMasterViewToolbarImport"));

		importPictures.setOnAction(event -> {
			List<Picture> importedPictures;
			ImportPane importDialog = new ImportPane(masterStage.get(),
					Container.getActiveContainer().materialize(Messenger.class, Scope.APPLICATION));

			importedPictures = importDialog.show();
			if (albumArea.get().getSelectedAlbumId() != null) {
				albumArea.get().resetSelection();
			} else {
				viewArea.get().getPictures().addAll(importedPictures);
			}

		});
		importM.getItems().addAll(importPictures);
		return importM;
	}
}
