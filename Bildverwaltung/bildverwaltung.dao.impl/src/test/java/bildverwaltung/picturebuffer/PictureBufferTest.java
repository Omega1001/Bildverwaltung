package bildverwaltung.picturebuffer;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.net.URI;
import bildverwaltung.dao.exception.DaoException;
import bildverwaltung.picturebufffer.PictureBuffer;
public class PictureBufferTest {	
	private PictureBuffer noBuffer;
	private PictureBuffer sixtyfourByteBuffer;
	private PictureBuffer eightMByteBuffer;	
	private URI dummyURI1;
	private URI dummyURI2;
	private URI dummyURI3;
	private URI dummyURI4;
	private URI dummyURI5;
	private URI dummyURIBig;
	private URI dummyURIBigger;
	private URI dummyURIBiggest;	
	private byte[] dummyPicture1 = new byte[16];
	private byte[] dummyPicture2 = new byte[16];
	private byte[] dummyPicture3 = new byte[16];
	private byte[] dummyPicture4 = new byte[16];
	private byte[] dummyPicture5 = new byte[16];
	private byte[] dummyPictureBig = new byte[64];
	private byte[] dummyPictureBigger = new byte[7549747];
	private byte[] dummyPictureBiggest = new byte[8388608];	
	private byte byt = 0;
	@Before
	public void setUpBefore() throws Exception {
		try {
			dummyURI1 = new URI("/file:/C:/Users/Dummy/1");
			dummyURI2 = new URI("/file:/C:/Users/Dummy/2");
			dummyURI3 = new URI("/file:/C:/Users/Dummy/3");
			dummyURI4 = new URI("/file:/C:/Users/Dummy/4");
			dummyURI5 = new URI("/file:/C:/Users/Dummy/5");
			dummyURIBig = new URI("/file:/C:/Users/Dummy/Big");
			dummyURIBigger = new URI("/file:/C:/Users/Dummy/Bigger");
			dummyURIBiggest = new URI("/file:/C:/Users/Dummy/Biggest");
		} catch  (Exception e) {
			throw new Exception(e);
		}
		noBuffer = new PictureBuffer(1L);
		sixtyfourByteBuffer = new PictureBuffer(64L);
		eightMByteBuffer = new PictureBuffer(8388608L);
		for(int i = 0; i < 8388608; i++) {
			if(i < 16) {
				dummyPicture1 [i] = byt++;
				dummyPicture2 [i] = byt++;
				dummyPicture3 [i] = byt++;
				dummyPicture4 [i] = byt++;
				dummyPicture5 [i] = byt++;
			}
			if(i < 64) {
				dummyPictureBig [i] = byt++;
			}
			if(i < 7549747) {
				dummyPictureBigger [i] = byt++;
			}
			if(i < 8388608) {
				dummyPictureBiggest [i] = byt++;
			}	
		}
	}	
	@Test
	public void testPictureBuffer1() {
		PictureBuffer buffer = new PictureBuffer(null);
		assertEquals((Long)0L, buffer.getMaxMemory());
		assertEquals((Long)0L, buffer.getMaxPictureSize());
		assertEquals((Long)0L, buffer.getMemoryUsage());
		assertTrue(buffer.getOrder().isEmpty());
		assertTrue(buffer.getPictureStreams().isEmpty());
	}
	@Test
	public void testPictureBuffer2() {
		PictureBuffer buffer = new PictureBuffer(0L);
		assertEquals((Long)0L, buffer.getMaxMemory());
		assertEquals((Long)0L, buffer.getMaxPictureSize());
		assertEquals((Long)0L, buffer.getMemoryUsage());
		assertTrue(buffer.getOrder().isEmpty());
		assertTrue(buffer.getPictureStreams().isEmpty());
	}
	@Test
	public void testPictureBuffer3() {
		PictureBuffer buffer = new PictureBuffer(-1L);
		assertEquals((Long)0L, buffer.getMaxMemory());
		assertEquals((Long)0L, buffer.getMaxPictureSize());
		assertEquals((Long)0L, buffer.getMemoryUsage());
		assertTrue(buffer.getOrder().isEmpty());
		assertTrue(buffer.getPictureStreams().isEmpty());
	}
	@Test
	public void testPictureBuffer4() {
		PictureBuffer buffer = new PictureBuffer(1L);
		assertEquals((Long)1L, buffer.getMaxMemory());
		assertEquals((Long)0L, buffer.getMaxPictureSize());
		assertEquals((Long)0L, buffer.getMemoryUsage());
		assertTrue(buffer.getOrder().isEmpty());
		assertTrue(buffer.getPictureStreams().isEmpty());
	}
	@Test
	public void testPictureBuffer5() {
		PictureBuffer buffer = new PictureBuffer(536870912L);
		assertEquals((Long)536870912L, buffer.getMaxMemory());
		assertEquals((Long)483183820L, buffer.getMaxPictureSize());
		assertEquals((Long)0L, buffer.getMemoryUsage());
		assertTrue(buffer.getOrder().isEmpty());
		assertTrue(buffer.getPictureStreams().isEmpty());
	}
	@Test
	public void testPictureBuffer6() {
		PictureBuffer buffer = new PictureBuffer(8589934592L);
		assertEquals((Long)8589934592L, buffer.getMaxMemory());
		assertEquals((Long)7730941132L, buffer.getMaxPictureSize());
		assertEquals((Long)0L, buffer.getMemoryUsage());
		assertTrue(buffer.getOrder().isEmpty());
		assertTrue(buffer.getPictureStreams().isEmpty());
	}
	@Test(expected = DaoException.class) 
	public void testBufferPicture1() throws DaoException {
		noBuffer.bufferPicture(null, null);
	}
	@Test(expected = DaoException.class) 
	public void testBufferPicture2() throws DaoException {
		noBuffer.bufferPicture(null, dummyPicture1);
	}
	@Test(expected = DaoException.class) 
	public void testBufferPicture3() throws DaoException {
		noBuffer.bufferPicture(dummyURI1, null);
	}
	@Test 
	public void testBufferPicture4() throws DaoException {
		noBuffer.bufferPicture(dummyURI1, dummyPicture1);
		assertTrue(noBuffer.getOrder().isEmpty());
		assertTrue(noBuffer.getPictureStreams().isEmpty());
	}
	@Test
	public void testBufferPicture5() throws DaoException {
		sixtyfourByteBuffer.bufferPicture(dummyURI1, dummyPicture1);
		assertTrue(sixtyfourByteBuffer.getOrder().contains(dummyURI1));
		assertTrue(sixtyfourByteBuffer.getPictureStreams().containsKey(dummyURI1));
		assertTrue(sixtyfourByteBuffer.getPictureStreams().containsValue(dummyPicture1));
	}
	@Test
	public void testBufferPicture6() throws DaoException {
		sixtyfourByteBuffer.bufferPicture(dummyURIBig, dummyPictureBig);
		assertFalse(sixtyfourByteBuffer.getOrder().contains(dummyURIBig));
		assertFalse(sixtyfourByteBuffer.getPictureStreams().containsKey(dummyURIBig));
		assertFalse(sixtyfourByteBuffer.getPictureStreams().containsValue(dummyPictureBig));
	}
	@Test
	public void testBufferPicture7() throws DaoException {
		sixtyfourByteBuffer.bufferPicture(dummyURI1, dummyPicture1);
		sixtyfourByteBuffer.bufferPicture(dummyURI2, dummyPicture2);
		sixtyfourByteBuffer.bufferPicture(dummyURI3, dummyPicture3);
		sixtyfourByteBuffer.bufferPicture(dummyURI4, dummyPicture4);
		sixtyfourByteBuffer.bufferPicture(dummyURI5, dummyPicture5);
		assertFalse(sixtyfourByteBuffer.getOrder().contains(dummyURI1));
		assertFalse(sixtyfourByteBuffer.getPictureStreams().containsKey(dummyURI1));
		assertFalse(sixtyfourByteBuffer.getPictureStreams().containsValue(dummyPicture1));
		assertTrue(sixtyfourByteBuffer.getOrder().contains(dummyURI2));
		assertTrue(sixtyfourByteBuffer.getPictureStreams().containsKey(dummyURI2));
		assertTrue(sixtyfourByteBuffer.getPictureStreams().containsValue(dummyPicture2));
		assertTrue(sixtyfourByteBuffer.getOrder().contains(dummyURI3));
		assertTrue(sixtyfourByteBuffer.getPictureStreams().containsKey(dummyURI3));
		assertTrue(sixtyfourByteBuffer.getPictureStreams().containsValue(dummyPicture3));
		assertTrue(sixtyfourByteBuffer.getOrder().contains(dummyURI4));
		assertTrue(sixtyfourByteBuffer.getPictureStreams().containsKey(dummyURI4));
		assertTrue(sixtyfourByteBuffer.getPictureStreams().containsValue(dummyPicture4));
		assertTrue(sixtyfourByteBuffer.getOrder().contains(dummyURI5));
		assertTrue(sixtyfourByteBuffer.getPictureStreams().containsKey(dummyURI5));
		assertTrue(sixtyfourByteBuffer.getPictureStreams().containsValue(dummyPicture5));
	}
	@Test
	public void testBufferPicture8() throws DaoException {
		sixtyfourByteBuffer.bufferPicture(dummyURI1, dummyPicture1);
		sixtyfourByteBuffer.bufferPicture(dummyURI2, dummyPicture2);
		sixtyfourByteBuffer.bufferPicture(dummyURI3, dummyPicture3);
		sixtyfourByteBuffer.bufferPicture(dummyURI4, dummyPicture4);
		sixtyfourByteBuffer.bufferPicture(dummyURIBig, dummyPictureBig);
		assertTrue(sixtyfourByteBuffer.getOrder().contains(dummyURI1));
		assertTrue(sixtyfourByteBuffer.getPictureStreams().containsKey(dummyURI1));
		assertTrue(sixtyfourByteBuffer.getPictureStreams().containsValue(dummyPicture1));
		assertTrue(sixtyfourByteBuffer.getOrder().contains(dummyURI2));
		assertTrue(sixtyfourByteBuffer.getPictureStreams().containsKey(dummyURI2));
		assertTrue(sixtyfourByteBuffer.getPictureStreams().containsValue(dummyPicture2));
		assertTrue(sixtyfourByteBuffer.getOrder().contains(dummyURI3));
		assertTrue(sixtyfourByteBuffer.getPictureStreams().containsKey(dummyURI3));
		assertTrue(sixtyfourByteBuffer.getPictureStreams().containsValue(dummyPicture3));
		assertTrue(sixtyfourByteBuffer.getOrder().contains(dummyURI4));
		assertTrue(sixtyfourByteBuffer.getPictureStreams().containsKey(dummyURI4));
		assertTrue(sixtyfourByteBuffer.getPictureStreams().containsValue(dummyPicture4));
		assertFalse(sixtyfourByteBuffer.getOrder().contains(dummyURIBig));
		assertFalse(sixtyfourByteBuffer.getPictureStreams().containsKey(dummyURIBig));
		assertFalse(sixtyfourByteBuffer.getPictureStreams().containsValue(dummyPictureBig));
	}
	@Test
	public void testBufferPicture9() throws DaoException {
		eightMByteBuffer.bufferPicture(dummyURIBigger, dummyPictureBigger);
		assertTrue(eightMByteBuffer.getOrder().contains(dummyURIBigger));
		assertTrue(eightMByteBuffer.getPictureStreams().containsKey(dummyURIBigger));
		assertTrue(eightMByteBuffer.getPictureStreams().containsValue(dummyPictureBigger));
	}
	@Test
	public void testBufferPicture10() throws DaoException {
		eightMByteBuffer.bufferPicture(dummyURIBiggest, dummyPictureBiggest);
		assertFalse(eightMByteBuffer.getOrder().contains(dummyURIBiggest));
		assertFalse(eightMByteBuffer.getPictureStreams().containsKey(dummyURIBiggest));
		assertFalse(eightMByteBuffer.getPictureStreams().containsValue(dummyPictureBiggest));
	}
	@Test
	public void testBufferPicture11() throws DaoException {
		eightMByteBuffer.bufferPicture(dummyURIBigger, dummyPictureBigger);
		eightMByteBuffer.bufferPicture(dummyURI1, dummyPicture1);
		assertTrue(eightMByteBuffer.getOrder().contains(dummyURIBigger));
		assertTrue(eightMByteBuffer.getPictureStreams().containsKey(dummyURIBigger));
		assertTrue(eightMByteBuffer.getPictureStreams().containsValue(dummyPictureBigger));
		assertTrue(eightMByteBuffer.getOrder().contains(dummyURI1));
		assertTrue(eightMByteBuffer.getPictureStreams().containsKey(dummyURI1));
		assertTrue(eightMByteBuffer.getPictureStreams().containsValue(dummyPicture1));
	}
	@Test
	public void testBufferPicture12() throws DaoException {
		eightMByteBuffer.bufferPicture(dummyURI1, dummyPicture1);
		eightMByteBuffer.bufferPicture(dummyURIBigger, dummyPictureBigger);
		System.out.println(eightMByteBuffer.getOrder());
		assertTrue(eightMByteBuffer.getOrder().contains(dummyURIBigger));
		assertTrue(eightMByteBuffer.getPictureStreams().containsKey(dummyURIBigger));
		assertTrue(eightMByteBuffer.getPictureStreams().containsValue(dummyPictureBigger));
		assertTrue(eightMByteBuffer.getOrder().contains(dummyURIBigger));
		assertTrue(eightMByteBuffer.getPictureStreams().containsKey(dummyURIBigger));
		assertTrue(eightMByteBuffer.getPictureStreams().containsValue(dummyPictureBigger));
	}
	@Test(expected = DaoException.class)
	public void testGetBufferedPictureStream1() throws DaoException {
		assertNull(eightMByteBuffer.getBufferedPictureStream(null));
	}
	@Test
	public void testGetBufferedPictureStream2() throws DaoException {
		assertNull(eightMByteBuffer.getBufferedPictureStream(dummyURI1));
	}
	@Test
	public void testGetBufferedPictureStream3() throws DaoException {
		eightMByteBuffer.bufferPicture(dummyURI1, dummyPicture1);
		assertEquals(dummyPicture1, eightMByteBuffer.getBufferedPictureStream(dummyURI1));
	}
}
