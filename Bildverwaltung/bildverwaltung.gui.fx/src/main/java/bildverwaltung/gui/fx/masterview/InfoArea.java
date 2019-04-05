package bildverwaltung.gui.fx.masterview;

import java.util.function.Supplier;

import bildverwaltung.gui.fx.util.RebuildebleSubComponent;
import bildverwaltung.localisation.Messenger;
import javafx.scene.Node;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class InfoArea extends RebuildebleSubComponent {

	private AlbumArea albumArea;
	private SearchArea searchArea;
	private AttributeArea attributeArea;

	public InfoArea(Messenger msg, Supplier<PictureArea> viewArea) {
		super(msg);
		albumArea = new AlbumArea(msg, viewArea);
		searchArea = new SearchArea(msg, viewArea,()->albumArea);
		attributeArea = new AttributeArea(msg, viewArea);
	}

	@Override
	protected void rebuildSubComponents() {
		albumArea.rebuild();
		searchArea.rebuild();
		attributeArea.rebuild();
	}

	@Override
	protected Node build() {
		VBox b = new VBox(albumArea.getGraphic(), searchArea.getGraphic(), attributeArea.getGraphic());
		b.setMaxHeight(Double.MAX_VALUE);
		b.setMaxWidth(200d);
		b.setBorder(new Border(new BorderStroke(Color.BLACK, 
	            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		return b;
	}

	public AlbumArea getAlbumArea() {
		return albumArea;
	}

	public SearchArea getSearchArea() {
		return searchArea;
	}

	public AttributeArea getAttributeArea() {
		return attributeArea;
	}
	
	

}
