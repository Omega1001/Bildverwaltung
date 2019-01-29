package bildverwaltung.dao;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.DaoException;
import bildverwaltung.dao.impl.PictureDaoImpl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

public class PictureDaoImplTest {
	
	TestDBEntityManagerFactory emf = new TestDBEntityManagerFactory();
	EntityManager em = emf.get();
	
	PictureDaoImpl picDao = new PictureDaoImpl(em);
	
	// picX Date = 200X-0X-0X
	static Picture pic1;
	static Picture pic1B;
	static UUID uUID1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
	static Picture pic2;
	static UUID uUID2 = UUID.fromString("00000000-0000-0000-0000-000000000002");
	static Picture pic3;
	static UUID uUID3 = UUID.fromString("00000000-0000-0000-0000-000000000003");
	static Picture pic4;
	static UUID uUID4 = UUID.fromString("00000000-0000-0000-0000-000000000004");
	static Picture pic5;
	static UUID uUID5 = UUID.fromString("00000000-0000-0000-0000-000000000005");
	
	@BeforeClass
		public static void setUpBeforeCLass() {
	        try {
	        	pic1 = new Picture("black", new URI("file", "//bildverwaltung.dao.impl/src/test/resources/black.png", null), null, ".png", 4, 128, new Date(978307200), "Is black.");
	        	pic1.setId(uUID1);
	        	pic1B = new Picture("Bee", new URI("file", "//bildverwaltung.dao.impl/src/test/resources/Bee.png", null), null, ".jpg", 16, 16, new Date(978307200), "Is Bee.");
	        	pic1B.setId(uUID1);
	        	pic2 = new Picture("blue", new URI("file", "//bildverwaltung.dao.impl/src/test/resources/blue.png", null), null, ".png", 8, 64, new Date(1012608000), "Is blue.");
	        	pic2.setId(uUID2);
	        	pic3 = new Picture("green", new URI("file", "//bildverwaltung.dao.impl/src/test/resources/green.png", null), null, ".png", 16, 32, new Date(1046649600), "Is green.");
	        	pic3.setId(uUID3);
	        	pic4 = new Picture("red", new URI("file", "//bildverwaltung.dao.impl/src/test/resources/red.png", null), null, ".png", 32, 16, new Date(1081036800), "Is red.");
	        	pic4.setId(uUID4);
	        	pic5 = new Picture("white", new URI("file", "//bildverwaltung.dao.impl/src/test/resources/white.png", null), null, ".png", 64, 8, new Date(1115251200), "Is white.");
	        	pic5.setId(uUID5);
	        } catch (URISyntaxException e) {
	            throw new RuntimeException(e);
	        }
		}
	
	public void fillEM() {
		em.getTransaction().begin();
		em.merge(pic1);
		em.merge(pic2);
		em.merge(pic3);
		em.getTransaction().commit();
	}
	
	@After
	public void close() {
		emf.close();
	}
	
	@Test
	public void saveTest1_1() throws DaoException {
		
		fillEM();
		
		picDao.save(null);
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void saveTest1_2() throws DaoException {
		
		fillEM();
		
		picDao.save(pic4);
		assertEquals(pic4, em.find(Picture.class, uUID4));
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void saveTest1_3() throws DaoException {
		
		fillEM();
		
		picDao.save(pic4);
		picDao.save(pic5);
		assertEquals(pic4, em.find(Picture.class, uUID4));
		assertEquals(pic5, em.find(Picture.class, uUID5));
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void saveTest1_4() throws DaoException {
		
		fillEM();
		
		picDao.save(pic1);
		assertEquals(pic1, em.find(Picture.class, uUID1));
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void saveTest1_5() throws DaoException {
		
		fillEM();
		
		picDao.save(pic4);
		picDao.save(pic4);
		assertEquals(pic4, em.find(Picture.class, uUID4));
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void saveTest1_6() throws DaoException {
		
		fillEM();
		
		picDao.save(pic1B);
		assertEquals(pic1, em.find(Picture.class, uUID1));
		assertEquals(pic1B, em.find(Picture.class, uUID1));
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void saveTest2_1() throws DaoException {
		
		picDao.save(null);
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void saveTest2_2() throws DaoException {
		
		picDao.save(pic1);
		assertEquals(pic1, em.find(Picture.class, uUID1));
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void saveTest2_3() throws DaoException {
		
		picDao.save(pic4);
		picDao.save(pic5);
		assertEquals(pic4, em.find(Picture.class, uUID4));
		assertEquals(pic5, em.find(Picture.class, uUID5));
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void getTest1_1() throws DaoException {
		
		fillEM();
		
		picDao.get(null);
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void getTest1_2() throws DaoException {
		
		fillEM();
		
		assertEquals(picDao.get(uUID1), em.find(Picture.class, uUID1));
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void getTest1_3() throws DaoException {
		
		fillEM();
		
		assertEquals(picDao.get(uUID5), null);
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void getTest2_1() throws DaoException {
		
		picDao.get(null);
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void getTest2_2() throws DaoException {
		
		assertEquals(null, picDao.get(uUID1));
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void getAllTest1_1() throws DaoException {
		
		fillEM();
		
		LinkedList<Picture> list = new LinkedList<Picture>();
		list.add(pic1);
		list.add(pic2);
		list.add(pic3);
		
		assertThat("List equality without order", picDao.getAll(), containsInAnyOrder(list));
	}
	
	@Test
	public void getAllTest2_1() throws DaoException {
		
		LinkedList<Picture> list = new LinkedList<Picture>();
		
		assertThat("List equality without order", picDao.getAll(), containsInAnyOrder(list));
	}
	
	@Test
	public void deleteTest1_1() throws DaoException {
		
		fillEM();
		
		picDao.delete(null);
//		TODO check DB for unwanted changes
		
	}
	
	@Test
	public void deleteTest1_2() throws DaoException {
		
		fillEM();
		
		picDao.delete(uUID3);
		assertEquals(null, em.find(Picture.class, uUID3));
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void deleteTest1_3() throws DaoException {
		
		fillEM();
		
		picDao.delete(uUID5);
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void deleteTest1_4() throws DaoException {
		
		fillEM();
		
		em.getTransaction().begin();
		em.merge(pic1);
		em.getTransaction().commit();
		
		picDao.delete(uUID1);
		
		assertEquals(null, em.find(Picture.class, uUID1));
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void deleteTest1_5() throws DaoException {
		
		fillEM();
		
		em.getTransaction().begin();
		em.merge(pic1);
		em.merge(pic1B);
		em.getTransaction().commit();
		
		picDao.delete(uUID1);
		
		assertEquals(null, em.find(Picture.class, uUID1));
		assertEquals(pic1, em.find(Picture.class, uUID1));
		assertEquals(pic1B, em.find(Picture.class, uUID1));
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void deleteTest2_1() throws DaoException {
		
		picDao.delete(null);
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void deleteTest2_2() throws DaoException {
		
		picDao.delete(uUID1);
//		TODO check DB for unwanted changes
	}
	
}
