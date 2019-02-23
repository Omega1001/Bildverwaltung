package gui.fx.attributeEditor;

import bildverwaltung.container.Container;
import bildverwaltung.container.Scope;
import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.facade.PictureFacade;
import bildverwaltung.gui.fx.attributeEditor.AttributeEditor;
import bildverwaltung.localisation.Messenger;
import bildverwaltung.localisation.MessengerImpl;
import bildverwaltung.localisation.TranslatorImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AttributeEditorTest extends Application {

	PictureFacade facade = Container.getActiveContainer().materialize(PictureFacade.class, Scope.APPLICATION);

    public void start(Stage primaryStage){
        Button bt = new Button("test");
        BorderPane bp = new BorderPane();
        bp.setCenter(bt);
        Scene sc = new Scene(bp);
                primaryStage.setScene(sc);

        bt.setOnAction(e->{
            Picture picture;
            try {
                picture = facade.getAllPictures().get(0);
            } catch (FacadeException e1) {
                e1.printStackTrace();
                picture = null;
            }

            Container.getActiveContainer().addFactory(MessengerImpl.FACTORY);
            Container.getActiveContainer().addFactory(TranslatorImpl.FACTORY);

            AttributeEditor st2 = new AttributeEditor(primaryStage, picture, Container.getActiveContainer().materialize(Messenger.class));
            st2.show();
        });

        primaryStage.show();
    }


    public static void main(String [] args){
        launch(args);
    }
}
