/**
 * 
 */
package bildverwaltung.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bildverwaltung.dao.entity.UUIDBase;
import bildverwaltung.dao.exception.DaoException;
import bildverwaltung.dao.helper.DataFilter;
import bildverwaltung.dao.helper.FilterDiscriptor;
import bildverwaltung.dao.helper.SortCriteria;
import bildverwaltung.dao.helper.SortOrder;

/**
 * This Class extends the {@link AbstractDao} with the ability to retrieve
 * filtered data from the database
 * 
 * @author Jannik
 * @param <E>
 *            the type of entity this dao shall serve for
 *
 */
public abstract class AbstractFilterDao<E extends UUIDBase> extends AbstractDao<E> implements FilterDao<E> {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractFilterDao.class);

	/**
	 * Creates a new {@link AbstractFilterDao}
	 * 
	 * @param entityClass
	 *            Class of the entity to serve
	 * @param entityManager
	 *            to be used to communicate with the database
	 */
	public AbstractFilterDao(Class<E> entityClass, EntityManager entityManager) {
		super(entityClass, entityManager);
	}

	@Override
	public List<E> getFiltered(DataFilter<E> filters, List<SortCriteria<E>> order) throws DaoException {
		LOG.trace("Enter getFiltered filters={}, order={}", filters, order);
		CriteriaBuilder cb = em().getCriteriaBuilder();
		CriteriaQuery<E> query = cb.createQuery(getEntityClass());
		Root<E> root = query.from(getEntityClass());

		List<Predicate> filterPredicates = new LinkedList<>();
		List<Join<E, ?>> joins = new LinkedList<>();
		LOG.debug("Generating Filter Criterias");
		generateFilterAndJoins(filters, joins, filterPredicates, root, cb);
		LOG.debug("Propergating necessary joins");
		query.multiselect(compileSelect(root, joins));
		if (!filterPredicates.isEmpty()) {
			LOG.debug("Propergating where criterias");
			query.where(filterPredicates.toArray(new Predicate[filterPredicates.size()]));
		}
		List<Order> orderList = generateOrderList(order, root, cb);
		if (!orderList.isEmpty()) {
			LOG.debug("Propergating order crierias");
			query.orderBy(orderList);
		}
		LOG.debug("Fetching results");
		List<E> result = em().createQuery(query).getResultList();
		LOG.trace("Exit getFiltered resultLength={}", result.size());
		return result;
	}

	/**
	 * Generates a list of filters to apply, as well as a list of required joins
	 * <p>
	 * Note:<br>
	 * Joins are only needed, if there is actually a filter for one of the joined
	 * attributes<br>
	 * Any necessary data are joined to the fetched objects whether a join exists
	 * here or not
	 * 
	 * @param filters
	 *            to be processed
	 * @param joins
	 *            a not null {@link List} to look into for existing joins and adding
	 *            new ones (call by reference, used as return)
	 * @param filterPredicates
	 *            a not null {@link List} to put created filters into (call by
	 *            reference, used as return)
	 * @param root
	 *            of the request
	 * @param cb
	 *            {@link CriteriaBuilder} to be used
	 */
	private void generateFilterAndJoins(DataFilter<E> filters, List<Join<E, ?>> joins, List<Predicate> filterPredicates,
			Root<E> root, CriteriaBuilder cb) {
		if (filters != null) {
			for (FilterDiscriptor<E, ?> filter : filters.getEntityOwnedFilters()) {
				addFilter(filter, root, cb, filterPredicates);
			}
			for (Entry<SingularAttribute<E, ?>, DataFilter<?>> join : filters.getForeignFilters().entrySet()) {
				Join<E, ?> joinRoot = root.join(join.getKey());
				LOG.trace("Adding Join for column {}", join.getKey().getName());
				joins.add(joinRoot);
				for (FilterDiscriptor<?, ?> filter : join.getValue().getEntityOwnedFilters()) {
					addFilter(filter, joinRoot, cb, filterPredicates);
				}
			}
		}

	}

	/**
	 * Creates a array, that contains the root element and all joins<br>
	 * The element at index 0 has to be the root element, followed by the joins, if
	 * any<br>
	 * There has to be a root element<br>
	 * In case the list of joins are empty, an array with the length of 1 is
	 * returned (containing only the root element)
	 * 
	 * @param root
	 *            element
	 * @param joins
	 *            to be appended
	 * @return an array containing the root element, followed by the passed joins
	 *         (if any)
	 */
	private From<?, ?>[] compileSelect(Root<E> root, List<Join<E, ?>> joins) {
		From<?, ?>[] selection = new From<?, ?>[joins.size() + 1];
		selection[0] = root;
		int i = 1;
		for (Join<E, ?> join : joins) {
			selection[i++] = join;
		}
		return selection;
	}

	/**
	 * Creates a Filter
	 * 
	 * @param filter
	 *            Instructions to create the filter by
	 * @param root
	 *            Entity root that will own this filter
	 * @param cb
	 *            {@link CriteriaBuilder} to be used
	 * @param filterPredicates
	 *            list to put the result in (call by reference, used as return)
	 */
	private void addFilter(FilterDiscriptor<?, ?> filter, From<?, ?> root, CriteriaBuilder cb,
			List<Predicate> filterPredicates) {
		if (root.getJavaType().equals(filter.getAttribute().getDeclaringType().getJavaType())) {
			boolean negate = false;
			Predicate work = null;
			switch (filter.getComparisonMode()) {
			case NOT_EQUAL:
				negate = true;
			case IS_EQUAL:
				work = cb.equal(root.get(filter.getAttribute().getName()), filter.getValue());
				break;
			default:
				LOG.warn("Tried to filter with unsupported mode {}, ignoring ...",filter.getComparisonMode().name());
				break;

			}
			if (work != null) {
				if (LOG.isTraceEnabled()) {
					LOG.trace("Adding Filter for field {} of entity {} with {} {}", filter.getAttribute().getName(),
							filter.getAttribute().getDeclaringType().getClass().getSimpleName(),
							filter.getComparisonMode().name(), filter.getValue());
				}
				filterPredicates.add(negate ? work.not() : work);
			}
		}

	}

	/**
	 * Converts a List of {@link SortCriteria} into a JPA understandable format
	 * 
	 * @param orderInstructions
	 *            Instructions to be created as order statement
	 * @param root
	 *            of the entity
	 * @param cb
	 *            {@link CriteriaBuilder} to be used
	 * @return A {@link List} of JPA understandable order instructions
	 */
	private List<Order> generateOrderList(List<SortCriteria<E>> orderInstructions, Root<E> root, CriteriaBuilder cb) {
		List<Order> res = new LinkedList<>();
		if (orderInstructions != null) {
			for (SortCriteria<E> sortCreteria : orderInstructions) {
				if (getEntityClass().equals(sortCreteria.getField().getDeclaringType().getJavaType())) {
					if (SortOrder.ASC.equals(sortCreteria.getOrder())) {
						res.add(cb.asc(root.get(sortCreteria.getField())));
					} else {
						res.add(cb.desc(root.get(sortCreteria.getField())));
					}
				} else {
					LOG.error("Filtering based upon forigen attributes currently not supported, ignoring ...");
				}
			}
		}
		return res;
	}

}
