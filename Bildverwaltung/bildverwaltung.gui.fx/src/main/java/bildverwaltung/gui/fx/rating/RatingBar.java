package bildverwaltung.gui.fx.rating;

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

	private final StarHolderPane[] stars;
	private IntegerProperty selected = new SimpleIntegerProperty(0);

	public RatingBar(Byte rating) {
		super();
		stars = new StarHolderPane[5];
//		stars = new ShapeStar[maxRating];
//		for(int i=0;i<maxRating;i++) {
//			stars[i] = new ShapeStar(5);
//		}

// 	    for(int i=0;i<rating;i++) {
// 	    	StarHolderPane p = new StarHolderPane(10);
// 	    	//p.setOnMouseEntered(new RecoloringHandler(i));
// 	    	stars[i] = p;
// 	    	p.getStar().setFill(Color.YELLOW);
// 	    	add(p, i,0);
// 	    }

		updateRating(rating);

	}

	public void updateRating(Byte newRating) {
		int i = 0;
		while(i < newRating) {
			StarHolderPane p = new StarHolderPane(10);
			//p.setOnMouseEntered(new RecoloringHandler(i));
			stars[i] = p;
			p.getStar().setFill(Color.YELLOW);
			add(p, i,0);
			i++;
		}

		while(i < 5) {
			StarHolderPane p = new StarHolderPane(10);
			//p.setOnMouseEntered(new RecoloringHandler(i));
			stars[i] = p;
			p.getStar().setFill(Color.BLACK);
			add(p, i,0);
			i++;
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
				for (int i=selectedIndex.get();i<index;i++) {
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
		
		public StarHolderPane(double scaleFactor) {
			super();
			this.star = new ShapeStar(scaleFactor);
			getChildren().add(star);
		}

		public ShapeStar getStar() {
			return star;
		}
		
		
		
		
	}
	
	
}

