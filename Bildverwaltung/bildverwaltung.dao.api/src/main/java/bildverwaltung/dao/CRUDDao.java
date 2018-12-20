package bildverwaltung.dao;

import java.util.List;
import java.util.UUID;

import bildverwaltung.dao.entity.UUIDBase;
import bildverwaltung.dao.exception.DaoException;

public interface CRUDDao<E extends UUIDBase> {
	
	public E save (E toSave) throws DaoException;
	
	public E get(UUID key) throws DaoException;
	
	public List<E> getAll()throws DaoException;
	
	public boolean delete (UUID key)throws DaoException;
	
}
