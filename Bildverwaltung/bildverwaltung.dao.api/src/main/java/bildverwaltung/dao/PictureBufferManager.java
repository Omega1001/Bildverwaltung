package bildverwaltung.dao;
import java.io.InputStream;
import java.net.URI;
public interface PictureBufferManager {
	InputStream readFromBuffer(URI uri);
	void addToBuffer(URI uri, byte[] byteArray);
}
