package bildverwaltung.container;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ManagedContainerImpl implements ManagedContainer {

    Map<Scope,ScopeContainer> scopeContainer;
    Map<Class,Factory<?>> factories; //speichert (interface, factory) paare



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
    @Override
    public <T> T materialize(Class<T> interfaceClass, Scope scope, UUID scopeId) {
        return null;
    }

    /**
     * This method materializes an implementation of a certain interface<br>
     * This method does the same thing as {@link #materialize(Class, Scope, UUID)}
     * with 'null' as third parameter<br>
     * <p>
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
    @Override
    public <T> T materialize(Class<T> interfaceClass, Scope scope) {
        return null;
    }

    /**
     * This method materializes an implementation of a certain interface<br>
     * This method does the same thing as {@link #materialize(Class, Scope, UUID)}
     * with 'DEFAULT' as second and 'null' as third parameter<br>
     * <p>
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
    @Override
    public <T> T materialize(Class<T> interfaceClass) {
        return null;
    }

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
    @Override
    public <T> T materializeAny(Class<T> interfaceClass, Scope scope, UUID scopeId) {
        return null;
    }

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
    @Override
    public <T> T materializeAny(Class<T> interfaceClass, Scope scope) {
        return null;
    }

    /**
     * This method does the same as {@link #materialize(Class)} Only that this
     * method will return a implementation instead of throwing an exception if there
     * is more than one factory present
     *
     * @param interfaceClass Class to be materialized
     * @return an implementation of the supplied interface
     * @throws ContainerException if there was an error
     */
    @Override
    public <T> T materializeAny(Class<T> interfaceClass) {
        return null;
    }

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
    @Override
    public <T> List<T> materializeAll(Class<T> interfaceClass, Scope scope, UUID scopeId) {
        return null;
    }

    /**
     * This method materializes all implementation of a certain interface<br>
     * This method does the same thing as
     * {@link #materializeAll(Class, Scope, UUID)} with 'null' as third
     * parameter<br>
     * <p>
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
    @Override
    public <T> List<T> materializeAll(Class<T> interfaceClass, Scope scope) {
        return null;
    }

    /**
     * This method materializes all implementation of a certain interface<br>
     * This method does the same thing as
     * {@link #materializeAll(Class, Scope, UUID)} with 'null' as third
     * parameter<br>
     * <p>
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
    @Override
    public <T> List<T> materializeAll(Class<T> interfaceClass) {
        return null;
    }

    /**
     * Adds a {@link Factory} to the container to be used
     *
     * @param factory         to be added
     * @param targetInterface The implementations interface created by this factory
     */
    @Override
    public <T> void addFactory(Factory<T> factory, Class<T> targetInterface) {

    }
    
    @Override
    public <T> void addFactory(Factory<?> factory) {
    	System.out.println();
    }

    /**
     * This method creates a new subScope
     *
     * @param scope to create subScope in
     * @return the subScopeId of the new subScope
     */
    @Override
    public UUID beginCustomScope(Scope scope) {
        return null;
    }

    /**
     * Ends a subScope<br>
     *
     * @param scope   Referenced scope
     * @param scopeId of the subScope to be ended
     */
    @Override
    public void endCustomScope(Scope scope, UUID scopeId) {

    }
}
