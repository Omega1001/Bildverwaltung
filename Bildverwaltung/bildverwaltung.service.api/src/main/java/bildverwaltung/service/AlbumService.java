package bildverwaltung.service;

import java.util.List;

import bildverwaltung.dao.entity.Album;
import bildverwaltung.dao.exception.ServiceException;
import bildverwaltung.utils.DBDataRefference;

public interface AlbumService {

	public List<Album> getAllAlbums() throws ServiceException;

	public List<DBDataRefference<String>> getAllAlbumNameReferences() throws ServiceException;

	public Album save(Album toSave) throws ServiceException;
	
}
