package bildverwaltung.service.pictureimport.impl;

import bildverwaltung.dao.exception.ServiceException;
import bildverwaltung.service.pictureimport.PictureImportService;

import java.io.File;
import java.net.URISyntaxException;

public class TestImportFile {

    public static void main(String [] args) throws URISyntaxException {

        File test = new File(PictureImportServiceImpl.class.getResource("/ex_pics/Art-gordon-greybg.jpg").toURI());
        PictureImportService testerino = new PictureImportServiceImpl();
        try {
            testerino.importPicture(test);
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (javax.persistence.PersistenceException e) {
            System.out.println("Nun tja, DB funktioniert noch nicht..." );
        }
    }

}
