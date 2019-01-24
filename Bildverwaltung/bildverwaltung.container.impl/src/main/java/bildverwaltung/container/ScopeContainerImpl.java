package bildverwaltung.container;

import java.util.*;

public class ScopeContainerImpl implements ScopeContainer {

    private final UUID scopeId;
    Map<UUID,ScopeContainer> subScopes;
    private Scope managedScope;
    private Map<Factory<?>, Object> factories; //speichert (factory,implementation) paare

    public ScopeContainerImpl(Scope scope, UUID scopeId) {

        this.managedScope = scope;

        if(scope == Scope.APPLICATION) {

            subScopes = new HashMap<>();
            subScopes.put(scopeId, this);

        } else {
            subScopes= new HashMap<>();
        }

        factories = new HashMap<>();
        this.scopeId = scopeId;

    }

    public ScopeContainerImpl(Scope scope) {
        this(scope,null);
    }



    public UUID getScopeId() {
        return scopeId;
    }

    @Override
    public Scope getManagedScope() {
        return managedScope;
    }

    public UUID beginSubScope(){

        //TODO do we need it?
        //Methode bereits in ManagedContainer
        //Subscopes werden aber anscheinend trotzdem im ScopeContainer gemanaged, also wirds auch hier erstellt

        UUID newSubScopeId = UUID.randomUUID();

        ScopeContainer newSubScope = new ScopeContainerImpl(managedScope,newSubScopeId);

        subScopes.put(newSubScopeId, newSubScope);

        return newSubScopeId;

    }

    @Override
    public void endSubScope(UUID subScopeId) {
               Map factories= subScopes.remove(subScopeId).getFactories();
               factories.forEach((k,v)->{((Factory)k).preDestroy(v);});
               factories.clear();
    }

    @Override
    public ScopeContainer getSubScope(UUID subScopeId) {
        if(subScopes.containsKey(subScopeId)&& subScopes.get(subScopeId)!= null) {
            return subScopes.get(subScopeId);
        } else {
            throw new ContainerException("Scope does not have a subscope with such ID.");
        }
    }

    @Override
    public boolean hasImplementationForFactory(Factory<?> factory) {
        return factories.get(factory) != null;
    }

    @Override
    public boolean hasImplementationForFactory(Factory<?> factory, UUID subScopeId) {
        return getSubScope(subScopeId).getImplementationForFactory(factory) != null;
    }

    @Override
    public <T> T getImplementationForFactory(Factory<T> factory) {
        return (T) factories.get(factory);
    }

    @Override
    public <T> T getImplementationForFactory(Factory<T> factory, UUID subScopeId) {
        return getSubScope(subScopeId).getImplementationForFactory(factory);
    }

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

    public Map<Factory<?>, Object> getFactories() {
        return factories;
    }

}
