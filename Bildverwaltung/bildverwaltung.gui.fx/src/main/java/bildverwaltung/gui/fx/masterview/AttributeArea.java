package bildverwaltung.gui.fx.masterview;

import java.util.function.Supplier;

import bildverwaltung.dao.entity.Picture;
import bildverwaltung.gui.fx.rating.RatingBar;
import bildverwaltung.gui.fx.util.RebuildebleSubComponent;
import bildverwaltung.localisation.Messenger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class AttributeArea extends RebuildebleSubComponent {

	private Supplier<PictureArea> viewArea;
	
	public AttributeArea(Messenger msg,Supplier<PictureArea> viewArea) {
		super(msg);
		this.viewArea = viewArea;
	}

	@Override
	protected Node build() {
		TitledPane title = new TitledPane();
		title.setText(msg().translate("headerTextMasterViewInfoAreaAttributeAreaHeader"));
		GridPane grid = new GridPane();
		Label name = new Label();
		Label height = new Label();
		Label width = new Label();
		//HBox ratingBar = new HBox();
		Pane rating = new RatingBar((byte) 0);


		grid.add(new Label(msg().translate("labelMasterViewInfoAreaAttributeAreaPictureName")), 0, 0);
		grid.add(name, 1, 0);
		grid.add(new Label(msg().translate("labelMasterViewInfoAreaAttributeAreaPictureHeight")), 0, 1);
		grid.add(height, 1, 1);
		grid.add(new Label(msg().translate("labelMasterViewInfoAreaAttributeAreaPictureWidth")), 0, 2);
		grid.add(width, 1, 2);
		grid.add(new Label(msg().translate("labelMasterViewInfoAreaAttributeAreaPictureRating")), 0, 3);
		grid.add(rating, 1, 3);
		
		ChangeListener<Picture> changeHandler = new ChangeListener<Picture>() {

			@Override
			public void changed(ObservableValue<? extends Picture> observable, Picture oldValue, Picture newValue) {
				if(newValue != null) {
					name.setText(newValue.getName());
					height.setText(String.valueOf(newValue.getHeigth()));
					width.setText(String.valueOf(newValue.getWidth()));
					((RatingBar) rating).updateRating(Byte.valueOf(newValue.getRating()));
				}else {
					name.setText("");
					height.setText("");
					width.setText("");
					((RatingBar) rating).updateRating((byte) 0);
				}
			}
		};
		ObjectProperty<Picture> selected = viewArea.get().getSelectedPicture();
		changeHandler.changed(selected, null, selected.getValue());
		selected.addListener(changeHandler);
		
		title.setContent(grid);
		title.setMaxHeight(Double.MAX_VALUE);
		return title;
	}

}
