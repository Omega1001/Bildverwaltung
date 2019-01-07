package bildverwaltung.container;

public class FactoryImpl<T> implements Factory<String> {
    /**
     * Generates an Implementation of type T and puts it into the scopeContainer<br>
     * While doing so, the implementation is able to request other dependent Implementations, to use those while creating an implementation<br>
     * The returned value must be not null<br>
     * in case of an error, an {@link ContainerException} is thrown<br>
     *
     * @param container      Parent to be used for requesting additional dependencies
     * @param scope          in that the implementation is to be stored in
     * @param scopeContainer Managed container, in that the scopes are kept
     * @return an implementation of the interface T
     * @throws ContainerException if there was an error during generating the implementation
     */
    @Override
    public String generate(ManagedContainer container, Scope scope, ScopeContainer scopeContainer) {
        return "Hurensohn";
    }
}
