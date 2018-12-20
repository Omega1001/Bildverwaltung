package bildverwaltung.container;

import java.util.List;
import java.util.UUID;

/**
 * The {@link ManagedContainer} represents the main interaction-point<br>
 * It is used to request components as needed<br>
 * 
 * @author jannik
 *
 */
public interface ManagedContainer {
	/**
	 * This method materializes an implementation of a certain interface<br>
	 * The returned implementation will be associated with the specified scope and
	 * subScope<br>
	 * If the specified Scope and subScope has no implementation associated with
	 * this interface, a new implementation is created and returned
	 * <p>
	 * This method throws a {@link ContainerException} if:
	 * <ul>
	 * <li>the passed interfaceClass is not an interface</li>
	 * <li>there is no factory for this interface</li>
	 * <li>there is more than one factory for this interface</li>
	 * <li>there was an error during creating the implementation</li>
	 * </ul>
	 * 
	 * @param interfaceClass Class to be materialized
	 * @param scope          to take/put implementation from/to
	 * @param scopeId        Id of the subScope to use or 'null' to use primaryScope
	 * @return an implementation of the supplied interface
	 * @throws ContainerException if there was an error
	 */
	public <T> T materialize(Class<T> interfaceClass, Scope scope, UUID scopeId);

	/**
	 * This method materializes an implementation of a certain interface<br>
	 * This method does the same thing as {@link #materialize(Class, Scope, UUID)}
	 * with 'null' as third parameter<br>
	 * 
	 * The implementation will be fetched from the primary Scope of the specified
	 * scope<br>
	 * For more informations, see {@link #materialize(Class, Scope, UUID)}
	 * 
	 * @param interfaceClass Class to be materialized
	 * @param scope          to take/put implementation from/to
	 * @return an implementation of the supplied interface
	 * @throws ContainerException if there was an error
	 * @see #materialize(Class, Scope, UUID)
	 */
	public <T> T materialize(Class<T> interfaceClass, Scope scope);

	/**
	 * This method materializes an implementation of a certain interface<br>
	 * This method does the same thing as {@link #materialize(Class, Scope, UUID)}
	 * with 'DEFAULT' as second and 'null' as third parameter<br>
	 * 
	 * The implementation will be freshly created and not be associated with a
	 * scope<br>
	 * See {@link Scope#DEFAULT} for more informations For more informations, see
	 * {@link #materialize(Class, Scope, UUID)}
	 * 
	 * @param interfaceClass Class to be materialized
	 * @return an implementation of the supplied interface
	 * @throws ContainerException if there was an error
	 * @see #materialize(Class, Scope, UUID)
	 */
	public <T> T materialize(Class<T> interfaceClass);

	/**
	 * This method does the same as {@link #materialize(Class, Scope, UUID)} Only
	 * that this method will return a implementation instead of throwing an
	 * exception if there is more than one factory present
	 * 
	 * @param interfaceClass Class to be materialized
	 * @param scope          to take/put implementation from/to
	 * @param scopeId        Id of the subScope to use or 'null' to use primaryScope
	 * @return an implementation of the supplied interface
	 * @throws ContainerException if there was an error
	 */
	public <T> T materializeAny(Class<T> interfaceClass, Scope scope, UUID scopeId);

	/**
	 * This method does the same as {@link #materialize(Class, Scope)} Only that
	 * this method will return a implementation instead of throwing an exception if
	 * there is more than one factory present
	 * 
	 * @param interfaceClass Class to be materialized
	 * @param scope          to take/put implementation from/to
	 * @return an implementation of the supplied interface
	 * @throws ContainerException if there was an error
	 */
	public <T> T materializeAny(Class<T> interfaceClass, Scope scope);

	/**
	 * This method does the same as {@link #materialize(Class)} Only that this
	 * method will return a implementation instead of throwing an exception if there
	 * is more than one factory present
	 * 
	 * @param interfaceClass Class to be materialized
	 * @return an implementation of the supplied interface
	 * @throws ContainerException if there was an error
	 */
	public <T> T materializeAny(Class<T> interfaceClass);

	/**
	 * This method materializes all implementation of a certain interface<br>
	 * The returned implementations will be associated with the specified scope and
	 * subScope<br>
	 * If the specified Scope and subScope has no implementation associated with
	 * this interface, a new implementation is created and returned
	 * <p>
	 * This method throws a {@link ContainerException} if:
	 * <ul>
	 * <li>the passed interfaceClass is not an interface</li>
	 * <li>there is no factory for this interface</li>
	 * <li>there was an error during creating the implementation</li>
	 * </ul>
	 * 
	 * @param interfaceClass Class to be materialized
	 * @param scope          to take/put implementation from/to
	 * @param scopeId        Id of the subScope to use or 'null' to use primaryScope
	 * @return all implementation of the supplied interface
	 * @throws ContainerException if there was an error
	 */
	public <T> List<T> materializeAll(Class<T> interfaceClass, Scope scope, UUID scopeId);

	/**
	 * This method materializes all implementation of a certain interface<br>
	 * This method does the same thing as
	 * {@link #materializeAll(Class, Scope, UUID)} with 'null' as third
	 * parameter<br>
	 * 
	 * The implementation will be fetched from the primary Scope of the specified
	 * scope<br>
	 * For more informations, see {@link #materialize(Class, Scope, UUID)}
	 * 
	 * @param interfaceClass Class to be materialized
	 * @param scope          to take/put implementation from/to
	 * @return an implementation of the supplied interface
	 * @throws ContainerException if there was an error
	 * @see #materialize(Class, Scope, UUID)
	 */
	public <T> List<T> materializeAll(Class<T> interfaceClass, Scope scope);

	/**
	 * This method materializes all implementation of a certain interface<br>
	 * This method does the same thing as
	 * {@link #materializeAll(Class, Scope, UUID)} with 'null' as third
	 * parameter<br>
	 * 
	 * All implementations will be freshly created and not associated with a
	 * scope<br>
	 * See {@link Scope#DEFAULT} for more informations For more informations, see
	 * {@link #materialize(Class, Scope, UUID)}
	 * 
	 * @param interfaceClass Class to be materialized
	 * @return an implementation of the supplied interface
	 * @throws ContainerException if there was an error
	 * @see #materialize(Class, Scope, UUID)
	 */
	public <T> List<T> materializeAll(Class<T> interfaceClass);

	/**
	 * Adds a {@link Factory} to the container to be used
	 * 
	 * @param factory         to be added
	 * @param targetInterface The implementations interface created by this factory
	 */
	public <T> void addFactory(Factory<T> factory, Class<T> targetInterface);

	/**
	 * This method creates a new subScope
	 * 
	 * @param scope to create subScope in
	 * @return the subScopeId of the new subScope
	 */
	public UUID beginCustomScope(Scope scope);

	/**
	 * Ends a subScope<br>
	 * 
	 * @param scope Referenced scope
	 * @param scopeId of the subScope to be ended
	 */
	public void endCustomScope(Scope scope, UUID scopeId);

}
