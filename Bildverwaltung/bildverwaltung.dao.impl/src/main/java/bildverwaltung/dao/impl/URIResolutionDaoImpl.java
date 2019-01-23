package bildverwaltung.dao.impl;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

import bildverwaltung.dao.URIResolutionDao;
import bildverwaltung.dao.exception.DaoException;
import bildverwaltung.dao.exception.ExceptionType;

public class URIResolutionDaoImpl implements URIResolutionDao {

	
	private final List<URIResolver> resolvers;

	public URIResolutionDaoImpl(List<URIResolver> resolvers) {
		super();
		this.resolvers = resolvers;
	}

	@Override
	public InputStream resolv(URI uri) throws DaoException {
		for(URIResolver resolver : resolvers) {
			if(resolver.canHandle(uri)) {
				return resolver.handle(uri);
			}
		}
		throw new DaoException(ExceptionType.URI_RESOLUTION_0001);
	}

}
