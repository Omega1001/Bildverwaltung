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

import bildverwaltung.dao.entity.UUIDBase;
import bildverwaltung.dao.exception.DaoException;

/**
 * @author jannik
 *
 */
public abstract class AbstractDao<E extends UUIDBase> implements CRUDDao<E> {

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
		return em.merge(toSave);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bildverwaltung.dao.CRUDDao#get(java.util.UUID)
	 */
	@Override
	public E get(UUID key) throws DaoException {
		return em.find(entityClass, key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bildverwaltung.dao.CRUDDao#getAll()
	 */
	@Override
	public List<E> getAll() throws DaoException {
		CriteriaQuery<E> q = em.getCriteriaBuilder().createQuery(entityClass);
		Root<E> c = q.from(entityClass);
		q.select(c);
		TypedQuery<E> query = em.createQuery(q);
		return query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bildverwaltung.dao.CRUDDao#delete(java.util.UUID)
	 */
	@Override
	public void delete(UUID key) throws DaoException {
		em.remove(get(key));
	}

}
