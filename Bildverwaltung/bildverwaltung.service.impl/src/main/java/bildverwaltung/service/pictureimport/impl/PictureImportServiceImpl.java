package bildverwaltung.service.pictureimport.impl;

import bildverwaltung.dao.PictureDao;
import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.DaoException;
import bildverwaltung.dao.exception.ExceptionType;
import bildverwaltung.dao.exception.ServiceException;
import bildverwaltung.service.pictureimport.PictureImportService;
import bildverwaltung.utils.ApplicationIni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PictureImportServiceImpl implements PictureImportService {
    private PictureDao dao;
    private static final Logger LOG = LoggerFactory.getLogger(PictureImportServiceImpl.class);
    private String picturesDirectory;

    public PictureImportServiceImpl(PictureDao dao) {
        this(dao, getImportDirectory());
    }

    public PictureImportServiceImpl(PictureDao dao, String picturesDirectory) {
        this.dao = dao;
        this.picturesDirectory = picturesDirectory;
    }

    /**
     * get the path to the current directory of the pictures
     * @return
     */
    public static String getImportDirectory() {
        return ApplicationIni.get().get("directory", "picturesDirectory");
    }

    /**
     * convert a List of Files to Picture (if they are actually a picture) into the DB
     *
     * @param pictures given (assumpted) picture as file
     * @throws ServiceException 
     */
    @Override
    public List<Picture> importAll(List<File> pictures) {

        List<Picture> importedPictures = new ArrayList<>();

        for(File picture: pictures) {
            LOG.debug("Import Number {} of {}, File: {}",
                    pictures.indexOf(picture), pictures.size(), picture.getAbsolutePath());
            try {
                importedPictures.add(importPicture(picture));
            } catch (ServiceException e) {
                LOG.warn("Import of picture {} failed", picture.getName());
            }
        }

        return importedPictures;
    }


    /**
     * converts a single file to Picture and imports it into the DB.
     *
     * @param picture file to import
     */
    @Override
    public Picture importPicture(File picture) throws ServiceException{

        if (picturesDirectory == null || picturesDirectory == "") {
            throw new ServiceException(ExceptionType.APP_INI_PICTURES_DIR_MISSING);
        }

        if(!isPicture(picture) || picture == null) {
            LOG.error("Picture {} is not actually a picture", picture.getAbsolutePath());
            throw new ServiceException(ExceptionType.NOT_A_PICTURE);
        } else {
            Picture newPicture = convertToEntity(picture);


            //File directory = new File("PictureManager");
            File directory = new File(picturesDirectory);
            //File newFile = new File("PictureManager/" + picture.getName());
            File newFile = new File(picturesDirectory + File.separator + picture.getName());

            directory.mkdirs();
            LOG.debug("created Directory {} in absolute path {}", directory.getName(), directory.getAbsolutePath());

            try {
                LOG.debug("copy {} to {}", picture.getName(), newFile.getName());
                Files.copy(picture.toPath(), newFile.toPath());
            } catch (IOException e) {
                //e.printStackTrace();
                throw new ServiceException(ExceptionType.IMPORT_COPY_PIC_FAILED);
            }


            //rename the File in copy directory to the corresponding UUID of the Picture entity
            File newName = new File(directory.getAbsolutePath() + File.separator + newPicture.getId().toString());

            newFile.renameTo(newName);
            newPicture.setUri(newName.toURI());
            LOG.debug("Saved new picture file as {}", newName.getAbsolutePath());
            try {
                dao.save(newPicture);
                LOG.debug("saved picture in db with following attributes: {}", newPicture.toString());
            } catch (DaoException e) {
                throw new ServiceException(ExceptionType.IMPORT_SAVING_PIC_TO_DB_FAILED);
            }
            return newPicture;
        }

    }

    /**
     * Check if given file is actually a Picture
     *
     * @param assumptedPicture given file that could be a picture
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
     * @param picture file to convert
     * @return Picture Entity converted file
     * @throws ServiceException if given file is not actually a picture
     * @throws ServiceException if there is a exception thrown while reading the file
     */
    private Picture convertToEntity(File picture) throws ServiceException{
        LOG.debug("trying to get attributes needed from {}", picture.getAbsolutePath());
        String extension = getFileExtension(picture);
        String name = picture.getName(); // Does this give the file name with the extension?

        if(name.contains(extension)) {
            int index = name.lastIndexOf(".");
            if(index != -1) {
                name = name.substring(0, name.lastIndexOf("."));
            }
        }

        URI uri = picture.toURI();


        // extract attributes from the picture itself
        int width;
        int height;
        Date date;
        try {
            LOG.debug("Trying to get size and creation date of file {}",picture.getName());
            BasicFileAttributes attributes = Files.readAttributes(picture.toPath(),BasicFileAttributes.class);
            BufferedImage pictureStream = ImageIO.read(picture);

            height = pictureStream.getHeight();
            width = pictureStream.getWidth();
            //date = new Date();
            date = new Date(attributes.creationTime().to(TimeUnit.MILLISECONDS));

        } catch (IOException e) {
            LOG.error("Error while trying to get the size and/or creation date of picture {}"
                    ,picture.getAbsolutePath());
            throw new ServiceException(ExceptionType.IMPORT_EXTRACT_ATTRIBS_FAILED,e);
        }

        Byte rating = 0;

        return new Picture(name, uri, new ArrayList<>(), extension, height, width, date, "", rating);
    }

    /**
     * get the file extension of a given file.
     * @param file given file
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
