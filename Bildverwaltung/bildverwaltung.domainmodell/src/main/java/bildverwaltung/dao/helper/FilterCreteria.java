package bildverwaltung.dao.helper;

import javax.persistence.metamodel.SingularAttribute;

import bildverwaltung.dao.entity.UUIDBase;

public class FilterCreteria<BE extends UUIDBase, E extends UUIDBase,T> {

	private final Class<BE> baseEntity;
	private final SingularAttribute<E, T> field;
	private final ComparisonMode mode;
	private final T value;
	
	
	public FilterCreteria(Class<BE> baseEntity, SingularAttribute<E, T> field,
			ComparisonMode mode, T value) {
		super();
		this.field = field;
		this.mode = mode;
		this.value = value;
		this.baseEntity = baseEntity;
	}
	/**
	 * @return the field
	 */
	public SingularAttribute<E, T> getField() {
		return field;
	}
	/**
	 * @return the mode
	 */
	public ComparisonMode getMode() {
		return mode;
	}
	/**
	 * @return the value
	 */
	public T getValue() {
		return value;
	}
	public Class<BE> getBaseEntity() {
		return baseEntity;
	}
	
	
	
	
}
