package bildverwaltung.gui.fx.masterview;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
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
import bildverwaltung.gui.fx.util.RebuildebleSubComponent;
import bildverwaltung.localisation.Messenger;
import bildverwaltung.utils.DBDataRefference;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
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
		MenuBar mBar = new MenuBar(buildFileMenu(), buildOrganiseMenu(), buildImportMenu());
		return mBar;
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
			public void handle(ActionEvent event) {
				Picture pic = viewArea.get().getSelectedPicture().get();
				EnlargedPictureView enl = new EnlargedPictureView();
				enl.enlargePicture(new ImageView(viewArea.get().getSelectedPicture().get().getUri().toString()));
			}
		});

        MenuItem showAll = new MenuItem("showAll");
        showAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                albumArea.get().resetSelection();
                viewArea.get().loadAllPictures();
            }
        });

		MenuItem toAlbum = new MenuItem(msg().translate("menuItemMasterViewToolbarOrganisePictureToAlbum"));
		toAlbum.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
                try {
                    Picture pic = viewArea.get().getSelectedPicture().get();
                    Album alb= AlbumSelectionDialog.selectAlbum(msg(),"msgMasterViewAlbumSelecionDlgSelectAlbumToAdd",albumFacade.getAllAlbums(),masterStage.get());
                    List<Picture> li = alb.getPictures();
                    if(pic==null){
                        msg().showWarningMessage(msg().translate("editAttributesAlertPictureSelectionEmpty"), "");
                    }else {
                        if(!li.contains(pic)){
                            li.add(pic);
                            alb.setPictures(li);
                            albumFacade.save(alb);
                        }
                    }

                } catch (FacadeException e) {
                    e.printStackTrace();
                }
            }
		});

		MenuItem editAttributes = new MenuItem(msg().translate("menuItemMasterViewToolbarOrganisePictureEditAttributes"));
		editAttributes.setOnAction(actionEvent -> {
			Picture pic = viewArea.get().getSelectedPicture().get();

			if(pic == null) {
				msg().showWarningMessage(msg().translate("editAttributesAlertPictureSelectionEmpty"), "");
			} else {
                AttributeEditor editor = new AttributeEditor(masterStage.get(), pic, msg());
                editor.show();
                viewArea.get().loadAllPictures();
			}
		});

		MenuItem del = new MenuItem(msg().translate("menuItemMasterViewToolbarOrganisePictureDelete"));
		del.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					Picture pic = viewArea.get().getSelectedPicture().get();
					if (ConfirmationDialog.requestConfirmation(msg(), "msgMasterViewToolbarViewDeletePictureConfirm")) {
						//viewArea.get().getPictures().remove(pic);
						pictureFacade.delete(pic);
						viewArea.get().loadAllPictures();

						File picFile =new File(pic.getUri());
						if(picFile.exists()){
							try {
								Files.delete(picFile.toPath());
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}

				} catch (FacadeException e) {
					msg().showExceptionMessage(e);
				}
			}
		});

		picture.getItems().addAll(show, toAlbum, editAttributes, del,showAll);
		return picture;
	}

	private Menu buildImportMenu() {
		Menu importM = new Menu(msg().translate("labelMasterViewToolbarImport"));
		importM.setGraphic(IconLoader.loadIcon("Import.png"));

		MenuItem importPictures = new MenuItem(msg().translate("menuItemMasterViewToolbarImport"));
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
