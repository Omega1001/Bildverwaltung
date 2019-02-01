package bildverwaltung.facade;

import java.util.List;

import bildverwaltung.dao.entity.Album;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.utils.DBDataRefference;

public interface AlbumFacade {

	public List<Album> getAllAlbums() throws FacadeException;
	
	public List<DBDataRefference<String>> getAllAlbumNameReferences() throws FacadeException;

	public Album save(Album album) throws FacadeException;
	
}
