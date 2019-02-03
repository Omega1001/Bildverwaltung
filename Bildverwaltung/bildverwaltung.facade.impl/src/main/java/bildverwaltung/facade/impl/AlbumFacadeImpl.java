/**
 * 
 */
package bildverwaltung.facade.impl;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bildverwaltung.dao.entity.Album;
import bildverwaltung.dao.exception.ExceptionType;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.facade.AlbumFacade;
import bildverwaltung.service.AlbumService;
import bildverwaltung.utils.DBDataRefference;

/**
 * @author jannik
 *
 */
public class AlbumFacadeImpl implements AlbumFacade {
	private static final Logger LOG = LoggerFactory.getLogger(AlbumFacadeImpl.class);
	private AlbumService aService;

	/**
	 * 
	 */
	public AlbumFacadeImpl(AlbumService aService) {
		if (aService == null) {
			throw new IllegalStateException("AlbumService must not be null");
		}
		this.aService = aService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bildverwaltung.facade.AlbumFacade#getAllAlbums()
	 */
	@Override
	public List<Album> getAllAlbums() throws FacadeException {
		try {
			return aService.getAllAlbums();
		}catch(FacadeException ex) {
			throw ex;
		}catch(Exception ex) {
			LOG.error("Unexpected error during fetching all albums : ",ex);
			throw new FacadeException(ExceptionType.UNKNOWN, ex);
		}
	}

	@Override
	public List<DBDataRefference<String>> getAllAlbumNameReferences() throws FacadeException {
		try {
			return aService.getAllAlbumNameReferences();
		} catch (FacadeException ex) {
			throw ex;
		} catch (Exception ex) {
			LOG.error("Unexpected error during fetching all albums name refferences : ",ex);
			throw new FacadeException(ExceptionType.UNKNOWN, ex);
		}
	}

	@Override
	public Album save(Album toSave) throws FacadeException {
		try {
			return aService.save(toSave);
		} catch (FacadeException ex) {
			throw ex;
		} catch (Exception ex) {
			LOG.error("Unexpected error during saving album {} : ",toSave,ex);
			throw new FacadeException(ExceptionType.UNKNOWN, ex);
		}
	}

	@Override
	public Album getAlbumById(UUID albumId) throws FacadeException {
		try {
			return aService.getAlbumById(albumId);
		} catch (FacadeException ex) {
			throw ex;
		} catch (Exception ex) {
			LOG.error("Unexpected error during fetching album with id {} : ",albumId,ex);
			throw new FacadeException(ExceptionType.UNKNOWN, ex);
		}
	}

}
