package bildverwaltung.facade;

import java.util.Locale;
import java.util.ResourceBundle;

import bildverwaltung.dao.exception.FacadeException;

public interface ResourceStringFacade {

	public ResourceBundle fetchBundleForLocale(Locale locale)throws FacadeException;
	
}
