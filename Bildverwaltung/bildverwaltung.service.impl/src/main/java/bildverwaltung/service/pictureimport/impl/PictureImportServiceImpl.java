package bildverwaltung.service.pictureimport.impl;

import bildverwaltung.dao.PictureDao;
import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.DaoException;
import bildverwaltung.dao.exception.ExceptionType;
import bildverwaltung.dao.exception.ServiceException;
import bildverwaltung.service.pictureimport.PictureImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PictureImportServiceImpl implements PictureImportService {
    private PictureDao dao;
    private static final Logger LOG = LoggerFactory.getLogger(PictureImportServiceImpl.class);

    public PictureImportServiceImpl(PictureDao dao) {
        this.dao = dao;
    }


    /**
     * convert a List of Files to Picture (if they are actually a picture) into the DB
     *
     * @param pictures given (assumpted) picture as file
     */
    @Override
    public void importAll(List<File> pictures) {

        for(File picture: pictures) {
            LOG.debug("Import Number {} of {}, File: {}",
                    pictures.indexOf(picture), pictures.size(), picture.getAbsolutePath());
            try {
                importPicture(picture);
            } catch (ServiceException e) {
                e.printStackTrace();
                LOG.warn("Import of picture {} failed", picture.getName());
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

        if (!isPicture(picture)) {
            throw new ServiceException(ExceptionType.NOT_A_PICTURE);
        }
        //TODO implement that the method returns a boolean wether the import into the DB did succeed or not

        File directory = new File("PictureManager");
        File newFile = new File("PictureManager/" + picture.getName());

        directory.mkdirs();
        LOG.debug("created Directory {} in absolute path {}", directory.getName(), directory.getAbsolutePath());

        try {
            Files.copy(picture.toPath(), newFile.toPath());
        } catch (IOException e) {
            //e.printStackTrace();
            throw new ServiceException(ExceptionType.IMPORT_COPY_PIC_FAILED);
        }

        Picture newPicture = convertToEntity(newFile);

        //rename the File in copy directory to the corresponding UUID of the Picture entity
        File newName = new File(directory.getName() + File.separator + newPicture.getId().toString());

        newFile.renameTo(newName);
        newPicture.setUri(newName.toURI());
        LOG.debug("Saved new picture as {}", newFile.getAbsolutePath());

        try {
            dao.save(newPicture);
        } catch (DaoException e) {
            throw new ServiceException(ExceptionType.IMPORT_SAVING_PIC_TO_DB_FAILED);
        }

        return newPicture;

    }

    /**
     * Check if given file is actually a Picture
     *
     * @param assumptedPicture
     * @return whether assumptedPicture is a picture or not
     */
    @Override
    public boolean isPicture(File assumptedPicture) {
        try {
            //ImageIO can only read a real picture file, if it is something else, it returns null
            return (ImageIO.read(assumptedPicture) != null);
        } catch(IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * convert a given File to a picture entity that can be added to the DB
     * @param picture
     * @return Picture Entity
     * @throws ServiceException if given file is not actually a picture
     * @throws ServiceException if there is a exception thrown while reading the file
     */
    private Picture convertToEntity(File picture) throws ServiceException{
        if(!isPicture(picture) || picture == null) {
            LOG.error("Picture {} is not actually a picture", picture.getAbsolutePath());
           throw new ServiceException(ExceptionType.NOT_A_PICTURE);
        } else {

            LOG.debug("trying to get attributes needed from {}", picture.getAbsolutePath());
            String extension = getFileExtension(picture);
            String name = picture.getName(); // Does this give the file name with the extension?

            if(name.contains(extension)) {
                name = name.substring(0, name.lastIndexOf("."));
            }

            URI uri = picture.toURI();
            Date date = new Date(); // TODO maybe we should change this to the "added" day that the file manager of the OS shows?

            // extract attributes from the picture itself
            int width;
            int height;
            try {
                BufferedImage pictureStream = ImageIO.read(picture);
                height = pictureStream.getHeight();
                width = pictureStream.getWidth();
            } catch (IOException e) {
                LOG.error("Error while trying to get the size of picture {}",picture.getAbsolutePath());
                throw new ServiceException(ExceptionType.IMPORT_EXTRACT_ATTRIBS_FAILED,e);
            }

            return new Picture(name, uri, new ArrayList<>(), extension, height, width, date, "");
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
            LOG.warn("given file {} does not have a extension, give back a empty string", file.getAbsolutePath());
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }
}
