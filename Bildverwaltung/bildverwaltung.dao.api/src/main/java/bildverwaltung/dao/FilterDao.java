package bildverwaltung.dao;

import java.util.List;

import bildverwaltung.dao.entity.UUIDBase;
import bildverwaltung.dao.exception.DaoException;
import bildverwaltung.dao.helper.DataFilter;
import bildverwaltung.dao.helper.SortCriteria;

/**
 * Indicating that a dao serving for a specific entity &ltE&gt is able to
 * produce filtered output using the application search engine<br>
 * 
 * @author Jannik
 *
 * @param <E>
 *            The type of entity this dao is serving
 */
public interface FilterDao<E extends UUIDBase> {

	/**
	 * This method is used to fetch all db entries that satisfy the passed criteria
	 * in the passed order
	 * <p>
	 * This method takes a {@link DataFilter} object, that represents, the criteria,
	 * that every object returned by this method must satisfy<br>
	 * If this parameter is null, it will be interpreted as unrestricted
	 * <p>
	 * 
	 * The method also takes a {@link List} of {@link SortCriteria}, that define the
	 * order, in which the objects are to be sorted<br>
	 * If this parameter is null, or a empty {@link List}, the natural order of the
	 * database is used
	 * <p>
	 * 
	 * @param filter
	 *            to apply to the fetched objects
	 * @param order
	 *            to be applied to the fetched objects
	 * @return a {@link List} of all database entries, that satisfy the passed
	 *         filters in the passed order
	 * @throws DaoException
	 *             if there was a problem during building the search request or
	 *             interacting with the database
	 */
	public List<E> getFiltered(DataFilter<E> filter, List<SortCriteria<E>> order) throws DaoException;
}
