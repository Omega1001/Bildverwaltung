/**
 * 
 */
package bildverwaltung.dao;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bildverwaltung.dao.entity.UUIDBase;
import bildverwaltung.dao.exception.DaoException;
import bildverwaltung.dao.exception.ExceptionType;

/**
 * @author jannik
 *
 */
public abstract class AbstractDao<E extends UUIDBase> implements CRUDDao<E>,AutoCloseable{

	private static final Logger LOG = LoggerFactory.getLogger(AbstractDao.class);
	private EntityManager em;
	private Class<E> entityClass;

	/**
	 * 
	 */
	public AbstractDao(Class<E> entityClass, EntityManager entityManager) {
		if (entityClass == null) {
			throw new IllegalArgumentException("Missing entity class");
		}
		if (entityManager == null) {
			throw new IllegalArgumentException("Missing entity manager");
		}
		this.em = entityManager;
		this.entityClass = entityClass;
	}

	protected EntityManager em() {
		return em;
	}

	protected Class<E> getEntityClass() {
		return entityClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bildverwaltung.dao.CRUDDao#save(bildverwaltung.dao.entity.UUIDBase)
	 */
	@Override
	public E save(E toSave) throws DaoException {
		LOG.trace("Enter save toSave={}", toSave);
		if (toSave == null) {
			LOG.error("Entity : {} :Unable to save null ", entityClass.getSimpleName());
			throw new DaoException(ExceptionType.ABS_DAO_0001);
		}
		try {
			E res = em.merge(toSave);
			LOG.trace("Exit save res={}", res);
			return res;
		} catch (Exception e) {
			LOG.error("Entity : {} :Error during saving Entity {} : ", entityClass.getSimpleName(), toSave, e);
			throw new DaoException(ExceptionType.ABS_DAO_0004, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bildverwaltung.dao.CRUDDao#get(java.util.UUID)
	 */
	@Override
	public E get(UUID key) throws DaoException {
		LOG.trace("Enter get key={}", key);
		if (key == null) {
			LOG.error("Entity : {} :Unable to retrieve null ", entityClass.getSimpleName());
			throw new DaoException(ExceptionType.ABS_DAO_0003);
		}
		try {
			E res = em.find(entityClass, key);
			LOG.trace("Exit get res={}", res);
			return res;
		} catch (Exception e) {
			LOG.error("Entity : {} :Error during fetching Entity with key {} : ", entityClass.getSimpleName(), key, e);
			throw new DaoException(ExceptionType.ABS_DAO_0005, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bildverwaltung.dao.CRUDDao#getAll()
	 */
	@Override
	public List<E> getAll() throws DaoException {
		LOG.trace("Enter getAll");
		try {
			CriteriaQuery<E> q = em.getCriteriaBuilder().createQuery(entityClass);
			Root<E> c = q.from(entityClass);
			q.select(c);
			TypedQuery<E> query = em.createQuery(q);
			List<E> res = query.getResultList();
			LOG.trace("Exit getAll res={}", res);
			return res;
		} catch (Exception e) {
			LOG.error("Entity : {} :Error during fetching all Entities : ", entityClass.getSimpleName(), e);
			throw new DaoException(ExceptionType.ABS_DAO_0007, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bildverwaltung.dao.CRUDDao#delete(java.util.UUID)
	 */
	@Override
	public void delete(UUID key) throws DaoException {
		LOG.trace("Enter delete key={}", key);
		if (key == null) {
			LOG.error("Entity : {} :Unable to save null ", entityClass.getSimpleName());
			throw new DaoException(ExceptionType.ABS_DAO_0002);
		}
		try {
			E obj = get(key);
			if(obj == null) {
				LOG.debug("Entity : {} : Key {} delete is not persistent, doing nothing",entityClass.getSimpleName(),key);
			}else {
				em.remove(obj);
			}
		} catch (Exception e) {
			LOG.error("Entity : {} :Error during removing Entity with key{} : ", entityClass.getSimpleName(), key, e);
			throw new DaoException(ExceptionType.ABS_DAO_0006, e);
		}
		LOG.trace("Exit delete");
	}
	
	@Override
	public void close() throws Exception {
		LOG.trace("Enter close");
		em.close();
		LOG.trace("Exit close");
	}

}
