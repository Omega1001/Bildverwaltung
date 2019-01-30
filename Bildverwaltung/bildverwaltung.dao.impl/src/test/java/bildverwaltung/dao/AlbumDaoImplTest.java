package bildverwaltung.dao;

import bildverwaltung.dao.entity.Album;
import bildverwaltung.dao.exception.DaoException;
import bildverwaltung.dao.impl.AlbumDaoImpl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.hamcrest.collection.IsEmptyCollection;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.MatcherAssert.assertThat;

public class AlbumDaoImplTest {
	
	@Rule
	public final ExpectedException exception = ExpectedException.none();
	
	private TestDBEntityManagerFactory emf = new TestDBEntityManagerFactory();
	private EntityManager em = emf.get();
	
	private AlbumDaoImpl albumDao = new AlbumDaoImpl(em);
	
	// albumX Date = 200X-0X-0X
	private Album album1 = new Album("one", null, new Date(978307200), "Is one");
	private Album album1B = new Album("Bee", null, new Date(978307200), "Is Bee.");
	private UUID uUID1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
	private Album album2 = new Album("two", null, new Date(1012608000), "Is two.");
	private UUID uUID2 = UUID.fromString("00000000-0000-0000-0000-000000000002");
	private Album album3 = new Album("three", null, new Date(1046649600), "Is three.");
	private UUID uUID3 = UUID.fromString("00000000-0000-0000-0000-000000000003");
	private Album album4 = new Album("four", null, new Date(1081036800), "Is four.");
	private UUID uUID4 = UUID.fromString("00000000-0000-0000-0000-000000000004");
	private Album album5 = new Album("five", null, new Date(1115251200), "Is five.");
	private UUID uUID5 = UUID.fromString("00000000-0000-0000-0000-000000000005");
	
	@Before
	public void setUp() {
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
		
		// save null on non-empty DB
		
		fillEM();
		
		exception.expect(DaoException.class);
		
		em.getTransaction().begin();
		albumDao.save(null);
		em.getTransaction().commit();

		List<Album> p = em.createQuery("SELECT a FROM Album a", Album.class).getResultList();
		assertThat(p, containsInAnyOrder(album1, album2, album3));
	}
	
	@Test
	public void saveTest1_2() throws DaoException {
		
		// save new Album entity on non-empty DB
		
		fillEM();
		
		em.getTransaction().begin();
		albumDao.save(album4);
		em.getTransaction().commit();

		List<Album> a = em.createQuery("SELECT a FROM Album a", Album.class).getResultList();
		assertThat(a, containsInAnyOrder(album1, album2, album3, album4));
	}
	
	@Test
	public void saveTest1_3() throws DaoException {
		
		// save multiple new Album entities on non-empty DB
		
		fillEM();
		
		em.getTransaction().begin();
		albumDao.save(album4);
		albumDao.save(album5);
		em.getTransaction().commit();

		List<Album> a = em.createQuery("SELECT a FROM Album a", Album.class).getResultList();
		assertThat(a, containsInAnyOrder(album1, album2, album3, album4, album5));
	}
	
	@Test
	public void saveTest1_4() throws DaoException {
		
		// save copy of already existent Album entity on non-empty DB
		
		fillEM();
		
		em.getTransaction().begin();
		albumDao.save(album1);
		em.getTransaction().commit();

		List<Album> a = em.createQuery("SELECT a FROM Album a", Album.class).getResultList();
		assertThat(a, containsInAnyOrder(album1, album2, album3));
	}
	
	@Test
	public void saveTest1_5() throws DaoException {
		
		// save multiple copies of a new Album entity on non-empty DB
		
		fillEM();
		
		em.getTransaction().begin();
		albumDao.save(album4);
		albumDao.save(album4);
		em.getTransaction().commit();
		
		List<Album> a = em.createQuery("SELECT a FROM Album a", Album.class).getResultList();
		assertThat(a, containsInAnyOrder(album1, album2, album3, album4));
	}
	
	@Test
	public void saveTest1_6() throws DaoException {
		
		// save new version of already existent Album entity on non-empty DB
		
		fillEM();
		
		em.getTransaction().begin();
		albumDao.save(album1B);
		em.getTransaction().commit();
		
		List<Album> a = em.createQuery("SELECT a FROM Album a", Album.class).getResultList();
		assertThat(a, containsInAnyOrder(album1B, album2, album3));
	}
	
	@Test
	public void saveTest1_7() throws DaoException {
		
		// save multiple versions of an already existent Album entity on non-empty DB
		
		fillEM();
		
		em.getTransaction().begin();
		albumDao.save(album1);
		albumDao.save(album1B);
		em.getTransaction().commit();
		
		List<Album> a = em.createQuery("SELECT a FROM Album a", Album.class).getResultList();
		assertThat(a, containsInAnyOrder(album1B, album2, album3));
	}
	
	@Test
	public void saveTest1_8() throws DaoException {
		
		// save multiple versions of an already existent Album entity on non-empty DB
		
		fillEM();
		
		em.getTransaction().begin();
		albumDao.save(album1B);
		albumDao.save(album1);
		em.getTransaction().commit();
		
		List<Album> a = em.createQuery("SELECT a FROM Album a", Album.class).getResultList();
		assertThat(a, containsInAnyOrder(album1, album2, album3));
	}
	
	@Test
	public void saveTest2_1() throws DaoException {
		
		// save null on empty DB
		
		exception.expect(DaoException.class);
				
		em.getTransaction().begin();
		albumDao.save(null);
		em.getTransaction().commit();
		
		List<Album> a = em.createQuery("SELECT a FROM Album a", Album.class).getResultList();
		assertThat(a, containsInAnyOrder(album1));
	}
	
	@Test
	public void saveTest2_2() throws DaoException {
		
		// save new Album entity on empty DB

		em.getTransaction().begin();
		albumDao.save(album1);
		em.getTransaction().commit();

		List<Album> a = em.createQuery("SELECT a FROM Album a", Album.class).getResultList();
		assertThat(a, containsInAnyOrder(album1));
	}
	
	@Test
	public void saveTest2_3() throws DaoException {
		
		// save multiple new Album entities on empty DB
		
		em.getTransaction().begin();
		albumDao.save(album4);
		albumDao.save(album5);
		em.getTransaction().commit();
		

		List<Album> a = em.createQuery("SELECT a FROM Album a", Album.class).getResultList();
		assertThat(a, containsInAnyOrder(album4, album5));
	}
	
	@Test
	public void saveTest2_4() throws DaoException {
		
		// save multiple versions of a new Album entity on empty DB
		
		em.getTransaction().begin();
		albumDao.save(album1);
		albumDao.save(album1B);
		em.getTransaction().commit();
		

		List<Album> a = em.createQuery("SELECT a FROM Album a", Album.class).getResultList();
		assertThat(a, containsInAnyOrder(album1B));
	}
	
	@Test
	public void getTest1_1() throws DaoException {
		
		// get null from DB
		
		fillEM();
		
		exception.expect(DaoException.class);
		
		albumDao.get(null);

		List<Album> a = em.createQuery("SELECT a FROM Album a", Album.class).getResultList();
		assertThat(a, containsInAnyOrder(album1B, album2, album3));
	}
	
	@Test
	public void getTest1_2() throws DaoException {
		
		// get existent entity (by UUID) from DB
		
		fillEM();
		
		assertEquals(albumDao.get(uUID1), em.find(Album.class, uUID1));

		List<Album> a = em.createQuery("SELECT a FROM Album a", Album.class).getResultList();
		assertThat(a, containsInAnyOrder(album1B, album2, album3));
	}
	
	@Test
	public void getTest1_3() throws DaoException {
		
		// get non-existent entity (by UUID) from DB
		
		fillEM();
		
		assertEquals(albumDao.get(uUID5), null);

		List<Album> a = em.createQuery("SELECT a FROM Album a", Album.class).getResultList();
		assertThat(a, containsInAnyOrder(album1B, album2, album3));
	}
	
	@Test
	public void getTest2_1() throws DaoException {
		
		// get null from empty DB
		
		exception.expect(DaoException.class);
		
		albumDao.get(null);
		
		List<Album> a = em.createQuery("SELECT a FROM Album a", Album.class).getResultList();
		assertThat(a, IsEmptyCollection.empty());
	}
	
	@Test
	public void getTest2_2() throws DaoException {
		
		// get non-existent entity from empty DB
		
		assertNull(albumDao.get(uUID1));
		
		List<Album> a = em.createQuery("SELECT a FROM Album a", Album.class).getResultList();
		assertThat(a, IsEmptyCollection.empty());
	}
	
	@Test
	public void getAllTest1_1() throws DaoException {
		
		// get all entities (as list) entities from DB
		
		fillEM();
		
		assertThat("List equality without order", albumDao.getAll(), containsInAnyOrder(album1, album2, album3));
	}
	
	@Test
	public void getAllTest2_1() throws DaoException {
		
		// get all entities (as list) from empty DB
		
		assertThat("List equality without order", albumDao.getAll(), IsEmptyCollection.empty());
	}
	
	@Test
	public void deleteTest1_1() throws DaoException {
		
		// delete null from DB
		
		fillEM();
		
		exception.expect(DaoException.class);
		
		em.getTransaction().begin();
		albumDao.delete(null);
		em.getTransaction().commit();

		List<Album> a = em.createQuery("SELECT a FROM Album a", Album.class).getResultList();
		assertThat(a, containsInAnyOrder(album1, album2, album3));	
	}
	
	@Test
	public void deleteTest1_2() throws DaoException {
		
		//delete existent Album entity from DB
		
		fillEM();
		
		em.getTransaction().begin();
		albumDao.delete(uUID3);
		em.getTransaction().commit();
		
		assertEquals(null, em.find(Album.class, uUID3));
		
		List<Album> a = em.createQuery("SELECT a FROM Album a", Album.class).getResultList();
		assertThat(a, containsInAnyOrder(album1, album2));	
	}
	
	@Test
	public void deleteTest1_3() throws DaoException {
		
		//delete non-existent Album entity from DB
		
		fillEM();
		
		exception.expect(DaoException.class);
		
		em.getTransaction().begin();
		albumDao.delete(uUID5);
		em.getTransaction().commit();
		
		List<Album> a = em.createQuery("SELECT a FROM Album a", Album.class).getResultList();
		assertThat(a, containsInAnyOrder(album1, album2, album3));
	}
	
	@Test
	public void deleteTest2_1() throws DaoException {
		
		// delete null from empty DB
		
		exception.expect(DaoException.class);
		
		em.getTransaction().begin();
		albumDao.delete(null);
		em.getTransaction().commit();
		
		List<Album> a = em.createQuery("SELECT a FROM Album a", Album.class).getResultList();
		assertThat(a, IsEmptyCollection.empty());
	}
	
	@Test
	public void deleteTest2_2() throws DaoException {
		
		// delete null from empty DB
		
		em.getTransaction().begin();
		albumDao.delete(uUID1);
		em.getTransaction().commit();
		
		List<Album> a = em.createQuery("SELECT a FROM Album a", Album.class).getResultList();
		assertThat(a, IsEmptyCollection.empty());
	}
	
}
