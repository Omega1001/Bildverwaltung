/**
 * 
 */
package bildverwaltung.service.impl;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import bildverwaltung.dao.PictureDao;
import bildverwaltung.dao.URIResolutionDao;
import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.ServiceException;
import bildverwaltung.dao.helper.DataFilter;
import bildverwaltung.dao.helper.SortCriteria;
import bildverwaltung.service.PictureService;

/**
 * @author jannik
 *
 */
public class PictureServiceImpl implements PictureService {
	private final PictureDao pDao;
	private final URIResolutionDao uriDao;
	
	/**
	 * 
	 */
	public PictureServiceImpl(PictureDao pDao,URIResolutionDao uriDao) {
		if(pDao == null) {
			throw new IllegalArgumentException("PictureDao must nt be null");
		}
		if(uriDao == null) {
			throw new IllegalArgumentException("URIResolutionDao must nt be null");
		}
		this.pDao = pDao;
		this.uriDao = uriDao;
	}

	/* (non-Javadoc)
	 * @see bildverwaltung.service.PictureService#getAllPictures()
	 */
	@Override
	public List<Picture> getAllPictures() throws ServiceException {
		List<Picture> res = pDao.getAll();
		return res;
	}

	/* (non-Javadoc)
	 * @see bildverwaltung.service.PictureService#save(bildverwaltung.dao.entity.Picture)
	 */
	@Override
	public Picture save(Picture toSave) throws ServiceException {
		Picture res  = pDao.save(toSave);
		return res;
	}

	/* (non-Javadoc)
	 * @see bildverwaltung.service.PictureService#delete(bildverwaltung.dao.entity.Picture)
	 */
	@Override
	public void delete(Picture toDelete) throws ServiceException {
		pDao.delete(toDelete.getId());
	}

	/* (non-Javadoc)
	 * @see bildverwaltung.service.PictureService#resolvePictureURI(java.net.URI)
	 */
	@Override
	public InputStream resolvePictureURI(URI pictureUri) throws ServiceException {
		InputStream res = uriDao.resolv(pictureUri);
		return res;
	}

	/* (non-Javadoc)
	 * @see bildverwaltung.service.PictureService#getFiltered(bildverwaltung.dao.helper.DataFilter, java.util.List)
	 */
	@Override
	public List<Picture> getFiltered(DataFilter<Picture> filter, List<SortCriteria<Picture>> order)
			throws ServiceException {
		List<Picture> res = pDao.getFiltered(filter, order);
		return res;
	}

}
