package bildverwaltung.service.impl;

import bildverwaltung.dao.entity.Picture;

import java.io.File;
import java.util.List;

/**
 * Class with utilities to import a path-string to an picture entity
 * @author Matthias
 * @author Alessio
 */

public interface PictureImportService {

    /**
     * convert a List of Files to Picture (if they are actually a picture) into the DB
     * @param pictures given (assumpted) picture as file
     */
    public void importAll(List<File> pictures);

    /**
     * converts a single file to Picture and imports it into the DB.
     * @param picture
     */
    public void importPicture(File picture);

    /**
     * Check if given file is actually a Picture
     * @param asumptedPicture
     * @return if alessio is annoying or not
     */
    public boolean isPicture(File asumptedPicture);

}
