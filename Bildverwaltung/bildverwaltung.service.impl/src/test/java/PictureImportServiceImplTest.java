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

import bildverwaltung.utils.ApplicationIni;
import bildverwaltung.utils.IniFile;
import bildverwaltung.utils.IniFileReader;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.persistence.EntityManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class PictureImportServiceImplTest {

    private final static String PICTURES_PATH = "target/test/resources/PictureManager";

    PictureDao daoMock = mock(PictureDao.class);

    PictureImportServiceImpl sut = new PictureImportServiceImpl(daoMock, PICTURES_PATH);

    private final static Logger LOG = LoggerFactory.getLogger(PictureImportServiceImplTest.class);


    // Delete the "PictureManager" folder created by the tests with the content in it
    @After
    public void cleanFolder() throws URISyntaxException, IOException {
        File folder = new File(PICTURES_PATH);
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

    @Test
    public void testImportMultipleFilesWithOneFail() throws  URISyntaxException{
        List<File> pictures = new ArrayList<>();
        pictures.add(new File(PictureImportService.class.getResource("/ex_pics/Art-gordon-greybg.jpg").toURI()));
        pictures.add(new File(PictureImportService.class.getResource("/voll_das_picture_und_so").toURI()));
        pictures.add(new File(PictureImportService.class.getResource("/ex_pics/wow_gordon_existiert_zweimal.jpg").toURI()));

        try {
			sut.importAll(pictures);
		} catch (ServiceException e) {
			e.printStackTrace();
		}

        File filesToExpect = new File(PICTURES_PATH);
        System.out.println(filesToExpect.list().toString());
        assertEquals(2,filesToExpect.list().length);
    }

}
