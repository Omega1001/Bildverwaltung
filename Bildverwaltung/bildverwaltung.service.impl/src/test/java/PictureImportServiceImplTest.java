import bildverwaltung.service.pictureimport.PictureImportService;
import bildverwaltung.service.pictureimport.impl.PictureImportServiceImpl;
import static org.junit.Assert.*;
import org.junit.Test;


import java.io.File;
import java.net.URISyntaxException;

public class PictureImportServiceImplTest {

    PictureImportService sut = new PictureImportServiceImpl();

    @Test
    public void testCheckIfPicture() throws URISyntaxException {

        File testFile = new File(PictureImportServiceImpl.class.getResource("/ex_pics/Art-gordon-greybg.jpg").toURI());
        File testFileNotPicture = new File(PictureImportServiceImpl.class.getResource("/voll_das_picture_und_so").toURI());

        assertTrue(sut.isPicture(testFile));
        assertFalse(sut.isPicture(testFileNotPicture));

    }

    @Test
    public void testGetExtension() { }

    @Test
    public void testImportFile() { }

}
