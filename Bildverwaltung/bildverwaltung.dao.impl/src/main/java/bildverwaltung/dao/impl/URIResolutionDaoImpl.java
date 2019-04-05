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
import bildverwaltung.picturebufffer.PictureBufferManagerImpl;
public class URIResolutionDaoImpl implements URIResolutionDao {
	private static final Logger LOG = LoggerFactory.getLogger(URIResolutionDaoImpl.class);
	private final List<URIResolver> resolvers;
	private PictureBufferManagerImpl bufferManager = new PictureBufferManagerImpl();
	public URIResolutionDaoImpl(List<URIResolver> resolvers) {
		super();
		this.resolvers = resolvers;
	}
	@Override
<<<<<<< HEAD
	public InputStream resolve(URI uri) throws DaoException {
		LOG.trace("Enter resolve uri={}", uri);
		InputStream res = null;
		res = bufferManager.readFromBuffer(uri);
		if(res == null) {
			LOG.debug("Picture not in Buffer uri={}", uri);
			for (URIResolver resolver : resolvers) {
				if (resolver.canHandle(uri)) {
					try {
						res = resolver.handle(uri);
						byte[] byteArray = asArray(res);
						bufferManager.addToBuffer(uri, byteArray);
						res = new ByteArrayInputStream(byteArray);
					} catch (Exception e) {
						DaoException ex = new DaoException(ExceptionType.URI_RESOLUTION_0002);
					    LOG.error(ex.toString());
					    throw ex;
					}
				} else {
					LOG.error("Could not find a resolver, that can handle this uri {}",uri);
					throw new DaoException(ExceptionType.URI_RESOLUTION_0001);
=======
	public InputStream resolv(URI uri) throws DaoException {
		LOG.trace("Enter resolv uri={}", uri);
		for (URIResolver resolver : resolvers) {
			if (resolver.canHandle(uri)) {
				try {
					InputStream res = resolver.handle(uri);
					LOG.trace("Exit resolv res={}", res);
					return res;
				} catch (Exception e) {
					DaoException ex = new DaoException(ExceptionType.URI_RESOLUTION_0002, e);
				    LOG.error(ex.toString());
				    throw ex;
>>>>>>> refs/remotes/origin/master
				}
			}
		}
		LOG.trace("Exit resolve res={}", res);
		return res;
	}
	/**
	 * Method for buffering a stream as a byteArray.
	 *
	 * @param stream, which shall be buffered. 
	 * @return byte[], containing the buffered stream.
	 * @throws DaoException 
	 */
	private byte[] asArray(InputStream stream) throws DaoException {
		LOG.trace("Enter asArray stream={}", stream);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int byt;
		try {
			while ((byt = stream.read()) > -1) {
				outputStream.write(byt);
			}
		} catch (IOException e) {
			DaoException ex = new DaoException(ExceptionType.URI_RESOLUTION_0003);
		    LOG.error(ex.toString());
		    throw ex;
		}
		byte [] byteArray = outputStream.toByteArray();
		LOG.trace("Exit asArray byteArray={}", byteArray);
		return byteArray;
	}
}
