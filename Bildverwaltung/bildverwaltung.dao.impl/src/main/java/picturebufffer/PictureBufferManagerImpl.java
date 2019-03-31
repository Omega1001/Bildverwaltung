package picturebufffer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
	public void addToBuffer(URI uri, InputStream stream) {
		LOG.trace("Enter addToBuffer uri={}, stream=[]", uri, stream);
	
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
		pictureBuffer.bufferPicture(uri, byteArray);
		LOG.trace("Exit addToBuffer");
	}

}
