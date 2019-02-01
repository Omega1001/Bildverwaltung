package bildverwaltung.facade;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.dao.helper.DataFilter;
import bildverwaltung.dao.helper.SortCriteria;

public interface PictureFacade {

public List<Picture> getAllPictures() throws FacadeException;
	
	public Picture save(Picture toSave) throws FacadeException;
	
	public void delete(Picture toDelete) throws FacadeException;
	
	public InputStream resolvePictureURI(URI pictureUri) throws FacadeException;
	
	public List<Picture> getFiltered(DataFilter<Picture> filter, List<SortCriteria<Picture>> order) throws FacadeException;

	
}
