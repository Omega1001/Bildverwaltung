package bildverwaltung.gui.fx.enlargedpicture;

import bildverwaltung.container.Container;
import bildverwaltung.container.Scope;
import bildverwaltung.gui.fx.util.IconLoader;
import bildverwaltung.gui.fx.util.RebuildebleSubComponent;
import bildverwaltung.localisation.Messenger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class EnlargedPictureToolBar extends RebuildebleSubComponent{

	private Messenger msg = Container.getActiveContainer().materialize(Messenger.class, Scope.APPLICATION);


	private final String LAST_PIC = msg().translate("buttonEnlargedPictureToolBarLastPicture");
	private final String FULL_PIC = msg().translate("buttonEnlargedPictureToolBarFullPicture");
	private final String NEXT_PIC = msg().translate("buttonEnlargedPictureToolBarNextPicture");
	private final String DIASHOW_PIC = msg().translate("buttonEnlargedPictureToolBarDiashow");

	private static final double BUTTON_SIZE = 40.0;

	public EnlargedPictureToolBar(Messenger msg, Stage newWindow) {
		super(msg);
	}

	@Override
	protected Node build() {
		HBox toolBarBottom = new HBox();

		//show last Picture
		Button buttonBack = new Button(msg().translate("buttonEnlargedPictureToolBarLastPicture"));
		buttonBack.setGraphic(IconLoader.loadIcon("Zurueck.png"));
		showButtonWithFunction(buttonBack, LAST_PIC, BUTTON_SIZE);
		buttonBack.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
			}
		});

		//show Picture in FullScreen Modus
		Button buttonFullScreen = new Button (msg().translate("buttonEnlargedPictureToolBarNextPicture"));
		buttonFullScreen.setGraphic(IconLoader.loadIcon("Grossansicht.png"));
		showButtonWithFunction(buttonFullScreen, FULL_PIC, BUTTON_SIZE);
		buttonFullScreen.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				//TODO
			}
		});

		// show the next Picture
		Button buttonNext = new Button ();
		buttonNext.setGraphic(IconLoader.loadIcon("Weiter.png"));
		showButtonWithFunction(buttonNext, NEXT_PIC, BUTTON_SIZE);
		buttonNext.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				//TODO
			}});
		
		// Starts diashow
		// TODO BLOCK NEEDS FCKN RESOURCE STINRGS
			Button buttonDiashow = new Button (msg().translate("buttonEnlargedPictureToolBarDiashow"));
			buttonDiashow.setGraphic(IconLoader.loadIcon("Weiter.png"));
			showButtonWithFunction(buttonDiashow, DIASHOW_PIC, BUTTON_SIZE);
			buttonDiashow.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						//TODO
		}});


		HBox buttonBox = new HBox();
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.getChildren().addAll(buttonBack,buttonNext,buttonFullScreen);

		//toolBarBottom.getItems().addAll(buttonBack,buttonNext,buttonFullScreen);
		toolBarBottom.getChildren().addAll(buttonBox);
		toolBarBottom.setAlignment(Pos.CENTER);

		return toolBarBottom;
	}

	private static void showButtonWithFunction(Button btn, String viewText, double size){
		btn.setMinWidth(size);
	    btn.setPrefWidth(size);
	    btn.setMaxWidth(size);

		Tooltip toolTip = new Tooltip(viewText);
	    btn.setTooltip(toolTip);
	    toolTip.setTextAlignment(TextAlignment.RIGHT);
	}

}
