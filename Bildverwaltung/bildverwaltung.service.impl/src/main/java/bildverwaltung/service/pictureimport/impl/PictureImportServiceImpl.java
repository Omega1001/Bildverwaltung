package bildverwaltung.service.pictureimport.impl;

import bildverwaltung.container.Container;
import bildverwaltung.container.ManagedContainer;
import bildverwaltung.container.Scope;
import bildverwaltung.dao.PictureDao;
import bildverwaltung.dao.entity.Album;
import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.DaoException;
import bildverwaltung.dao.exception.ExceptionType;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.dao.exception.ServiceException;
//import bildverwaltung.factory.impl.FactoryPictureDao;
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
    private PictureDao dao;

    public PictureImportServiceImpl(PictureDao dao) {
        this.dao = dao;
    }

    /**
     * convert a given File to a picture entity that can be added to the DB
     * @param picture
     * @return Picture Entity
     * @throws ServiceException if given file is not actually a picture
     * @throws ServiceException if there is a exception thrown while reading the file
     */

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
    public Picture importPicture(File picture) throws ServiceException{

        //TODO implement that the method returns a boolean wether the import into the DB did succeed or not

        File directory = new File("PictureManager");
        File newFile = new File("PictureManager/tmp" );

        directory.mkdirs();

        try {

            Files.copy(picture.toPath(), newFile.toPath());


        } catch(IOException e) {
            //e.printStackTrace();
            throw new ServiceException(ExceptionType.IMPORT_COPY_PIC_FAILED);
        }

        Picture newPicture = convertToEntity(newFile);

        //rename the File in copy directory to the corresponding UUID of the Picture entity
        File newName = new File( directory.getName() + File.separator + newPicture.getId().toString());

        newFile.renameTo(newName);
        newPicture.setUri(newName.toURI());



        try {

            dao.save(newPicture);

        } catch (FacadeException e) {

            e.printStackTrace();
            throw new ServiceException(ExceptionType.IMPORT_SAVING_PIC_TO_DB_FAILED);

        }

        return newPicture;
    }

    /**
     * Check if given file is actually a Picture
     *
     * @param asumptedPicture
     * @return whether asumptedPicture is a picture or not
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
     * Create the copy directory where the new pictures will be copied in
     * @return
     */

    private Picture convertToEntity(File picture) throws ServiceException{
        if(!isPicture(picture) || picture == null) {

           throw new ServiceException(ExceptionType.NOT_A_PICTURE);

        } else {

            String name = picture.getName(); // Does this give the file name with the extension?
            URI uri = picture.toURI();
            String extension = getFileExtension(picture);
            Date date = new Date(); // TODO maybe we should change this to the "added" day that the file manager of the OS shows?

            // extract attributes from the picture itself
            int width;
            int height;
            try {

                BufferedImage pictureStream = ImageIO.read(picture);
                height = pictureStream.getHeight();
                width = pictureStream.getWidth();

            } catch (IOException e) {

                e.printStackTrace();
                throw new ServiceException(ExceptionType.IMPORT_EXTRACT_ATTRIBS_FAILED);

            }

            return new Picture(name, uri, new ArrayList<Album>(), extension, height, width, date, "");
        }
    }

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
