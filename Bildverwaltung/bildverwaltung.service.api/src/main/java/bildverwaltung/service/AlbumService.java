package bildverwaltung.service;

import java.util.List;
import java.util.UUID;

import bildverwaltung.dao.entity.Album;
import bildverwaltung.dao.exception.ServiceException;
import bildverwaltung.utils.DBDataRefference;

public interface AlbumService {

	public List<Album> getAllAlbums() throws ServiceException;

	public List<DBDataRefference<String>> getAllAlbumNameReferences() throws ServiceException;

	public Album save(Album toSave) throws ServiceException;

	public Album getAlbumById(UUID albumId) throws ServiceException;

	public void delete(UUID albumId)throws ServiceException;

	public void refresh(Album alb)throws ServiceException;
	
}
