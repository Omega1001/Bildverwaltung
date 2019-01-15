package bildverwaltung.service.pictureimport.impl;

import bildverwaltung.dao.PictureDao;
import bildverwaltung.dao.entity.Album;
import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.DaoException;
import bildverwaltung.dao.exception.ExceptionType;
import bildverwaltung.dao.exception.ServiceException;
import bildverwaltung.factory.impl.FactoryPictureDao;
import bildverwaltung.service.pictureimport.PictureImportService;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.nio.file.Files.copy;

public class PictureImportServiceImpl implements PictureImportService {

    private Picture convertToEntity(File picture) {
        if(!isPicture(picture) || picture == null) {

            // nvm found out how to use ExceptionType
            // TODO implement this Exception
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
            try {
                importPicture(picture);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * converts a single file to Picture and imports it into the DB.
     *
     * @param picture
     */
    @Override
    public void importPicture(File picture) throws ServiceException{

        //TODO implement that the method returns a boolean wether the import into the DB did succeed or not

        File directory = new File("PictureManager");
        File newFile = new File("PictureManager/tmp" );

        directory.mkdirs();

        System.out.println(newFile.toPath());

        try {

            Files.copy(picture.toPath(), newFile.toPath());

            Picture newPicture = convertToEntity(newFile);

            //rename the File in copy directory to the corresponding UUID of the Picture entity
            newFile.renameTo(new File( directory.getName() + File.separator + newPicture.getId().toString()));

            PictureDao dao = new FactoryPictureDao().generate(null, null, null);

            try {
                dao.save(newPicture);
            } catch (DaoException e) {
                //throw new RuntimeException("Saving into the database failed!! [TODO: replace with ServiceException (when i learn how the ServiceException works...) ]");
                throw new ServiceException(ExceptionType.DAO_EXCEPTION);
            } finally {

            }


        } catch(IOException e) {
            e.printStackTrace();
            throw new ServiceException(ExceptionType.IO_EXCEPTION);
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
            return (ImageIO.read(asumptedPicture) != null);

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


    /**
     * get the file extension of a given file.
     * @param file
     * @return either the extension or an empty string if there isn't any extension in the filename.
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
