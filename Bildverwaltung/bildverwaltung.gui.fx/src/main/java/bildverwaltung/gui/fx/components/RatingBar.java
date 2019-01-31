package bildverwaltung.gui.fx.components;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class RatingBar extends GridPane {

	private final int maxRating;
	private final StarHolderPane[] stars;
	private IntegerProperty selected = new SimpleIntegerProperty(0);
	
	public RatingBar(int maxRating) {
		super();
		this.maxRating = maxRating;
		stars = new StarHolderPane[maxRating];
//		stars = new ShapeStar[maxRating];
//		for(int i=0;i<maxRating;i++) {
//			stars[i] = new ShapeStar(5);
//		}
		for(int i=0;i<maxRating;i++) {
			StarHolderPane p = new StarHolderPane(5);
			p.setOnMouseEntered(new RecoloringHandler(i));
			stars[i] = p;
			add(p, i,0);
		}
		
	}
	private class RecoloringHandler implements EventHandler<MouseEvent>{

		private final int index;
		private final IntegerBinding selectedIndex = selected.subtract(1);
		
		public RecoloringHandler(int index) {
			this.index = index;
		}

		@Override
		public void handle(MouseEvent event) {
			ParallelTransition t = new ParallelTransition();
			if(selectedIndex.get() < index) {
				for (int i=selectedIndex.get();i<=index;i++) {
					t.getChildren().add(new FillTransition(new Duration(1000), stars[i].getStar(), Color.BLACK, Color.YELLOW));
				}
			}else if(selectedIndex.get() > index) {
				for (int i=index;i<selectedIndex.get();i++) {
					t.getChildren().add(new FillTransition(new Duration(1000), stars[i].getStar(), Color.YELLOW, Color.BLACK));
				}
			}else {
				return;
			}
			selected.set(index);
			t.playFromStart();
		}
		
	}
	private static class StarHolderPane extends Pane{
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
	
	
}

