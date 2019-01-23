package bildverwaltung.service.impl;

import java.util.List;

import bildverwaltung.dao.AlbumDao;
import bildverwaltung.dao.entity.Album;
import bildverwaltung.dao.exception.ServiceException;
import bildverwaltung.service.AlbumService;

public class AlbumServiceImpl implements AlbumService {

	private AlbumDao aDao;
	
	public AlbumServiceImpl(AlbumDao aDao) {
		if(aDao == null) {
			throw new IllegalArgumentException("AlbumDao must not be null");
		}
		this.aDao = aDao;
	}

	@Override
	public List<Album> getAllAlbums() throws ServiceException {
		List<Album> res = aDao.getAll();
		return res;
	}

}
