package bildverwaltung.gui.fx.components;

import javafx.scene.shape.Polygon;

public class ShapeStar extends Polygon {

	public ShapeStar(double scaleFactor) {
		super();
		getPoints().addAll(0*scaleFactor,0*scaleFactor,50*scaleFactor,25*scaleFactor,0*scaleFactor,50*scaleFactor);
	}
	
}
