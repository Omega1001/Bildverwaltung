import bildverwaltung.dao.entity.Album;
import bildverwaltung.dao.entity.Picture;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.nio.file.Files.copy;

public class PictureImportServiceImpl implements PictureImportService {

    private Picture convertToEntity(File picture) {
        if(!isPicture(picture) || picture == null) {

            // wtf is ExceptionType
            //throw new ServiceException();
        } else {

            try {
				// extract attributes from picture
                BufferedImage pictureStream = ImageIO.read(picture);
                String name = picture.getName();

                //TODO replace the URI with the path of the copied file ( of course we need to first implement the actual copy step)
                URI uri = picture.toURI();
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

        for(File picture: pictures) {
            importPicture(picture);
        }

    }

    /**
     * converts a single file to Picture and imports it into the DB.
     *
     * @param picture
     */
    @Override
    public void importPicture(File picture) {
        File directory = new File("PictureManager");
        File newFile = new File(directory.getAbsolutePath() + File.pathSeparator + picture.getName());

        directory.mkdirs();

        try {

            Files.copy(picture.toPath(), newFile.toPath());


            Picture newPicture = convertToEntity(newFile);

            //rename the File in copy directory to the corresponding UUID of the Picture entity
            newFile.renameTo(new File(newPicture.getId().toString()));

            //TODO add Picture entity to DB


        } catch(IOException e) {
            e.printStackTrace();
        }
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

    /**
     * Check if the copy directory already exists.
     * @return
     */
    private boolean checkIfCopyDirectoryExists() {
        return new File("PicManager").exists();
    }

    /**
     * Create the copy directory where the new pictures will be copied in
     * @return
     */



    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }
}
