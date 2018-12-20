package bildverwaltung.dao.helper;

import javax.persistence.metamodel.SingularAttribute;

public class SortCreteria {
	
	private final SingularAttribute<?, ?> field;
	private final SortOrder order;
	
	public SortCreteria(SingularAttribute<?, ?> field, SortOrder order) {
		super();
		this.field = field;
		this.order = order;
	}

	/**
	 * @return the field
	 */
	public SingularAttribute<?, ?> getField() {
		return field;
	}

	/**
	 * @return the order
	 */
	public SortOrder getOrder() {
		return order;
	}
	
	
}
