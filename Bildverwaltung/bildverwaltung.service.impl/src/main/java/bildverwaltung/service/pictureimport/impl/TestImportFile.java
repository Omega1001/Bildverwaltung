package bildverwaltung.service.pictureimport.impl;

import bildverwaltung.dao.exception.ServiceException;
import bildverwaltung.service.pictureimport.PictureImportService;

import java.io.File;

public class TestImportFile {

    public static void main(String [] args) {

        File test = new File("Art-gordon-greybg.jpg");
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
