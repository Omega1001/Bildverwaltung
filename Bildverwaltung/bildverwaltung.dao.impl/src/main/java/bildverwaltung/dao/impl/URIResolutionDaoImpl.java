package bildverwaltung.dao.impl;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bildverwaltung.dao.URIResolutionDao;
import bildverwaltung.dao.URIResolver;
import bildverwaltung.dao.exception.DaoException;
import bildverwaltung.dao.exception.ExceptionType;
import picturebufffer.PictureBufferManagerImpl;

public class URIResolutionDaoImpl implements URIResolutionDao {

	private static final Logger LOG = LoggerFactory.getLogger(URIResolutionDaoImpl.class);
	private final List<URIResolver> resolvers;
	private PictureBufferManagerImpl bufferManager;

	public URIResolutionDaoImpl(List<URIResolver> resolvers) {
		super();
		this.resolvers = resolvers;
	}

	@Override
	public InputStream resolve(URI uri) throws DaoException {
		LOG.trace("Enter resolve uri={}", uri);
		for (URIResolver resolver : resolvers) {
			if (resolver.canHandle(uri)) {
				try {
					InputStream res = bufferManager.readFromBuffer(uri);
					if(res == null) {
						
						res = resolver.handle(uri);
						bufferManager.addToBuffer(uri, res);
						res = bufferManager.readFromBuffer(uri);
					}
					LOG.trace("Exit resolve res={}", res);
					return res;
				} catch (Exception e) {
					throw new DaoException(ExceptionType.URI_RESOLUTION_0002);
				}
			}
		}
		LOG.error("Could not find a resolver, that can handle this uri {}",uri);
		throw new DaoException(ExceptionType.URI_RESOLUTION_0001);
	}

}
