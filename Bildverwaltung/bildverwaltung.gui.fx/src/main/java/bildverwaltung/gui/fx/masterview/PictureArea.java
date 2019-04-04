package bildverwaltung.gui.fx.masterview;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import javafx.beans.property.ReadOnlyProperty;
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
import javafx.scene.paint.Color;

public class PictureArea extends RebuildebleSubComponent {
	private static final Logger LOG = LoggerFactory.getLogger(PictureArea.class);

	private static Border selectionBorder = new Border(
			new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, new CornerRadii(4), new BorderWidths(2)));

	private AlbumFacade albumFacade = Container.getActiveContainer().materialize(AlbumFacade.class, Scope.APPLICATION);
	private PictureFacade pictureFacade = Container.getActiveContainer().materialize(PictureFacade.class,
			Scope.APPLICATION);
	// private ObjectProperty<Picture> selectedPicture = new
	// SimpleObjectProperty<>();
	private SelectionModdel selected = new SelectionModdel();
	private ObservableList<Picture> pictures = FXCollections.observableArrayList();
	private FlowPane pane = new FlowPane();

	public PictureArea(Messenger msg) {
		super(msg);
	}

	@Override
	protected Node build() {
		pane.getChildren().clear();
		pane.getChildren().addAll(toView(pictures));
		pictures.addListener(new SyncroHandler());
		selected.selectedPictures.addListener(new SelectionSyncroHandler());
		ScrollPane scrollPane = new ScrollPane(pane);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);

		return scrollPane;
	}

	private List<Node> toView(List<? extends Picture> pictures) {
		List<Node> res = new ArrayList<>(pictures.size());
		for (Picture p : pictures) {
			ImageView view = new ImageView();
			view.setFitWidth(196d);
			view.setFitHeight(196d);
			view.setPreserveRatio(true);
			StackPane border = new StackPane();

			border.setMinHeight(200d);
			border.setMaxHeight(200d);
			border.setMinWidth(200d);
			border.setMaxWidth(200d);

			border.getChildren().add(view);
			border.addEventHandler(MouseEvent.ANY, new PictureMouseEventHandler(border));
			res.add(border);
			try {
				InputStream is = pictureFacade.resolvePictureURI(p.getUri());
				Image i = new Image(is, 196d, 196d, true, true);
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
		return selected.lastSelectedPicture;
	}

	public SelectionModdel getSelected() {
		return selected;
	}

	public ObservableList<Picture> getPictures() {
		return pictures;
	}

	public void startFullScreenView() {
		PictureIterator it = null;
		if (selected.selectedPictures.size() == 1) {
			it = new PictureIterator(pictures, pictures.indexOf(selected.lastSelectedPicture.get()));
		} else {
			List<Picture> pics = new LinkedList<>(selected.selectedPictures);
			Collections.sort(pics, (p1, p2) -> {
				return String.CASE_INSENSITIVE_ORDER.compare(p1.getName(), p2.getName());
			});
			it = new PictureIterator(pics, pics.indexOf(selected.lastSelectedPicture.get()));
		}

		EnlargedPictureView epv = new EnlargedPictureView(
				Container.getActiveContainer().materialize(Messenger.class, Scope.APPLICATION));
		epv.showEnlargedPicture(it);
	}

	private class PictureMouseEventHandler implements EventHandler<MouseEvent> {

		private final Node obj;

		public PictureMouseEventHandler(Node obj) {
			super();
			this.obj = obj;
		}

		@Override
		public void handle(MouseEvent event) {
			int index = pane.getChildren().indexOf(obj);
			if (MouseEvent.MOUSE_CLICKED.equals(event.getEventType())) {
				// Mouse Clicked
				if (MouseButton.PRIMARY.equals(event.getButton())) {
					if (event.isShiftDown()) {
						doRangeSelect(index);
					} else if (event.isControlDown()) {
						doAddSelect(index);

					} else {
						doSelect(index);
					}
				}
				if (event.getClickCount() == 2) {
					startFullScreenView();
				}
			}

		}

		private void doSelect(int index) {
			Picture p = pictures.get(index);
			selected.lastSelectedPicture.set(p);
			selected.selectedPictures.clear();
			selected.selectedPictures.add(p);
		}

		private void doAddSelect(int index) {
			Picture p = pictures.get(index);
			int i = selected.selectedPictures.indexOf(p);
			if (i != -1) {
				selected.selectedPictures.remove(i);
			} else {
				selected.selectedPictures.add(p);
			}
			selected.lastSelectedPicture.set(p);
		}

		private void doRangeSelect(int toIndex) {
			Picture lastSeleced = selected.lastSelectedPicture.get();
			if (lastSeleced == null) {
				// No start detected, do standard select
				doSelect(toIndex);
			} else {
				int startIndex = pictures.indexOf(lastSeleced);
				if (toIndex == startIndex) {
					// Single Select operation
					doAddSelect(toIndex);
				} else {
					// Multiselect operation
					List<Picture> selection = null;
					if (toIndex < startIndex) {
						selection = pictures.subList(toIndex, startIndex);
					} else {
						// Shift 1 right to match first exclusive, last inclusive
						selection = pictures.subList(startIndex + 1, toIndex + 1);
					}
					boolean remove = false;
					for (Picture p : selection) {
						if (selected.selectedPictures.contains(p)) {
							remove = true;
							break;
						}
					}
					if (remove) {
						selected.selectedPictures.removeAll(selection);
					} else {
						selected.selectedPictures.addAll(selection);
					}
					selected.lastSelectedPicture.set(pictures.get(toIndex));
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
					// Workaround for FX Bug, range remove does crazy stuff if used to delete a
					// single element
					if (c.getRemovedSize() == 1) {
						pane.getChildren().remove(c.getFrom());
					} else if (c.getRemovedSize() > 1) {
						pane.getChildren().remove(c.getFrom(), c.getRemovedSize() + c.getFrom());
					}
				} else if (c.wasUpdated()) {
					for (int i = c.getFrom(); i < c.getTo(); i++) {
						Picture updated = c.getList().get(i);
						ImageView view = resolveViewForIndex(i);
						reloadPicture(updated, view);
					}
				}
			}
		}

		private ImageView resolveViewForIndex(int i) {
			Node n = pane.getChildren().get(i);
			if (n instanceof StackPane) {
				StackPane pane = (StackPane) n;
				if (!pane.getChildren().isEmpty()) {
					Node n2 = pane.getChildren().get(0);
					if (n2 != null && n2 instanceof ImageView) {
						return (ImageView) n2;
					}
				}
			}
			return null;
		}

		private void reloadPicture(Picture updatedPicture, ImageView view) {
			if (updatedPicture == null || view == null) {
				throw new IllegalArgumentException("Cannot update Picture");
			}
			InputStream is = null;
			try {
				is = pictureFacade.resolvePictureURI(updatedPicture.getUri());
				view.setImage(new Image(is));
			} catch (FacadeException e) {
				msg().showExceptionMessage(e);
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					LOG.error("Unable to close Stream after loading Picture : ", e);
				}
			}

		}

	}

	private class SelectionSyncroHandler implements ListChangeListener<Picture> {

		@Override
		public void onChanged(Change<? extends Picture> c) {
			while (c.next()) {
				List<? extends Picture> changed = null;
				boolean add = true;
				if (c.wasAdded()) {
					changed = c.getAddedSubList();
				} else if (c.wasRemoved() && c.getRemovedSize() > 0) {
					add = false;
					changed = c.getRemoved();
				}
				for (Picture p : changed) {
					int index = pictures.indexOf(p);
					if (index != -1) {
						turnBorder(pane.getChildren().get(index), add);
					}
				}
			}
		}

		private void turnBorder(Node node, boolean showBorder) {
			if (node instanceof StackPane) {
				StackPane view = (StackPane) node;
				view.setBorder(showBorder ? selectionBorder : null);
			}
		}

	}

	public boolean loadAllPictures() {
		try {
			List<Picture> pics = pictureFacade.getAllPictures();
			pictures.clear();
			pictures.addAll(pics);
			if (!pictures.contains(selected.lastSelectedPicture.getValue())) {
				selected.lastSelectedPicture.set(null);
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
			pictures.addAll(album.getPictures());
			if (!pictures.contains(selected.lastSelectedPicture.getValue())) {
				selected.lastSelectedPicture.set(null);
			}
			return true;
		} catch (FacadeException e) {
			msg().showExceptionMessage(e);
			return false;
		}
	}

	public static final class SelectionModdel {
		private ObjectProperty<Picture> lastSelectedPicture = new SimpleObjectProperty<>();
		private ObservableList<Picture> selectedPictures = FXCollections.observableArrayList();
		private ObservableList<Picture> readOnlyPictures = FXCollections.unmodifiableObservableList(selectedPictures);

		public SelectionModdel() {
			lastSelectedPicture.addListener((b, o, n) -> {
				if (n == null)
					selectedPictures.clear();
			});
		}

		public ReadOnlyProperty<Picture> getLastSelectedPicture() {
			return lastSelectedPicture;
		}

		public ObservableList<Picture> getSelectedPictures() {
			return readOnlyPictures;
		}

		public void clearSelection() {
			lastSelectedPicture.set(null);
		}

	}

}
