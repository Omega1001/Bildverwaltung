package bildverwaltung.facade;

import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.ServiceException;

import java.io.File;
import java.util.List;

public interface PictureImportFacade {

    void importAll(List<File> pictures)throws ServiceException;

    Picture importPicture(File picture)throws ServiceException;

    boolean isPicture(File picture) throws ServiceException;

}
