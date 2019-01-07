package bildverwaltung.container;

import java.util.*;

public class ScopeContainerImpl implements ScopeContainer {

    List<UUID> subScopes;
    Scope managedScope;
    Map<Factory<?>, Object> factories;

    public ScopeContainerImpl(Scope scope) {

        this.managedScope = scope;
        if(scope == Scope.APPLICATION) {
            subScopes = null;
        } else {
            subScopes= new ArrayList<UUID>();
        }

        factories = new HashMap<>();

    }

    @Override
    public Scope getManagedScope() {
        return managedScope;
    }

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
     * the primary scope
     */
    @Override
    public boolean hasImplementationForFactory(Factory<?> factory) {
        return factories.get(factory) != null;
    }

    /**
     * Checks whether the specified subScope has a implementation associated with a
     * specific factory <br>
     *
     * @param factory    that is to be checked
     * @param subScopeId id of the subScope to check
     * @return if the passed factory produced an implementation that is stored in
     * the primary scope
     */
    @Override
    public boolean hasImplementationForFactory(Factory<?> factory, UUID subScopeId) {
        if(!subScopes.contains(subScopeId)) {
            throw new ContainerException("Scope does not have a subscope with such ID.");
        }

        return getSubScope(subScopeId).getImplementationForFactory(factory) != null;
    }

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
    @Override
    public <T> T getImplementationForFactory(Factory<T> factory) {
        if(factories.get(factory) == null) {
            //exception
        }
    }
}
