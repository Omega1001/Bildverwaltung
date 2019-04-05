package bildverwaltung.service.impl;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bildverwaltung.dao.AlbumDao;
import bildverwaltung.dao.entity.Album;
import bildverwaltung.dao.exception.DaoException;
import bildverwaltung.dao.exception.ServiceException;
import bildverwaltung.service.AlbumService;
import bildverwaltung.utils.DBDataRefference;

public class AlbumServiceImpl implements AlbumService {
	private static final Logger LOG = LoggerFactory.getLogger(AlbumServiceImpl.class);
	private AlbumDao aDao;

	public AlbumServiceImpl(AlbumDao aDao) {
		if (aDao == null) {
			throw new IllegalArgumentException("AlbumDao must not be null");
		}
		this.aDao = aDao;
	}

	@Override
	public List<Album> getAllAlbums() throws ServiceException {
		LOG.trace("Enter getAllAlbums");
		List<Album> res = aDao.getAll();
		LOG.trace("Exit getAllAlbums res={}", res);
		return res;
	}

	@Override
	public List<DBDataRefference<String>> getAllAlbumNameReferences() throws ServiceException {
		LOG.trace("Enter getAllAlbumNameReferences");
		List<DBDataRefference<String>> refs = aDao.getAllAlbumNameReferences();
		LOG.trace("Exit getAllAlbumNameReferences refs={}", refs);
		return refs;
	}

	@Override
	public Album save(Album toSave) throws ServiceException{
		LOG.trace("Enter save toSave={}", toSave);
		Album res = aDao.save(toSave);
		LOG.trace("Exit save res={}", res);
		return res;
	}

	@Override
	public Album getAlbumById(UUID albumId) throws ServiceException {
		LOG.trace("Enter getAlbumById albumId={}", albumId);
		Album res = aDao.get(albumId);
		LOG.trace("Exit getAlbumById res={}", res);
		return res;
	}

	@Override
	public void delete(UUID albumId) throws ServiceException {
		LOG.trace("Enter delete albumId={}", albumId);
		aDao.delete(albumId);
		LOG.trace("Exit delete");
	}

}
