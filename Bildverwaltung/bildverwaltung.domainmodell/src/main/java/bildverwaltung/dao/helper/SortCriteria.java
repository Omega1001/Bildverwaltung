package bildverwaltung.dao.helper;

import javax.persistence.metamodel.SingularAttribute;

/**
 * This class represents a criteria on which the database output shall be
 * sorted<br>
 * 
 * @author Jannik
 * @param <E>
 *            Type of the entity
 *
 */
public class SortCriteria<E> {

	private final SingularAttribute<E, ?> field;
	private final SortOrder order;

	/**
	 * Creates a new Sort Criteria
	 * 
	 * @param field
	 *            to be sorted on
	 * @param order
	 *            Sort order to apply
	 */
	public SortCriteria(SingularAttribute<E, ?> field, SortOrder order) {
		super();
		this.field = field;
		this.order = order;
	}

	/**
	 * @return the field
	 */
	public SingularAttribute<E, ?> getField() {
		return field;
	}

	/**
	 * @return the order
	 */
	public SortOrder getOrder() {
		return order;
	}

}
