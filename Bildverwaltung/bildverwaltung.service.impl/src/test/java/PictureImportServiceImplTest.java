import bildverwaltung.dao.PictureDao;
import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.DaoException;
import bildverwaltung.dao.exception.ExceptionType;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.dao.exception.ServiceException;
import bildverwaltung.service.pictureimport.PictureImportService;
import bildverwaltung.service.pictureimport.impl.PictureImportServiceImpl;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;


import javax.persistence.EntityManager;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

public class PictureImportServiceImplTest {


    PictureDao daoMock = mock(PictureDao.class);
    PictureImportServiceImpl sut = new PictureImportServiceImpl(daoMock);

    // Delete the tmp file created by some tests
    @Before
    public void cleanFileTmp() throws URISyntaxException, IOException {
        File file = new File("PictureManager/tmp");
        if(file.exists()) {
            Files.delete(file.toPath());
      }
    }

    // Delete the "PictureManager" folder created by the tests with the content in it
    @AfterClass
    public static void cleanFolder() throws URISyntaxException, IOException {
        File folder = new File("PictureManager");
        if(folder.exists()) {
            String [] children = folder.list();

            for(String child: children) {
                new File(folder, child).delete();
            }
            folder.delete();
        }
    }


    @Test
    public void testCheckIfPicture() throws URISyntaxException {

        File testFile = new File(PictureImportServiceImpl.class.getResource("/ex_pics/Art-gordon-greybg.jpg").toURI());
        File testFileNotPicture = new File(PictureImportServiceImpl.class.getResource("/voll_das_picture_und_so").toURI());

        assertTrue(sut.isPicture(testFile));
        assertFalse(sut.isPicture(testFileNotPicture));

    }


    @Test
    public void testImportFile() throws URISyntaxException, ServiceException {

        File testFile = new File(PictureImportServiceImpl
                .class.getResource("/ex_pics/Art-gordon-greybg.jpg").toURI());

        Picture testPicture = sut.importPicture(testFile);

        testFile = new File(testPicture.getUri());
        assertTrue(testFile.exists());
        assertTrue(sut.isPicture(testFile));

    }

    @Test
    public void testImportFileNotPictureFail() throws URISyntaxException, ServiceException {
        File testFile = new File(PictureImportServiceImpl.class.getResource("/voll_das_picture_und_so").toURI());
        try {
            sut.importPicture(testFile);
        } catch (ServiceException e) {
            assertEquals(ExceptionType.NOT_A_PICTURE,e.getType());
        }
    }

    @Test
    public void testImportFileDuplicate() throws URISyntaxException {
        File testFile = new File(PictureImportServiceImpl
                .class.getResource("/voll_das_picture_und_so").toURI());

        try {
            sut.importPicture(testFile);
        } catch (ServiceException e) {
            // of course its expected, we need it
        }

        File testFile2 = new File(PictureImportServiceImpl
                .class.getResource("/ex_pics/Art-gordon-greybg.jpg").toURI());

        try {
            sut.importPicture(testFile2);
        } catch(ServiceException e) {
            assertEquals(ExceptionType.IMPORT_COPY_PIC_FAILED,e.getType());
        }
    }

    @Test
    public void testImportDBFail() throws URISyntaxException, DaoException,ServiceException,FacadeException{
        File testFile = new File(PictureImportService
                .class.getResource("/ex_pics/Art-gordon-greybg.jpg").toURI());

        when(daoMock.save(any(Picture.class)))
                .thenThrow(new DaoException(ExceptionType.IMPORT_SAVING_PIC_TO_DB_FAILED));

        try {
            sut.importPicture(testFile);
        } catch (ServiceException e) {
            assertEquals(ExceptionType.IMPORT_SAVING_PIC_TO_DB_FAILED,e.getType());
        }
    }

}
