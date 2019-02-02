package bildverwaltung.facade.impl;

import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.ExceptionType;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.dao.exception.ServiceException;
import bildverwaltung.facade.PictureImportFacade;
import bildverwaltung.service.pictureimport.PictureImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

public class PictureImportFacadeImpl implements PictureImportFacade {

   private static final Logger LOG = LoggerFactory.getLogger(PictureImportFacadeImpl.class);
   private PictureImportService picImportService;

   public PictureImportFacadeImpl(PictureImportService picImportService) {
      if(picImportService != null) {
         this.picImportService = picImportService;
      } else {
         throw new IllegalStateException("PictureImportService must not be null");
      }
   }


   @Override
   public void importAll(List<File> pictures) {
         importAll(pictures);
   }

   @Override
   public Picture importPicture(File picture) throws ServiceException{
      try {
         return picImportService.importPicture(picture);
      } catch (ServiceException e) {
            throw e;
      }
   }

   @Override
   public boolean isPicture(File picture) throws ServiceException {
      return picImportService.isPicture(picture);
   }


}