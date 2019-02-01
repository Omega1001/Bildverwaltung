package bildverwaltung.container;

import java.util.Map;
import java.util.UUID;

/**
 * This class is used to represent a Scope inside the Container
 * <p>
 * A Scope may have independent subScopes<br>
 * A subscope is to be handled as if it where isolated<br>
 * It is possible to place 0 or 1 implementation for each Factory into every
 * subScope
 * <p>
 * Every container contains at least one subScope, the primary Scope<br>
 * The primary Scope is referenced by the subScopeId null<br>
 * If no subscope is specified, the primaryScope is used by default
 * 
 * @author jannik
 *
 */
public interface ScopeContainer {

	/**
	 * Returnes the scope that is managed by this container
	 * 
	 * @return
	 */
	public Scope getManagedScope();

	/**
	 * Checks whether the primary scope has a implementation associated with a
	 * specific factory
	 * <p>
	 * This method is does the same thing as
	 * {@link #hasImplementationForFactory(Factory, UUID)} with 'null' as second
	 * parameter
	 * 
	 * @param factory that is to be checked
	 * @return if the passed factory produced an implementation that is stored in
	 *         the primary scope
	 */
	public boolean hasImplementationForFactory(Factory<?> factory);

	/**
	 * Checks whether the specified subScope has a implementation associated with a
	 * specific factory <br>
	 * 
	 * @param factory    that is to be checked
	 * @param subScopeId id of the subScope to check
	 * @return if the passed factory produced an implementation that is stored in
	 *         the primary scope
	 */
	public boolean hasImplementationForFactory(Factory<?> factory, UUID subScopeId);

	/**
	 * This method returns the implementation associated with the specified factory
	 * from the primary scope<br>
	 * This method does the same as
	 * {@link #getImplementationForFactory(Factory, UUID)} with 'null' as second
	 * parameter
	 * 
	 * @param factory the implementation is associated with
	 * @return the associated implementation
	 */
	public <T> T getImplementationForFactory(Factory<T> factory);

	/**
	 * This method returns the implementation associated with the specified factory
	 * from the subScope with the specified subScopeId<br>
	 * 
	 * @param factory    the implementation is associated with
	 * @param subScopeId id of the subScope to look into
	 * @return the associated implementation from the specified subScope or null if
	 *         no such implementation exists
	 */
	public <T> T getImplementationForFactory(Factory<T> factory, UUID subScopeId);

	/**
	 * Associates the implementation with the specified factory in the primary
	 * scope<br>
	 * This method does the same as
	 * {@link #setImplementationForFactory(Object, Factory, UUID)} with 'null' as
	 * second parameter
	 * 
	 * @param impl    to be associated
	 * @param factory to be associated with
	 */
	public void setImplementationForFactory(Object impl, Factory<?> factory);

	/**
	 * Associates the implementation with the specified factory in the primary
	 * scope<br>
	 * 
	 * @param impl       to be associated
	 * @param factory    to be associated with
	 * @param subScopeId Id of the subScope to be associated with
	 */
	public void setImplementationForFactory(Object impl, Factory<?> factory, UUID subScopeId);

	/**
	 * begin a new subscope in the given scopeContainer
	 * @return of the new ScopeContainer
	 */
	public UUID beginSubScope();


	/**
	 * This method ends a subScope<br>
	 * If a scope ends, all implementations that are managed in that subScope have
	 * to be dereferenced<br>
	 * Prior to that, the implementation have to be put thru its factories
	 * {@link Factory#preDestroy(Object)} method
	 * 
	 * @param subScopeId of the subScope to be ended
	 */
	public void endSubScope(UUID subScopeId);

	ScopeContainer getSubScope(UUID subScopeId);

	public Map<Factory<?>, Object> getFactories();

}
