package bildverwaltung.picturebufffer;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bildverwaltung.dao.exception.DaoException;
import bildverwaltung.dao.exception.ExceptionType;
/**
 *Class generating and maintaining picture-buffer objects for the application. 
 */
public class PictureBuffer {	
	private static final Logger LOG = LoggerFactory.getLogger(PictureBuffer.class);
	// Maximum memory in bytes the buffer is not allowed to exceed.
	private Long maxMemory;
	// Maximum size in bytes a picture is allowed to be buffered.
	private Long maxPictureSize;
	// Current memory usage of the buffer in byte.
	private Long memoryUsage;
	// List keeping track of the chronological order in which the pictures were added to the buffer.
	private LinkedList<URI> order;
	// The collection buffering the pictures.
	private HashMap<URI,byte[]> pictureStreams;
	public PictureBuffer(Long max) {
		maxMemory = 0L;
		maxPictureSize = 0L;
		if(max != null && max > 0) {
			maxMemory = max;
			maxPictureSize = (long)(maxMemory * 0.9d);
		}
		memoryUsage = 0L;
		order = new LinkedList <URI>();
		pictureStreams = new HashMap<URI,byte[]>();
	}
	/**
	 * Method for adding pictures to the buffer.
	 * 
	 * @param uri, the URI of the to be buffered picture.
	 * @param stream, a byteArray containing the saved Stream of the to be buffered picture.
	 * @param size, the size of the to be buffered picture.
	 * @throws DaoException 
	 */
	public void bufferPicture(URI uri, byte[] stream) throws DaoException {
		LOG.trace("Enter bufferImage uri={}, stream={}", uri, stream);
		if(uri == null) {
			DaoException ex = new DaoException(ExceptionType.PICTURE_BUFFER_0001);
		    LOG.error(ex.toString());
		    throw ex;
		}
		if(stream == null) {
			DaoException ex = new DaoException(ExceptionType.PICTURE_BUFFER_0002);
		    LOG.error(ex.toString());
		    throw ex;
		}
		long pictureSize = (long) stream.length;
		if(pictureSize <= maxPictureSize) {
			memoryUsage = memoryUsage + pictureSize;
			checkMemoryUsage();
			order.addLast(uri);
			pictureStreams.put(uri, stream);
		}
		LOG.trace("Exit bufferImage");
	}
	/**
	 * Method keeping track of the current memory usage of the buffer and keeping it from overflowing.
	 * This is accomplished by dropping the least used pictures.
	 * @throws DaoException 
	 */
	private void checkMemoryUsage() throws DaoException {
		LOG.trace("Enter checkMemoryUsage");	
		while(memoryUsage > maxMemory) {
			deBufferPicture(order.removeFirst());
		}
		LOG.trace("Exit checkMemoryUsage");
	}
	/**
	 * Method keeping drops pictures from the buffer and voids any mention of it.
	 * 
	 * @param uri, the URI of the to be de-buffered picture.
	 * @throws DaoException 
	 */
	private void deBufferPicture(URI uri) throws DaoException {
		LOG.trace("Enter deBufferPicture uri={}", uri);
		if(uri == null) {
			DaoException ex = new DaoException(ExceptionType.PICTURE_BUFFER_0003);
		    LOG.error(ex.toString());
		    throw ex;
		}
		memoryUsage = memoryUsage - (long) pictureStreams.get(uri).length;
		pictureStreams.remove(uri);
		LOG.trace("Exit deBufferPicture");
	}
	/**
	 * Method which returns the stream of a buffered picture and keeps track of its usage.
	 * 
	 * @param uri, of the requested picture.
	 * @return byte-array, containing the buffered stream of the requested picture. 
	 * @throws DaoException 
	 */
	public byte[] getBufferedPictureStream(URI uri) throws DaoException {
		LOG.trace("Enter getBufferedPictureStream uri={}", uri);
		if(uri == null) {
			DaoException ex = new DaoException(ExceptionType.PICTURE_BUFFER_0004);
		    LOG.error(ex.toString());
		    throw ex;
		}
		byte[] stream = null;
		if(pictureStreams.containsKey(uri)) {
			stream = pictureStreams.get(uri);
			order.remove(uri);
			order.addLast(uri);
		}
		LOG.trace("Exit getBufferedPictureStream stream={}", stream);
		return stream;
	}
	public Long getMaxMemory() {
		return maxMemory;
	}
	public Long getMaxPictureSize() {
		return maxPictureSize;
	}
	public Long getMemoryUsage() {
		return memoryUsage;
	}
	public LinkedList<URI> getOrder() {
		return order;
	}
	public HashMap<URI, byte[]> getPictureStreams() {
		return pictureStreams;
	}
}
