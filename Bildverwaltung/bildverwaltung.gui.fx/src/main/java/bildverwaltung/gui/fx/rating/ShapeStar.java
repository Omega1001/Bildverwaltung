package bildverwaltung.gui.fx.rating;

import javafx.scene.canvas.Canvas;
import javafx.scene.shape.Polygon;

public class ShapeStar extends Polygon {

	public ShapeStar(double scaleFactor) {
		super();
		Double [] points =
				{
					scaleFactor * 0.3683742171725, scaleFactor * 1.8029201988942,
					scaleFactor * 0.5896829303938, scaleFactor * 1.1142659619203,
					scaleFactor * 0.0031219842342, scaleFactor * 0.6909830056251,
					scaleFactor * 0.726459237171 , scaleFactor * 0.6886542369739,
					scaleFactor * 0.9477679503923, scaleFactor * 0.0000000000000,
					scaleFactor * 1.173506244815 , scaleFactor * 0.6872149787955,
					scaleFactor * 1.8968434977519, scaleFactor * 0.6848862101442,
					scaleFactor * 1.3130201833307, scaleFactor * 1.1119371932691,
					scaleFactor * 1.5387584777534, scaleFactor * 1.7991521720645,
					scaleFactor * 0.9521975315937, scaleFactor * 1.3758692157693
				};

		getPoints().addAll(points);
	}
	
}
