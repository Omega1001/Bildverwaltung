package bildverwaltung.service;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.ServiceException;
import bildverwaltung.dao.helper.DataFilter;
import bildverwaltung.dao.helper.SortCriteria;

public interface PictureService {

	public List<Picture> getAllPictures() throws ServiceException;
	
	public Picture save(Picture toSave) throws ServiceException;
	
	public void delete(Picture toDelete) throws ServiceException;
	
	public InputStream resolvePictureURI(URI pictureUri) throws ServiceException;
	
	public List<Picture> getFiltered(DataFilter<Picture> filter, List<SortCriteria<Picture>> order) throws ServiceException;
}
