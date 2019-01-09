package bildverwaltung.dao;

import java.util.List;

import bildverwaltung.dao.entity.UUIDBase;
import bildverwaltung.dao.exception.DaoException;
import bildverwaltung.dao.helper.FilterCreteria;
import bildverwaltung.dao.helper.SortCreteria;

public interface FilterDao<E extends UUIDBase> {

	public List<E> getFiltered(List<FilterCreteria<?,?,?>> filters, List<SortCreteria> order) throws DaoException;
}
