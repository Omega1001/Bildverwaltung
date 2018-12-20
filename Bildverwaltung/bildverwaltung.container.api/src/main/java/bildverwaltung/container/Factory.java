package bildverwaltung.container;

public interface Factory<T> {

	public T generate(ManagedContainer container,Scope scope, ScopeContainer scopeContainer);
	
	public default void preDestroy(T toDestroy) {};
}
