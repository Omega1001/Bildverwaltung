/**
 * 
 */
package bildverwaltung.facade.impl;

import java.util.List;

import bildverwaltung.dao.entity.Album;
import bildverwaltung.dao.exception.ExceptionType;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.facade.AlbumFacade;
import bildverwaltung.service.AlbumService;

/**
 * @author jannik
 *
 */
public class AlbumFacadeImpl implements AlbumFacade {

	private AlbumService aService;
	
	/**
	 * 
	 */
	public AlbumFacadeImpl(AlbumService aService) {
		if(aService == null) {
			throw new IllegalStateException("AlbumService must not be null");
		}
		this.aService = aService;
	}

	/* (non-Javadoc)
	 * @see bildverwaltung.facade.AlbumFacade#getAllAlbums()
	 */
	@Override
	public List<Album> getAllAlbums() throws FacadeException {
		try {
			return aService.getAllAlbums();
		}catch(FacadeException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new FacadeException(ExceptionType.UNKNOWN, ex);
		}
	}

}
