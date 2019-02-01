package bildverwaltung.gui.fx.masterview;

import java.util.ArrayList;
import java.util.List;

import bildverwaltung.container.Container;
import bildverwaltung.container.Scope;
import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.facade.PictureFacade;
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
import javafx.scene.layout.FlowPane;

public class PictureArea extends RebuildebleSubComponent {

	private PictureFacade pictureFacade = Container.getActiveContainer().materialize(PictureFacade.class,
			Scope.APPLICATION);
	private ObjectProperty<Picture> selectedPicture = new SimpleObjectProperty<>();
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
		ScrollPane scrollPane = new ScrollPane(pane);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);
		return scrollPane;
	}

	private List<ImageView> toView(List<? extends Picture> pictures) {
		List<ImageView> res = new ArrayList<>(pictures.size());
		for (Picture p : pictures) {
			try {
				ImageView view = new ImageView();
				Image i = new Image(pictureFacade.resolvePictureURI(p.getUri()));
				view.setImage(i);
				view.setFitWidth(200d);
				view.setFitHeight(200d);
				view.setPreserveRatio(true);
				view.addEventHandler(MouseEvent.ANY, new PictureMouseEventHandler(view));
				res.add(view);
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
			int index = pane.getChildren().indexOf(obj);
			if (MouseEvent.MOUSE_CLICKED.equals(event.getEventType())) {
				// Mouse Clicked
				if (MouseButton.PRIMARY.equals(event.getButton())) {
					selectedPicture.set(pictures.get(index));
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
				} else if (c.wasRemoved()) {
					pane.getChildren().remove(c.getFrom(), c.getTo());
				} else if (c.wasUpdated()) {
					for (int i = c.getFrom(); i < c.getTo(); i++) {
						Picture updated = c.getList().get(i);
						Node n = pane.getChildren().get(i);
						if (n instanceof ImageView) {
							ImageView v = (ImageView) n;
							try {
								v.setImage(new Image(pictureFacade.resolvePictureURI(updated.getUri())));
							} catch (FacadeException e) {
								msg().showExceptionMessage(e);
							}
						}
					}
				}
			}
		}

	}

}
