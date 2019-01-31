package bildverwaltung.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bildverwaltung.dao.AlbumDao;
import bildverwaltung.dao.entity.Album;
import bildverwaltung.dao.exception.ServiceException;
import bildverwaltung.service.AlbumService;

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

}
