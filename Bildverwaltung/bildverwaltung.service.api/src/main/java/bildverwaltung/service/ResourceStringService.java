package bildverwaltung.service;

import java.util.Locale;
import java.util.ResourceBundle;

import bildverwaltung.dao.exception.ServiceException;

public interface ResourceStringService {

	public ResourceBundle fetchBundleForLocale(Locale locale)throws ServiceException;
	
}
