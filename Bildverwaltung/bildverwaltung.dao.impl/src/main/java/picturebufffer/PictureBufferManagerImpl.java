package picturebufffer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import bildverwaltung.dao.PictureBufferManager;

public class PictureBufferManagerImpl implements PictureBufferManager{

	//TODO Long value (currently null) has to be max memory allocated by user!!!
	private PictureBuffer pictureBuffer = PictureBuffer.getInstance(null);
	
	public PictureBufferManagerImpl() {
	}
	
	@Override
	public InputStream readFromBuffer(URI uri) {
		
		byte[] byteArray = pictureBuffer.getBufferedPictureStream(uri);
		InputStream stream = new ByteArrayInputStream(byteArray);
		
		return stream;
	}

	@Override
	public void addToBuffer(URI uri, InputStream stream) {
		
		byte[] byteArray;
		try {
			
			byteArray = new byte[stream.available()];
			stream.read(byteArray);
			pictureBuffer.bufferPicture(uri, byteArray);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
