package bildverwaltung.container;

/**
 * Method to generate an implementation of the interface T<br>
 * Any dependencies to that implementation are to be requested from the container, rather then creating them yourself<br>
 * 
 * The type T must be an interface<br>
 * 
 * @author jannik
 *
 * @param <T>
 */
public interface Factory<T> {
	/**
	 * Generates an Implementation of type T and puts it into the scopeContainer<br>
	 * While doing so, the implementation is able to request other dependent Implementations, to use those while creating an implementation<br>
	 * The returned value must be not null<br>
	 * in case of an error, an {@link ContainerException} is thrown<br>
	 * 
	 * @param container Parent to be used for requesting additional dependencies
	 * @param scope in that the implementation is to be stored in
	 * @return an implementation of the interface T
	 * @throws ContainerException if there was an error during generating the implementation
	 */
	public T generate(ManagedContainer container,Scope scope);
	
	/**
	 * Method to be called, if an implementation is removed from the scope
	 * The default implementation is to do noting 
	 * 
	 * @param toDestroy Implementation to be removed
	 */
	public default void preDestroy(T toDestroy) {};
}
