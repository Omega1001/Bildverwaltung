package bildverwaltung.dao;

import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.DaoException;
import bildverwaltung.dao.impl.PictureDaoImpl;

import java.net.URI;
import java.net.URISyntaxException;

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

public class PictureDaoImplTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	private TestDBEntityManagerFactory emf = new TestDBEntityManagerFactory();
	private EntityManager em = emf.get();

	private PictureDaoImpl picDao = new PictureDaoImpl(em);

	// picX Date = 200X-0X-0X
	private Picture pic1;
	private Picture pic1B;
	private UUID uUID1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
	private Picture pic2;
	private UUID uUID2 = UUID.fromString("00000000-0000-0000-0000-000000000002");
	private Picture pic3;
	private UUID uUID3 = UUID.fromString("00000000-0000-0000-0000-000000000003");
	private Picture pic4;
	private UUID uUID4 = UUID.fromString("00000000-0000-0000-0000-000000000004");
	private Picture pic5;
	private UUID uUID5 = UUID.fromString("00000000-0000-0000-0000-000000000005");

	@Before
	public void setUp() {
		try {
			pic1 = new Picture("black", new URI("file", "//bildverwaltung.dao.impl/src/test/resources/black.png", null),
					null, ".png", 4, 128, new Date(978307200), "Is black.");
			pic1.setId(uUID1);
			pic1B = new Picture("Bee", new URI("file", "//bildverwaltung.dao.impl/src/test/resources/Bee.png", null),
					null, ".jpg", 16, 16, new Date(978307200), "Is Bee.");
			pic1B.setId(uUID1);
			pic2 = new Picture("blue", new URI("file", "//bildverwaltung.dao.impl/src/test/resources/blue.png", null),
					null, ".png", 8, 64, new Date(1012608000), "Is blue.");
			pic2.setId(uUID2);
			pic3 = new Picture("green", new URI("file", "//bildverwaltung.dao.impl/src/test/resources/green.png", null),
					null, ".png", 16, 32, new Date(1046649600), "Is green.");
			pic3.setId(uUID3);
			pic4 = new Picture("red", new URI("file", "//bildverwaltung.dao.impl/src/test/resources/red.png", null),
					null, ".png", 32, 16, new Date(1081036800), "Is red.");
			pic4.setId(uUID4);
			pic5 = new Picture("white", new URI("file", "//bildverwaltung.dao.impl/src/test/resources/white.png", null),
					null, ".png", 64, 8, new Date(1115251200), "Is white.");
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

		// save null on non-empty DB
		
		fillEM();
		
		exception.expect(DaoException.class);
		
		em.getTransaction().begin();
		picDao.save(null);
		em.getTransaction().commit();
		
		List<Picture> p = em.createQuery("SELECT p FROM PICTURE p", Picture.class).getResultList();
		assertThat(p, containsInAnyOrder(pic1, pic2, pic3));
	}

	@Test
	public void saveTest1_2() throws DaoException {

		// save new Picture entity on non-empty DB
		
		fillEM();

		em.getTransaction().begin();
		picDao.save(pic4);
		em.getTransaction().commit();
		
		List<Picture> p = em.createQuery("SELECT p FROM PICTURE p", Picture.class).getResultList();
		assertThat(p, containsInAnyOrder(pic1, pic2, pic3, pic4));
	}

	@Test
	public void saveTest1_3() throws DaoException {

		// save multiple new Picture entities on non-empty DB
		
		fillEM();

		em.getTransaction().begin();
		picDao.save(pic4);
		picDao.save(pic5);
		em.getTransaction().commit();
		
		List<Picture> p = em.createQuery("SELECT p FROM PICTURE p", Picture.class).getResultList();
		assertThat(p, containsInAnyOrder(pic1, pic2, pic3, pic4, pic5));
	}

	@Test
	public void saveTest1_4() throws DaoException {
		
		// save copy of already existent Picture entity on non-empty DB

		fillEM();

		em.getTransaction().begin();
		picDao.save(pic1);
		em.getTransaction().commit();

		List<Picture> p = em.createQuery("SELECT p FROM PICTURE p", Picture.class).getResultList();
		assertThat(p, containsInAnyOrder(pic1, pic2, pic3));
	}

	@Test
	public void saveTest1_5() throws DaoException {

		// save multiple copies of a new Picture entity on non-empty DB
		
		fillEM();

		em.getTransaction().begin();
		picDao.save(pic4);
		picDao.save(pic4);
		em.getTransaction().commit();
		
		List<Picture> p = em.createQuery("SELECT p FROM PICTURE p", Picture.class).getResultList();
		assertThat(p, containsInAnyOrder(pic1, pic2, pic3, pic4));
	}

	@Test
	public void saveTest1_6() throws DaoException {

		// save new version of already existent Picture entity on non-empty DB
		
		fillEM();

		em.getTransaction().begin();
		picDao.save(pic1B);
		em.getTransaction().commit();
		
		List<Picture> p = em.createQuery("SELECT p FROM PICTURE p", Picture.class).getResultList();
		assertThat(p, containsInAnyOrder(pic1B, pic2, pic3));
	}
	
	@Test
	public void saveTest1_7() throws DaoException {

		// save multiple versions of an already existent Picture entity on non-empty DB
		
		fillEM();

		em.getTransaction().begin();
		picDao.save(pic1);
		picDao.save(pic1B);
		em.getTransaction().commit();
		
		List<Picture> p = em.createQuery("SELECT p FROM PICTURE p", Picture.class).getResultList();
		assertThat(p, containsInAnyOrder(pic1B, pic2, pic3));
	}
	
	@Test
	public void saveTest1_8() throws DaoException {

		// save multiple versions of an already existent Picture entity on non-empty DB
		
		fillEM();

		em.getTransaction().begin();
		picDao.save(pic1B);
		picDao.save(pic1);
		em.getTransaction().commit();
		
		List<Picture> p = em.createQuery("SELECT p FROM PICTURE p", Picture.class).getResultList();
		assertThat(p, containsInAnyOrder(pic1, pic2, pic3));
	}

	@Test
	public void saveTest2_1() throws DaoException {
		
		// save null on empty DB
		
		exception.expect(DaoException.class);
		
		em.getTransaction().begin();
		picDao.save(null);
		em.getTransaction().commit();
		
		List<Picture> p = em.createQuery("SELECT p FROM PICTURE p", Picture.class).getResultList();
		assertThat(p, IsEmptyCollection.empty());
	}

	@Test
	public void saveTest2_2() throws DaoException {
		
		// save new Picture entity on empty DB

		em.getTransaction().begin();
		picDao.save(pic1);
		em.getTransaction().commit();
	
		List<Picture> p = em.createQuery("SELECT p FROM PICTURE p", Picture.class).getResultList();
		assertThat(p, containsInAnyOrder(pic1));
	}

	@Test
	public void saveTest2_3() throws DaoException {
		
		// save multiple new Picture entities on empty DB

		em.getTransaction().begin();
		picDao.save(pic4);
		picDao.save(pic5);
		em.getTransaction().commit();
		
		List<Picture> p = em.createQuery("SELECT p FROM PICTURE p", Picture.class).getResultList();
		assertThat(p, containsInAnyOrder(pic4, pic5));
	}
	
	@Test
	public void saveTest2_4() throws DaoException {
		
		// save multiple versions of a new Picture entity on empty DB

		em.getTransaction().begin();
		picDao.save(pic1);
		picDao.save(pic1B);
		em.getTransaction().commit();
		
		List<Picture> p = em.createQuery("SELECT p FROM PICTURE p", Picture.class).getResultList();
		assertThat(p, containsInAnyOrder(pic1B));
	}

	@Test
	public void getTest1_1() throws DaoException {

		// get null from DB
		
		fillEM();
		
		exception.expect(DaoException.class);
		picDao.get(null);
		
		List<Picture> p = em.createQuery("SELECT p FROM PICTURE p", Picture.class).getResultList();
		assertThat(p, containsInAnyOrder(pic1, pic2, pic3));
	}

	@Test
	public void getTest1_2() throws DaoException {

		// get existent entity (by UUID) from DB
		
		fillEM();

		assertEquals(picDao.get(uUID1), em.find(Picture.class, uUID1));
		
		List<Picture> p = em.createQuery("SELECT p FROM PICTURE p", Picture.class).getResultList();
		assertThat(p, containsInAnyOrder(pic1, pic2, pic3));
	}

	@Test
	public void getTest1_3() throws DaoException {

		// get non-existent entity (by UUID) from DB
		
		fillEM();

		assertEquals(picDao.get(uUID5), null);

		List<Picture> p = em.createQuery("SELECT p FROM PICTURE p", Picture.class).getResultList();
		assertThat(p, containsInAnyOrder(pic1, pic2, pic3));
	}

	@Test
	public void getTest2_1() throws DaoException {
		
		 // get null from empty DB
		
		exception.expect(DaoException.class);
		
		picDao.get(null);
		
		List<Picture> p = em.createQuery("SELECT p FROM PICTURE p", Picture.class).getResultList();
		assertThat(p, IsEmptyCollection.empty());
	}

	@Test
	public void getTest2_2() throws DaoException {
		
		// get non-existent entity from empty DB

		assertNull(picDao.get(uUID1));

		List<Picture> p = em.createQuery("SELECT p FROM PICTURE p", Picture.class).getResultList();
		assertThat(p, IsEmptyCollection.empty());
	}

	@Test
	public void getAllTest1_1() throws DaoException {
		
		// get all entities (as list) entities from DB
		
		fillEM();

		assertThat("List equality without order", picDao.getAll(), containsInAnyOrder(pic1, pic2, pic3));
	}

	@Test
	public void getAllTest2_1() throws DaoException {
		
		// get all entities (as list) from empty DB

		assertThat("List equality without order", picDao.getAll(), IsEmptyCollection.empty());
	}

	@Test
	public void deleteTest1_1() throws DaoException {

		// delete null from DB
		
		fillEM();
		
		exception.expect(DaoException.class);
		             
		em.getTransaction().begin();
		picDao.delete(null);
		em.getTransaction().commit();
		
		List<Picture> p = em.createQuery("SELECT p FROM PICTURE p", Picture.class).getResultList();
		assertThat(p, containsInAnyOrder(pic1, pic2, pic3));
	}

	@Test
	public void deleteTest1_2() throws DaoException {
		
		//delete existent Picture entity from DB

		fillEM();
		
		em.getTransaction().begin();
		picDao.delete(uUID3);
		em.getTransaction().commit();
		
		List<Picture> p = em.createQuery("SELECT p FROM Picture p", Picture.class).getResultList();
		assertThat(p, containsInAnyOrder(pic1, pic2));
	}

	@Test
	public void deleteTest1_3() throws DaoException {
		
		//delete non-existent Picture entity from DB

		fillEM();
		
		exception.expect(DaoException.class);
		
		em.getTransaction().begin();
		picDao.delete(uUID5);
		em.getTransaction().commit();
		
		List<Picture> p = em.createQuery("SELECT p FROM PICTURE p", Picture.class).getResultList();
		assertThat(p, containsInAnyOrder(pic1, pic2, pic3));
	}

	@Test
	public void deleteTest2_1() throws DaoException {
		
		// delete null from empty DB
		
		exception.expect(DaoException.class);
		
		em.getTransaction().begin();
		picDao.delete(null);
		em.getTransaction().commit();

		List<Picture> p = em.createQuery("SELECT p FROM PICTURE p", Picture.class).getResultList();
		assertThat(p, IsEmptyCollection.empty());
	}

	@Test
	public void deleteTest2_2() throws DaoException {
		
		// delete non-existent entity from empty DB
		
		exception.expect(DaoException.class);
		
		em.getTransaction().begin();
		picDao.delete(uUID1);
		em.getTransaction().commit();

		List<Picture> p = em.createQuery("SELECT p FROM PICTURE p", Picture.class).getResultList();
		assertThat(p, IsEmptyCollection.empty());
	}

}
