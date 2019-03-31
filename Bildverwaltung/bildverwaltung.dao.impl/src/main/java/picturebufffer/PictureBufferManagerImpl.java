package picturebufffer;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import bildverwaltung.dao.PictureBufferManager;
public class PictureBufferManagerImpl implements PictureBufferManager{
	private static final Logger LOG = LoggerFactory.getLogger(PictureBufferManagerImpl.class);
	//TODO Long value (currently null) has to be max memory allocated by user!!!
	private PictureBuffer pictureBuffer = new PictureBuffer(2000000L);;
	public PictureBufferManagerImpl() {
	}
	@Override
	public InputStream readFromBuffer(URI uri) {
		LOG.trace("Enter readFromBuffer uri={}", uri);
		InputStream stream = null;
		byte[] byteArray = pictureBuffer.getBufferedPictureStream(uri);
		if(byteArray != null) {
			stream = new ByteArrayInputStream(byteArray);
		}
		LOG.trace("Exit readFromBuffer stream={}", stream);
		return stream;
	}
	@Override
	public void addToBuffer(URI uri, byte[] byteArray) {
		LOG.trace("Enter addToBuffer uri={}, stream=[]", uri, byteArray);
		pictureBuffer.bufferPicture(uri, byteArray);
		LOG.trace("Exit addToBuffer");
	}
}
