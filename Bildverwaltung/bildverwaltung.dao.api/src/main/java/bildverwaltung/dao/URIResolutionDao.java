package bildverwaltung.dao;

import java.io.InputStream;
import java.net.URI;

import bildverwaltung.dao.exception.DaoException;

public interface URIResolutionDao {

	public boolean canResolve(URI uri)throws DaoException;
	
	public InputStream resolv(URI uri)throws DaoException;
	
}
