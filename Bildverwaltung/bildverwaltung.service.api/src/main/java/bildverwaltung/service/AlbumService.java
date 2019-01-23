package bildverwaltung.service;

import java.util.List;

import bildverwaltung.dao.entity.Album;
import bildverwaltung.dao.exception.ServiceException;

public interface AlbumService {

	public List<Album> getAllAlbums() throws ServiceException;
	
}
