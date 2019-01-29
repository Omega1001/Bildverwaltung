package bildverwaltung.container;

/**
 * This Enum is a collection of all scopes supported by the container<br>
 * The following Scopes currently exists:
 * <table>
 * <tr>
 * <td>{@link #APPLICATION}</td>
 * <td>This scope supports no subScopes<br>
 * This scope is to be generated upon the initialisation of the container<br>
 * This scope can not be ended, unless by stopping the container</td>
 * </tr>
 * <tr>
 * <td>{@link #DEFAULT}</td>
 * <td>This scope is not actually a real scope<br>
 * Implementations created by this scope are not stored in any way<br>
 * Because this container actually holds no data, subScopes are ignored<br>
 * Implementations created with this scope are not to be reused
 * </td>
 * </tr>
 * <tr>
 *     <td>{@link #CREATIONALCONTEXT}</td>
 *     This scope is always created temporary when trying to materialize a interface in ManagedContainer.
 *     It is used to keep an "context" between all the different objects that will be created or used
 *     while the "materializing" process.
 * </tr>
 * </table>
 * 
 * @author jannik
 *
 */
public enum Scope {
	APPLICATION, DEFAULT,CREATIONALCONTEXT;
}
