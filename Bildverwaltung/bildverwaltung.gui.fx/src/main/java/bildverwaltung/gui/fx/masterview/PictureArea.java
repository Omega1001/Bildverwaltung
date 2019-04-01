package bildverwaltung.gui.fx.masterview;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import bildverwaltung.container.Container;
import bildverwaltung.container.Scope;
import bildverwaltung.dao.entity.Album;
import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.facade.AlbumFacade;
import bildverwaltung.facade.PictureFacade;
import bildverwaltung.gui.fx.enlargedpicture.EnlargedPictureView;
import bildverwaltung.gui.fx.util.PictureIterator;
import bildverwaltung.gui.fx.util.RebuildebleSubComponent;
import bildverwaltung.localisation.Messenger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

public class PictureArea extends RebuildebleSubComponent {
	private AlbumFacade albumFacade = Container.getActiveContainer().materialize(AlbumFacade.class, Scope.APPLICATION);
	private PictureFacade pictureFacade = Container.getActiveContainer().materialize(PictureFacade.class,
			Scope.APPLICATION);
	private ObjectProperty<Picture> selectedPicture = new SimpleObjectProperty<>();
	private ObservableList<Picture> pictures = FXCollections.observableArrayList();
	private List<ImageView> actualImageViews = new ArrayList<>();
	private FlowPane pane = new FlowPane();

	public PictureArea(Messenger msg) {
		super(msg);
	}

	@Override
	protected Node build() {
		pane.getChildren().clear();
		pane.getChildren().addAll(toView(pictures));
		pictures.addListener(new SyncroHandler());
		ScrollPane scrollPane = new ScrollPane(pane);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);

		return scrollPane;
	}

	private List<Pane> toView(List<? extends Picture> pictures) {
		List<Pane> res = new ArrayList<>(pictures.size());
		for (Picture p : pictures) {
			ImageView view = new ImageView();
			view.setFitWidth(200d);
			view.setFitHeight(200d);
			view.setPreserveRatio(true);
			view.addEventHandler(MouseEvent.ANY, new PictureMouseEventHandler(view));

			// Create a new BorderPane to enforce certain limitations to the picture and its space
			BorderPane bp = new BorderPane();

			// one sigle picture Pane will always be 200x200 px
			bp.setMinHeight(200d);
			bp.setMaxHeight(200d);
			bp.setMinWidth(200d);
			bp.setMaxWidth(200d);
			bp.setStyle("-fx-border-color: black");

			bp.addEventHandler(MouseEvent.ANY, new PictureMouseEventHandler(view));
			bp.setCenter(view);
			res.add(bp);

			actualImageViews.add(view);

			try {
				InputStream is = pictureFacade.resolvePictureURI(p.getUri());
				Image i = new Image(is, 200d, 200d, true, true);
				view.setImage(i);
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FacadeException e) {
				msg().showExceptionMessage(e);
			}
		}
		return res;
	}

	public ObjectProperty<Picture> getSelectedPicture() {
		return selectedPicture;
	}

	public ObservableList<Picture> getPictures() {
		return pictures;
	}

	private class PictureMouseEventHandler implements EventHandler<MouseEvent> {

		private final ImageView obj;

		public PictureMouseEventHandler(ImageView obj) {
			super();
			this.obj = obj;
		}

		@Override
		public void handle(MouseEvent event) {
			int index = actualImageViews.indexOf(obj);
			if (MouseEvent.MOUSE_CLICKED.equals(event.getEventType())) {
				// Mouse Clicked
				if (MouseButton.PRIMARY.equals(event.getButton())) {
					selectedPicture.set(pictures.get(index));
				}
				if (event.getClickCount() == 2) {
					PictureIterator it = new PictureIterator(pictures, index);
					EnlargedPictureView epv = new EnlargedPictureView(
							Container.getActiveContainer().materialize(Messenger.class, Scope.APPLICATION));
					epv.showEnlargedPicture(it);
				}
			}

		}

	}

	private class SyncroHandler implements ListChangeListener<Picture> {

		@Override
		public void onChanged(Change<? extends Picture> c) {
			while (c.next()) {
				if (c.wasAdded()) {
					List<? extends Picture> added = c.getAddedSubList();
					pane.getChildren().addAll(c.getFrom(), toView(added));
				} else if (c.wasRemoved() && c.getRemovedSize() > 0) {
					if (c.getRemovedSize() == 1) {
						pane.getChildren().remove(c.getFrom());
						actualImageViews.remove(c.getFrom());
					} else if (c.getRemovedSize() > 1) {
						pane.getChildren().remove(c.getFrom(), c.getRemovedSize() - c.getFrom());
					}
				} else if (c.wasUpdated()) {
					for (int i = c.getFrom(); i < c.getTo(); i++) {
						Picture updated = c.getList().get(i);
						Node n = pane.getChildren().get(i);
						if (n instanceof ImageView) {
							ImageView v = (ImageView) n;
							try {
								InputStream is = pictureFacade.resolvePictureURI(updated.getUri());
								v.setImage(new Image(is));
								try {
									is.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							} catch (FacadeException e) {
								msg().showExceptionMessage(e);
							}
						}
					}
				}
			}
		}

	}

	public List<Pane> getList() {
		return toView(pictures);
	}

	public boolean loadAllPictures() {
		try {
			List<Picture> pics = pictureFacade.getAllPictures();
			pictures.clear();
			actualImageViews.clear();
			pictures.addAll(pics);
			if (!pictures.contains(selectedPicture.getValue())) {
				selectedPicture.set(null);
			}
			return true;
		} catch (FacadeException e) {
			msg().showExceptionMessage(e);
			return false;
		}
	}

	public boolean loadAlbumById(UUID albumId) {
		try {
			Album album = albumFacade.getAlbumById(albumId);
			pictures.clear();
			actualImageViews.clear();
			pictures.addAll(album.getPictures());
			if (!pictures.contains(selectedPicture.getValue())) {
				selectedPicture.set(null);
			}
			return true;
		} catch (FacadeException e) {
			msg().showExceptionMessage(e);
			return false;
		}
	}

}
