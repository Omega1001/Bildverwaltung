package bildverwaltung.dao.helper;

import javax.persistence.metamodel.SingularAttribute;

/**
 * This class is a holder class, that describes a filter<br>
 * 
 * @author Jannik
 *
 * @param <En>
 *            The entity owning this filter
 * @param <V>
 *            The type of value this filter should work withs
 */
public class FilterDiscriptor<En, V> {

	private final SingularAttribute<En, V> attribute;
	private final FilterValueDiscriptor<V> value;
	private final ComparisonMode comparisonMode;

	/**
	 * Convenience Constructor that implicitly creates a
	 * {@link FilterValueDiscriptor} using the provided value<br>
	 * 
	 * See
	 * {@link #FilterDiscriptor(SingularAttribute, FilterValueDiscriptor, ComparisonMode)}
	 * to learn more
	 * 
	 * @param attribute
	 *            Field to be filtered on
	 * @param value
	 *            to be used in comparison
	 * @param comparisonMode
	 *            The method that is to be used during comparison
	 * @see #FilterDiscriptor(SingularAttribute, FilterValueDiscriptor,
	 *      ComparisonMode)
	 */
	public FilterDiscriptor(SingularAttribute<En, V> attribute, V value, ComparisonMode comparisonMode) {
		this(attribute, new FilterValueDiscriptor<>(value), comparisonMode);
	}

	/**
	 * Generates a new {@link FilterDiscriptor}
	 * 
	 * @param attribute
	 *            Field to be filtered on
	 * @param value
	 *            to be used in comparison
	 * @param comparisonMode
	 *            The method that is to be used during comparison
	 */
	public FilterDiscriptor(SingularAttribute<En, V> attribute, FilterValueDiscriptor<V> value,
			ComparisonMode comparisonMode) {
		super();
		this.attribute = attribute;
		this.value = value;
		this.comparisonMode = comparisonMode;
	}

	public SingularAttribute<En, V> getAttribute() {
		return attribute;
	}

	public FilterValueDiscriptor<V> getValue() {
		return value;
	}

	public ComparisonMode getComparisonMode() {
		return comparisonMode;
	}

}
