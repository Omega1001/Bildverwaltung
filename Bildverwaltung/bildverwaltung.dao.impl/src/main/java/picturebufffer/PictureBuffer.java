package picturebufffer;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *Class generating and maintaining picture-buffer objects for the application. 
 */

public class PictureBuffer {

	// Object pictureBufferInstance of type PictureBuffer 
    private static PictureBuffer pictureBufferInstance = null;
	
	// Maximum memory in bytes the buffer is not allowed to exceed.
	private Long maxMemory;
	
	// Current memory usage of the buffer in byte.
	private Long memoryUsage;
	
	// List keeping track of the chronological order in which the pictures were added to the buffer.
	private LinkedList<URI> order;
	
	// The collection buffering the pictures.
	private HashMap<URI,byte[]> pictureStreams;
	
	
	public PictureBuffer(Long max) {
		
		maxMemory = (long)(max * 0.8d);
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
		
		memoryUsage = memoryUsage + (long) stream.length;
		order.add(uri);
		pictureStreams.put(uri, stream);
		
		checkMemoryUsage();
		
	}
	
	/**
	 * Method keeping track of the current memory usage of the buffer and keeping it from overflowing.
	 * This is accomplished by dropping the least used pictures.
	 */
	private void checkMemoryUsage() {
		
		while(memoryUsage >= maxMemory) {
			
			deBufferPicture(order.removeFirst());
		}
	}
	
	/**
	 * Method keeping drops pictures from the buffer and voids any mention of it.
	 * 
	 * @param uri, the URI of the to be de-buffered picture.
	 */
	private void deBufferPicture(URI uri) {
		
		memoryUsage = memoryUsage - (long) pictureStreams.get(uri).length;
		pictureStreams.remove(uri);
	}
	
	/**
	 * Method which returns the stream of a buffered picture and keeps track of its usage.
	 * 
	 * @param uri,
	 * @return a byte-array containing the buffered stream of the requested picture. 
	 */
	public byte[] getBufferedPictureStream(URI uri) {
		
		byte[] stream = pictureStreams.get(uri);
		order.remove(uri);
		order.add(uri);
		
		return stream;
	}
}
