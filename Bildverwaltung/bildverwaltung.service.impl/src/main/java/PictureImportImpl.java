import bildverwaltung.dao.entity.Album;
import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.ExceptionType;
import bildverwaltung.dao.exception.ServiceException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

public class PictureImportImpl implements PictureImport {

    private Picture convertToEntity(File picture) {
        if(!isPicture(picture) || picture == null) {

            // wtf is ExceptionType
            //throw new ServiceException();
        } else {

            try {
                //attribute von picture extrahieren
                BufferedImage pictureStream = ImageIO.read(picture);
                String name = picture.getName();
                URI uri = picture.toURI(); // TODO replace with copied path
                String extension = getFileExtension(picture);
                int height = pictureStream.getHeight();
                int width = pictureStream.getWidth();
                Date date = new Date();

                //some parameters need to be replaced
                return new Picture(name, uri, new ArrayList<Album>(), extension, height, width, date, "");

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        return null;
    }

    /**
     * convert a List of Files to Picture (if they are actually a picture) into the DB
     *
     * @param pictures given (assumpted) picture as file
     */
    @Override
    public void importAll(List<File> pictures) {

    }

    /**
     * converts a single file to Picture and imports it into the DB.
     *
     * @param picture
     */
    @Override
    public void importPicture(File picture) {

    }

    /**
     * Check if given file is actually a Picture
     *
     * @param asumptedPicture
     * @return if alessio is annoying or not
     */
    @Override
    public boolean isPicture(File asumptedPicture) {
        try {
            //ImageIO can only read a real picture file, if it is something else, it returns null
            return (ImageIO.read(asumptedPicture) == null);

        } catch(IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }
}
