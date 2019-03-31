package bildverwaltung.gui.fx.attributeEditor;

import bildverwaltung.gui.fx.components.StarHolderPane;
import javafx.animation.ParallelTransition;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class ModifyingRatingBar extends GridPane {


	private StarHolderPane[] stars;
	private IntegerProperty selected = new SimpleIntegerProperty(0);
	private Byte currentRating;

	public ModifyingRatingBar(Byte rating) {
		super();
		stars = new StarHolderPane[5];
		for(int i=0;i<5;i++) {
			StarHolderPane p = new StarHolderPane(10);
			p.setOnMouseClicked(new RecoloringHandler(i));
			stars[i] = p;
			add(p, i,0);
		}

	}

	public Byte getCurrentRating() {
		return currentRating;
	}

	private class RecoloringHandler implements EventHandler<MouseEvent> {

		private final int index;

		public RecoloringHandler(int index) {
			this.index = index;
		}

		@Override
		public void handle(MouseEvent event) {
			ParallelTransition t = new ParallelTransition();
			updateRating((byte) (index+1));
		}

	}

	public void updateRating(Byte newRating) {
		currentRating = newRating;
		int i = 0;
		while(i < newRating) {
			stars[i].getChildren().clear();
			StarHolderPane p = new StarHolderPane(10);
			p.setOnMouseClicked(new RecoloringHandler(i));
			stars[i] = p;
			p.getStar().setFill(Color.YELLOW);
			add(p, i,0);
			i++;
		}

		while(i < 5) {
			StarHolderPane p = new StarHolderPane(10);
			p.setOnMouseClicked(new RecoloringHandler(i));
			stars[i] = p;
			p.getStar().setFill(Color.BLACK);
			add(p, i,0);
			i++;
		}

	}
}
