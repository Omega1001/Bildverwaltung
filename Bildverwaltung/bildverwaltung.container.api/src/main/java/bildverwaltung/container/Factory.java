package bildverwaltung.container;

/**
 * Method to generate an implementation of the interface T<br>
 * Any dependencies to that implementation are to be requested from the
 * container, rather then creating them yourself<br>
 * 
 * The type T must be an interface<br>
 * 
 * @author jannik
 *
 * @param <T>
 */
public interface Factory<T> extends AutoCloseable {

	/**
	 * Returns the type of an interface that is assignment compatible with the type
	 * returned by {@link #generate(ManagedContainer, Scope)}<br>
	 * The returned Type must be an interface<br>
	 * 
	 * @return the interface for witch this factory supplies an implementation
	 */
	public Class<T> getInterfaceType();

	/**
	 * Generates an Implementation of type T<br>
	 * While doing so, the implementation is able to request other dependent
	 * Implementations, to use those while creating an implementation<br>
	 * The returned value must be not null<br>
	 * The returned value must be assignment compatible with the interface returned by {@link #getInterfaceType()}<br>
	 * In case of an error, an {@link ContainerException} is thrown<br>
	 * 
	 * @param container
	 *            Parent to be used for requesting additional dependencies
	 * @param scope
	 *            in that the implementation is to be stored in
	 * @return an implementation of the interface T
	 * @throws ContainerException
	 *             if there was an error during generating the implementation
	 */
	public T generate(ManagedContainer container, Scope scope);

	/**
	 * Method to be called, if an implementation is removed from the scope The
	 * default implementation is to do noting
	 * 
	 * @param toDestroy
	 *            Implementation to be removed
	 */
	public default void preDestroy(T toDestroy) {}
	
	@Override
	default void close() throws Exception {}
}
