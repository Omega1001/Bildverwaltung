package bildverwaltung.container;

import java.util.UUID;

public interface ManagedContainer {
	
	public <T> T materialise (Class<T> interfaceClass, Scope scope, UUID scopeId);

	public <T> T materialise (Class<T> interfaceClass, Scope scope);
	
	public <T> T materialise (Class<T> interfaceClass);
	
	public <T> boolean addFactory(Factory<T> factory, Class<T> targetInterface);
	
	public UUID beginCustomScope(Scope scope);
	
	public void endCustomScope(UUID scopeId);
	
}
