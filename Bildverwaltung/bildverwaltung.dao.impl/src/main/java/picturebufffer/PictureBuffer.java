package picturebufffer;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
		maxMemory = (long)(max * 0.9d);
		maxPictureSize = (long)(maxMemory * 0.9d);
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
	 */
	public void bufferPicture(URI uri, byte[] stream) {
		LOG.trace("Enter bufferImage uri={}, stream={}", uri, stream);
		long pictureSize = (long) stream.length;
		if(pictureSize < maxPictureSize) {
			LOG.trace("HIER!!! maxPictureSize={}", maxPictureSize);
			memoryUsage = memoryUsage + pictureSize;
			order.add(uri);
			pictureStreams.put(uri, stream);
			checkMemoryUsage();
		}
		LOG.trace("Exit bufferImage");
	}
	/**
	 * Method keeping track of the current memory usage of the buffer and keeping it from overflowing.
	 * This is accomplished by dropping the least used pictures.
	 */
	private void checkMemoryUsage() {
		LOG.trace("Enter checkMemoryUsage");	
		while(memoryUsage >= maxMemory) {	
			deBufferPicture(order.removeFirst());
		}
		LOG.trace("Exit checkMemoryUsage");
	}
	/**
	 * Method keeping drops pictures from the buffer and voids any mention of it.
	 * 
	 * @param uri, the URI of the to be de-buffered picture.
	 */
	private void deBufferPicture(URI uri) {
		LOG.trace("Enter deBufferPicture uri={}", uri);
		memoryUsage = memoryUsage - (long) pictureStreams.get(uri).length;
		pictureStreams.remove(uri);
		LOG.trace("Exit deBufferPicture");
	}
	/**
	 * Method which returns the stream of a buffered picture and keeps track of its usage.
	 * 
	 * @param uri, of the requested picture.
	 * @return byte-array, containing the buffered stream of the requested picture. 
	 */
	public byte[] getBufferedPictureStream(URI uri) {
		LOG.trace("Enter getBufferedPictureStream uri={}", uri);
		byte[] stream = pictureStreams.get(uri);
		order.remove(uri);
		order.add(uri);
		LOG.trace("Exit getBufferedPictureStream stream={}", stream);
		return stream;
	}
}
