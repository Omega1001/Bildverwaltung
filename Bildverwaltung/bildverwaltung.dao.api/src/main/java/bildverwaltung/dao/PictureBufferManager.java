package bildverwaltung.dao;
import java.io.InputStream;
/**
 * Class which manages the connection between PictureBuffer objects and the DAO.
 */
import java.net.URI;
public interface PictureBufferManager {
	/**
	 * Method for reading a picture from the PictureBuffer.
	 * 
	 * @param uri, of the to be read picture.
	 * @return InputStream, containing the picture.
	 */
	InputStream readFromBuffer(URI uri);
	/**
	 * Method for adding a picture to the PictureBuffer.
	 * 
	 * @param uri, of the to be added picture.
	 * @param byteArray, containing the picture.
	 */
	void addToBuffer(URI uri, byte[] byteArray);
}
