package bildverwaltung.facade;

import java.util.List;
import java.util.UUID;

import bildverwaltung.dao.entity.Album;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.utils.DBDataRefference;

public interface AlbumFacade {

	public List<Album> getAllAlbums() throws FacadeException;
	
	public List<DBDataRefference<String>> getAllAlbumNameReferences() throws FacadeException;

	public Album save(Album album) throws FacadeException;

	public Album getAlbumById(UUID albumId) throws FacadeException;

	public void delete(UUID id) throws FacadeException;

	public void refresh(Album alb) throws FacadeException;
	
}
