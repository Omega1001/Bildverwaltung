package bildverwaltung.dao.helper;

/**
 * This enum represents a series of methods of comparison to be used in the
 * search framework
 * 
 * @author Jannik
 *
 */
public enum ComparisonMode {
	/**
	 * Specifies that a filter is not in use
	 */
	DISABLED,
	/**
	 * Specifies, that a value has to be the exact same as the provided to match
	 * @see #NOT_EQUAL
	 */
	IS_EQUAL,

	/**
	 * Negation of {@link #IS_EQUAL}<br>
	 * See {@link #IS_EQUAL} to learn more
	 * @see #IS_EQUAL 
	 */
	NOT_EQUAL, 
	/**
	 * Specifies that a value has to be between two values
	 * @see #NOT_BETWEEN
	 */
	IS_BETWEEN,
	/**
	 * Negation of {@link #IS_BETWEEN}<br>
	 * See {@link #IS_BETWEEN} to learn more
	 * @see #IS_BETWEEN 
	 */
	NOT_BETWEEN;

}
