package bildverwaltung.dao.impl;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import bildverwaltung.dao.URIResolver;
import bildverwaltung.dao.exception.DaoException;
import picturebufffer.PictureBufferManagerImpl;
public class BufferedURIResolutionDaoImpl extends URIResolutionDaoImpl {
	private static final Logger LOG = LoggerFactory.getLogger(BufferedURIResolutionDaoImpl.class);
	private PictureBufferManagerImpl bufferManager;
	public BufferedURIResolutionDaoImpl(List<URIResolver> resolvers) {
		super(resolvers);
		this.bufferManager = new PictureBufferManagerImpl();
	}
	public InputStream resolve(URI uri) throws DaoException {
		LOG.trace("Enter resolve uri={}", uri);
		InputStream stream = null;
		stream = bufferManager.readFromBuffer(uri);
		if(stream == null) {
			LOG.debug("Picture not in Buffer uri={}", uri);
			stream = super.resolve(uri);
			byte[] byteArray = asArray(stream);
			bufferManager.addToBuffer(uri, byteArray);
			stream = new ByteArrayInputStream(byteArray);
		}
		LOG.trace("Exit resolve res={}", stream);
		return stream;
	}
	/**
	 * Method for buffering a stream as a byteArray.
	 *
	 * @param stream, which shall be buffered. 
	 * @return byte[], containing the buffered stream.
	 */
	private byte[] asArray(InputStream stream) {
		LOG.trace("Enter asArray stream={}", stream);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int byt;
		try {
			while ((byt = stream.read()) > -1) {
				outputStream.write(byt);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte [] byteArray = outputStream.toByteArray();
		LOG.trace("Exit asArray byteArray={}", byteArray);
		return byteArray;
	}
}
