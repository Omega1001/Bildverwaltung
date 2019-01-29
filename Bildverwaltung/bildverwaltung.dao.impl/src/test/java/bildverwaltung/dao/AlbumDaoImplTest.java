package bildverwaltung.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import bildverwaltung.dao.entity.Album;
import bildverwaltung.dao.exception.DaoException;
import bildverwaltung.dao.impl.AlbumDaoImpl;

public class AlbumDaoImplTest {
	
	TestDBEntityManagerFactory emf = new TestDBEntityManagerFactory();
	EntityManager em = emf.get();
	
	AlbumDaoImpl albumDao = new AlbumDaoImpl(em);
	
	// albumX Date = 200X-0X-0X
	static Album album1 = new Album("one", null, new Date(978307200), "Is one");
	static Album album1B = new Album("Bee", null, new Date(978307200), "Is Bee.");
	static UUID uUID1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
	static Album album2 = new Album("two", null, new Date(1012608000), "Is two.");
	static UUID uUID2 = UUID.fromString("00000000-0000-0000-0000-000000000002");
	static Album album3 = new Album("three", null, new Date(1046649600), "Is three.");
	static UUID uUID3 = UUID.fromString("00000000-0000-0000-0000-000000000003");
	static Album album4 = new Album("four", null, new Date(1081036800), "Is four.");
	static UUID uUID4 = UUID.fromString("00000000-0000-0000-0000-000000000004");
	static Album album5 = new Album("five", null, new Date(1115251200), "Is five.");
	static UUID uUID5 = UUID.fromString("00000000-0000-0000-0000-000000000005");
	
	@BeforeClass
	public static void setUpBeforeCLass() {
        	album1.setId(uUID1);
        	album1B.setId(uUID1);
        	album2.setId(uUID2);
        	album3.setId(uUID3);
        	album4.setId(uUID4);
        	album5.setId(uUID5);
	}
	
	public void fillEM() {
		em.getTransaction().begin();
		em.merge(album1);
		em.merge(album2);
		em.merge(album3);
		em.getTransaction().commit();
	}
	
	@After
	public void close() {
		emf.close();
	}
	
	@Test
	public void saveTest1_1() throws DaoException {
		
		fillEM();
		
		albumDao.save(null);
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void saveTest1_2() throws DaoException {
		
		fillEM();
		
		albumDao.save(album4);
		assertEquals(album4, em.find(Album.class, uUID4));
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void saveTest1_3() throws DaoException {
		
		fillEM();
		
		albumDao.save(album4);
		albumDao.save(album5);
		assertEquals(album4, em.find(Album.class, uUID4));
		assertEquals(album5, em.find(Album.class, uUID5));
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void saveTest1_4() throws DaoException {
		
		fillEM();
		
		albumDao.save(album1);
		assertEquals(album1, em.find(Album.class, uUID1));
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void saveTest1_5() throws DaoException {
		
		fillEM();
		
		albumDao.save(album4);
		albumDao.save(album4);
		assertEquals(album4, em.find(Album.class, uUID4));
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void saveTest1_6() throws DaoException {
		
		fillEM();
		
		albumDao.save(album1B);
		assertEquals(album1, em.find(Album.class, uUID1));
		assertEquals(album1B, em.find(Album.class, uUID1));
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void saveTest2_1() throws DaoException {
		
		albumDao.save(null);
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void saveTest2_2() throws DaoException {
		
		albumDao.save(album1);
		assertEquals(album1, em.find(Album.class, uUID1));
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void saveTest2_3() throws DaoException {
		
		albumDao.save(album4);
		albumDao.save(album5);
		assertEquals(album4, em.find(Album.class, uUID4));
		assertEquals(album5, em.find(Album.class, uUID5));
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void getTest1_1() throws DaoException {
		
		fillEM();
		
		albumDao.get(null);
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void getTest1_2() throws DaoException {
		
		fillEM();
		
		assertEquals(albumDao.get(uUID1), em.find(Album.class, uUID1));
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void getTest1_3() throws DaoException {
		
		fillEM();
		
		assertEquals(albumDao.get(uUID5), null);
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void getTest2_1() throws DaoException {
		
		albumDao.get(null);
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void getTest2_2() throws DaoException {
		
		assertEquals(null, albumDao.get(uUID1));
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void getAllTest1_1() throws DaoException {
		
		fillEM();
		
		LinkedList<Album> list = new LinkedList<Album>();
		list.add(album1);
		list.add(album2);
		list.add(album3);
		
		assertThat("List equality without order", albumDao.getAll(), containsInAnyOrder(list));
	}
	
	@Test
	public void getAllTest2_1() throws DaoException {
		
		LinkedList<Album> list = new LinkedList<Album>();
		
		assertThat("List equality without order", albumDao.getAll(), containsInAnyOrder(list));
	}
	
	@Test
	public void deleteTest1_1() throws DaoException {
		
		fillEM();
		
		albumDao.delete(null);
//		TODO check DB for unwanted changes
		
	}
	
	@Test
	public void deleteTest1_2() throws DaoException {
		
		fillEM();
		
		albumDao.delete(uUID3);
		assertEquals(null, em.find(Album.class, uUID3));
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void deleteTest1_3() throws DaoException {
		
		fillEM();
		
		albumDao.delete(uUID5);
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void deleteTest1_4() throws DaoException {
		
		fillEM();
		
		em.getTransaction().begin();
		em.merge(album1);
		em.getTransaction().commit();
		
		albumDao.delete(uUID1);
		
		assertEquals(null, em.find(Album.class, uUID1));
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void deleteTest1_5() throws DaoException {
		
		fillEM();
		
		em.getTransaction().begin();
		em.merge(album1);
		em.merge(album1B);
		em.getTransaction().commit();
		
		albumDao.delete(uUID1);
		
		assertEquals(null, em.find(Album.class, uUID1));
		assertEquals(album1, em.find(Album.class, uUID1));
		assertEquals(album1B, em.find(Album.class, uUID1));
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void deleteTest2_1() throws DaoException {
		
		albumDao.delete(null);
//		TODO check DB for unwanted changes
	}
	
	@Test
	public void deleteTest2_2() throws DaoException {
		
		albumDao.delete(uUID1);
//		TODO check DB for unwanted changes
	}
	
}
