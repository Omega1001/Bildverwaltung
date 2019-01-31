/**
 * 
 */
package bildverwaltung.facade.impl;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.ExceptionType;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.dao.helper.DataFilter;
import bildverwaltung.dao.helper.SortCriteria;
import bildverwaltung.facade.PictureFacade;
import bildverwaltung.service.PictureService;

/**
 * @author jannik
 *
 */
public class PictureFacadeImpl implements PictureFacade {

	private PictureService pService;
	
	/**
	 * 
	 */
	public PictureFacadeImpl(PictureService pService) {
		if(pService == null) {
			throw new IllegalArgumentException("PictureService must not be null");
		}
		this.pService = pService;
	}

	/* (non-Javadoc)
	 * @see bildverwaltung.facade.PictureFacade#getAllPictures()
	 */
	@Override
	public List<Picture> getAllPictures() throws FacadeException {
		try {
			return pService.getAllPictures();
		}catch(FacadeException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new FacadeException(ExceptionType.UNKNOWN, ex);
		}
	}

	/* (non-Javadoc)
	 * @see bildverwaltung.facade.PictureFacade#save(bildverwaltung.dao.entity.Picture)
	 */
	@Override
	public Picture save(Picture toSave) throws FacadeException {
		try {
			return pService.save(toSave);
		}catch(FacadeException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new FacadeException(ExceptionType.UNKNOWN, ex);
		}
	}

	/* (non-Javadoc)
	 * @see bildverwaltung.facade.PictureFacade#delete(bildverwaltung.dao.entity.Picture)
	 */
	@Override
	public void delete(Picture toDelete) throws FacadeException {
		try {
			pService.delete(toDelete);
		}catch(FacadeException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new FacadeException(ExceptionType.UNKNOWN, ex);
		}
	}

	/* (non-Javadoc)
	 * @see bildverwaltung.facade.PictureFacade#resolvePictureURI(java.net.URI)
	 */
	@Override
	public InputStream resolvePictureURI(URI pictureUri) throws FacadeException {
		try {
			return pService.resolvePictureURI(pictureUri);
		}catch(FacadeException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new FacadeException(ExceptionType.UNKNOWN, ex);
		}
	}

	/* (non-Javadoc)
	 * @see bildverwaltung.facade.PictureFacade#getFiltered(bildverwaltung.dao.helper.DataFilter, java.util.List)
	 */
	@Override
	public List<Picture> getFiltered(DataFilter<Picture> filter, List<SortCriteria<Picture>> order)
			throws FacadeException {
		try {
			return pService.getFiltered(filter, order);
		}catch(FacadeException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new FacadeException(ExceptionType.UNKNOWN, ex);
		}
	}

}
