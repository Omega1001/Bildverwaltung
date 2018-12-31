package bildverwaltung.dao.helper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.metamodel.SingularAttribute;

/**
 * 
 * This class represents a series of filter<br>
 * This class distinguishes between "<b>Entity Owned Filters</b>" and
 * "<b>Foreign Filters</b>"
 * <p>
 * <b>Entity Owned Filters</b> are defined as filters that is applied to a true
 * field of the entity.<br>
 * That means, that the field must belong to the entity rather then to joined
 * entity<br>
 * <b>Foreign Filters</b> are defined as filters that belong to a entity that is
 * joined onto the master entity<br>
 * That means all OneToOne, OneToMany, ManyToOne and ManyToMany
 * relationships<br>
 * 
 * @author Jannik
 *
 * @param <En>
 *            The base entity type this DataFilter is for
 */
public class DataFilter<En> {

	private final Class<En> entityClass;
	private final List<FilterDiscriptor<En, ?>> entityOwnedFilters = new LinkedList<>();
	private final Map<SingularAttribute<En, ?>, DataFilter<?>> foreignFilters = new HashMap<>();

	/**
	 * Creates a new, empty DataFilter
	 * 
	 * @param entityClass
	 *            Type of the base entity
	 */
	public DataFilter(Class<En> entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 * Adds a <b>Entity Owned Filter</b> to this {@link DataFilter}
	 * 
	 * @param filter
	 *            to be added
	 */
	public void addOwnedFilter(FilterDiscriptor<En, ?> filter) {
		entityOwnedFilters.add(filter);
	}

	/**
	 * Adds a <b>Foreign Filter</b> to this {@link DataFilter}<br>
	 * Also passed is the relationship between the base entity and the entity that
	 * owns this filter
	 * 
	 * @param filter
	 *            to be added
	 * @param joinOver
	 *            Attribute that is used for the join from the base entity to the
	 *            entity that ownes the passed filter
	 */
	@SuppressWarnings("unchecked") // Checked in code
	public <Ten> void addForeignFilter(FilterDiscriptor<Ten, ?> filter, SingularAttribute<En, Ten> joinOver) {
		DataFilter<?> dfRaw = foreignFilters.get(joinOver);
		DataFilter<Ten> df = null;
		if (dfRaw == null) {
			df = new DataFilter<>(joinOver.getJavaType());
		} else if (dfRaw.getEntityClass().equals(joinOver.getJavaType())) {
			df = (DataFilter<Ten>) dfRaw;
		} else {
			// This should be impossible ...
			throw new IllegalStateException("Fetched sub filter don't have a matching Type");
		}
		df.addOwnedFilter(filter);
	}

	public Class<En> getEntityClass() {
		return entityClass;
	}

	public List<FilterDiscriptor<En, ?>> getEntityOwnedFilters() {
		return entityOwnedFilters;
	}

	public Map<SingularAttribute<En, ?>, DataFilter<?>> getForeignFilters() {
		return foreignFilters;
	}

}
