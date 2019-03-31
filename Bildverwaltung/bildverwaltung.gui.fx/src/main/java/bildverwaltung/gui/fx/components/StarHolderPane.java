package bildverwaltung.gui.fx.components;

import javafx.scene.layout.Pane;

public class StarHolderPane extends Pane {
		private final ShapeStar star;

		public StarHolderPane(int scaleFactor) {
			super();
			this.star = new ShapeStar(scaleFactor);
			getChildren().add(star);
		}

		public ShapeStar getStar() {
			return star;
		}
}

