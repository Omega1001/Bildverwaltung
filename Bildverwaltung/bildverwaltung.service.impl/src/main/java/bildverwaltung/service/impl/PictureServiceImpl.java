/**
 * 
 */
package bildverwaltung.service.impl;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private static final Logger LOG = LoggerFactory.getLogger(PictureServiceImpl.class);
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
		LOG.trace("Enter getAllPictures");
		List<Picture> res = pDao.getAll();
		LOG.trace("Exit getAllPictures res={}", res);
		return res;
	}

	/* (non-Javadoc)
	 * @see bildverwaltung.service.PictureService#save(bildverwaltung.dao.entity.Picture)
	 */
	@Override
	public Picture save(Picture toSave) throws ServiceException {
		LOG.trace("Enter save toSave={}", toSave);
		Picture res  = pDao.save(toSave);
		LOG.trace("Exit save res={}", res);
		return res;
	}

	/* (non-Javadoc)
	 * @see bildverwaltung.service.PictureService#delete(bildverwaltung.dao.entity.Picture)
	 */
	@Override
	public void delete(Picture toDelete) throws ServiceException {
		LOG.trace("Enter delete toDelete={}", toDelete);
		pDao.delete(toDelete.getId());
		LOG.trace("Exit delete");
	}

	/* (non-Javadoc)
	 * @see bildverwaltung.service.PictureService#resolvePictureURI(java.net.URI)
	 */
	@Override
	public InputStream resolvePictureURI(URI pictureUri) throws ServiceException {
		LOG.trace("Enter resolvePictureURI pictureUri={}", pictureUri);
		InputStream res = uriDao.resolv(pictureUri);
		LOG.trace("Exit resolvePictureURI res={}", res);
		return res;
	}

	/* (non-Javadoc)
	 * @see bildverwaltung.service.PictureService#getFiltered(bildverwaltung.dao.helper.DataFilter, java.util.List)
	 */
	@Override
	public List<Picture> getFiltered(DataFilter<Picture> filter, List<SortCriteria<Picture>> order)
			throws ServiceException {
		LOG.trace("Enter getFiltered filter={}, order={}", filter, order);
		List<Picture> res = pDao.getFiltered(filter, order);
		LOG.trace("Exit getFiltered res={}", res);
		return res;
	}
	
	@Override
	public Picture refresh(Picture picture) throws ServiceException {
		LOG.trace("Enter refresh picture={}", picture);
		Picture res = pDao.refresh(picture);
		LOG.trace("Exit refresh res={}", res);
		return res;
	}

}
