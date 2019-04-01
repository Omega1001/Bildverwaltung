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
	// Constant for turning a long value containing the size in MB into the corresponding amount of bytes.
	private static final Long TO_BYTE = (long)(1024*1024);
	// Constant containing maximum size of the cache in MB, allocated by the user in the app.ini.
	private static final Long CACHE_SIZE_MB = Long.parseLong(ApplicationIni.get().get("cache", "maxSize_MB"));
	// Constant containing the actual cache size in MB.
	private static final Long CACHE_SIZE = CACHE_SIZE_MB * TO_BYTE;
	// The to be managed PictureBuffer
	private PictureBuffer pictureBuffer = new PictureBuffer(CACHE_SIZE);
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
