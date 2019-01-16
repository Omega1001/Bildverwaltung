package bildverwaltung.container;

import java.util.*;

public class ScopeContainerImpl implements ScopeContainer {

    Map<UUID,ScopeContainerImpl> subScopes;
    Scope managedScope;
    Map<Factory<?>, Object> factories; //speichert (factory,implementation) paare

    public ScopeContainerImpl(Scope scope) {

        this.managedScope = scope;
        if(scope == Scope.APPLICATION) {
            subScopes = null;
        } else {
            subScopes= new HashMap<>();
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
     * @return the associated implementation or null!
     */
    @Override
    public <T> T getImplementationForFactory(Factory<T> factory) {
            return (T) factories.get(factory);
    }

    /**
     * This method returns the implementation associated with the specified factory
     * from the subScope with the specified subScopeId<br>
     *
     * @param factory    the implementation is associated with
     * @param subScopeId id of the subScope to look into
     * @return the associated implementation from the specified subScope or null if
     * no such implementation exists
     */
    @Override
    public <T> T getImplementationForFactory(Factory<T> factory, UUID subScopeId) {
        return getSubScope(subScopeId).getImplementationForFactory(factory);
    }

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
    @Override
    public void setImplementationForFactory(Object impl, Factory<?> factory) {
        factories.put(factory,impl);
    }

    /**
     * Associates the implementation with the specified factory in the primary
     * scope<br>
     *
     * @param impl       to be associated
     * @param factory    to be associated with
     * @param subScopeId Id of the subScope to be associated with
     */
    @Override
    public void setImplementationForFactory(Object impl, Factory<?> factory, UUID subScopeId) {
        subScopes.get(subScopeId).getFactories().put(factory,impl);
    }

    /**
     * This method ends a subScope<br>
     * If a scope ends, all implementations that are managed in that subScope have
     * to be dereferenced<br>
     * Prior to that, the implementation have to be put thru its factories
     * {@link Factory#preDestroy(Object)} method
     *
     * @param subScopeId of the subScope to be ended
     */
    @Override
    public void endSubScope(UUID subScopeId) {
               Map factories= subScopes.remove(subScopeId).getFactories();
               factories.forEach((k,v)->{((Factory)k).preDestroy(v);});
               factories.clear();
    }

    public Map<Factory<?>, Object> getFactories() {
        return factories;
    }

    /*public void beginSubScope(){

        TODO do we need it?
        Methode bereits in ManagedContainer
    }*/

    /**
     * Method to get a SubscopeContainer.
     *
     * @param subScopeId UUID which specifies a SubscopeContainer
     * @throws ContainerException if the ScopeContainer does not contain a SubscopeContainer with specified subScopeId
     * @return ScopeContainer which belongs to the subScopeId
     */
    @Override
    public ScopeContainer getSubScope(UUID subScopeId) {
        if(subScopes.containsKey(subScopeId)&& subScopes.get(subScopeId)!= null) {
            return subScopes.get(subScopeId);
        }else{
            throw new ContainerException("Scope does not have a subscope with such ID.");
        }
    }
}
