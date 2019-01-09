package bildverwaltung.dao;

import java.util.List;
import java.util.Locale;

import bildverwaltung.dao.entity.ResourceString;
import bildverwaltung.dao.exception.DaoException;

public interface ResourceStringDao {

	public List<ResourceString> getResourceStringsForLocale(Locale locale)throws DaoException;
	
}
