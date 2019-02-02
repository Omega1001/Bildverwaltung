package bildverwaltung.service.pictureimport.impl;

import bildverwaltung.container.Container;
import bildverwaltung.container.Scope;
import bildverwaltung.dao.exception.ServiceException;
import bildverwaltung.facade.PictureFacade;
import bildverwaltung.service.pictureimport.PictureImportService;

import java.io.File;
import java.net.URISyntaxException;

public class TestImportFile {




    /*
        Geht glaub ich nicht mehr, idgaf

        fix kommt iwann
     */




    public static void main(String [] args) throws URISyntaxException {
        File test = new File(PictureImportServiceImpl.class.getResource("/ex_pics/Art-gordon-greybg.jpg").toURI());
        PictureImportService testerino = new PictureImportServiceImpl(Container.getActiveContainer().materialize(PictureFacade.class, Scope.DEFAULT));
        try {
            testerino.importPicture(test);
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (javax.persistence.PersistenceException e) {
            System.out.println("Nun tja, DB funktioniert noch nicht..." );
        }
    }


}
