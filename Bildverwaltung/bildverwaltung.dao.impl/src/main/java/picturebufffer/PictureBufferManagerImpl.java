package picturebufffer;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import bildverwaltung.dao.PictureBufferManager;
import bildverwaltung.utils.ApplicationIni;
public class PictureBufferManagerImpl implements PictureBufferManager{
	private static final Logger LOG = LoggerFactory.getLogger(PictureBufferManagerImpl.class);
	private PictureBuffer pictureBuffer = new PictureBuffer(Long.parseLong(ApplicationIni.get().get("cache", "maxSize_MB"))*1024*1024);
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
