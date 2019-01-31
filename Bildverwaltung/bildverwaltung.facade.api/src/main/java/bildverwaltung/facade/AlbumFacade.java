package bildverwaltung.facade;

import java.util.List;

import bildverwaltung.dao.entity.Album;
import bildverwaltung.dao.exception.FacadeException;

public interface AlbumFacade {

	public List<Album> getAllAlbums() throws FacadeException;
	
}
