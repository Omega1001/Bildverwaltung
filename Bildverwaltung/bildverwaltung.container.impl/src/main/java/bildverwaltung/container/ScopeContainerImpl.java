package bildverwaltung.container;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScopeContainerImpl implements ScopeContainer, Closeable {
	private static final Logger LOG = LoggerFactory.getLogger(ScopeContainerImpl.class);
	private final UUID scopeId;
	private Map<UUID, ScopeContainer> subScopes;
	private Scope managedScope;
	private Map<Factory<?>, Object> factories; // speichert (factory,implementation) paare

	public ScopeContainerImpl(Scope scope, UUID scopeId) {

		this.managedScope = scope;
		subScopes = new HashMap<>();
		factories = new HashMap<>();
		this.scopeId = scopeId;

	}

	public ScopeContainerImpl(Scope scope) {
		this(scope, null);
	}

	public UUID getScopeId() {
		return scopeId;
	}

	@Override
	public Scope getManagedScope() {
		return managedScope;
	}

	public UUID beginSubScope() {

		// TODO do we need it?
		// Methode bereits in ManagedContainer
		// Subscopes werden aber anscheinend trotzdem im ScopeContainer gemanaged, also
		// wirds auch hier erstellt

		UUID newSubScopeId = UUID.randomUUID();

		ScopeContainer newSubScope = new ScopeContainerImpl(managedScope, newSubScopeId);

		subScopes.put(newSubScopeId, newSubScope);

		return newSubScopeId;

	}

	@Override
	public void endSubScope(UUID subScopeId) {
		Map<Factory<?>, Object> factories = subScopes.remove(subScopeId).getFactories();
		factories.forEach(this::preDestroy);
		factories.clear();
	}

	private <T> void preDestroy(Factory<T> factory, Object obj) {
		if (obj == null) {
			LOG.warn("Got object for preDestroy, but it was null");
		} else if (!factory.getInterfaceType().isInstance(obj)) {
			LOG.error("Got object for preDestroy, but it was of a different type : required {}, got {}",
					factory.getInterfaceType().getName(), obj.getClass().getName());
		} else {
			LOG.debug("Performing destruction of Object {}",obj);
			@SuppressWarnings("unchecked") //Check programmatically, type safety intact
			T work = (T) obj;
			factory.preDestroy(work);
		}
		if(obj instanceof AutoCloseable) {
			try {
				((AutoCloseable) obj).close();
			}catch (Exception e) {
				LOG.error("Error during closing AutoClosable {} during preDestroy : ",obj,e);
			}
		}
	}

	@Override
	public ScopeContainer getSubScope(UUID subScopeId) {
		if (subScopes.containsKey(subScopeId) && subScopes.get(subScopeId) != null) {
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
		factories.put(factory, impl);
	}

	/**
	 * Associates the implementation with the specified factory in the primary
	 * scope<br>
	 *
	 * @param impl
	 *            to be associated
	 * @param factory
	 *            to be associated with
	 * @param subScopeId
	 *            Id of the subScope to be associated with
	 */
	@Override
	public void setImplementationForFactory(Object impl, Factory<?> factory, UUID subScopeId) {
		subScopes.get(subScopeId).getFactories().put(factory, impl);
	}

	public Map<Factory<?>, Object> getFactories() {
		return factories;
	}

	@Override
	public void close() throws IOException {
		subScopes.forEach((k,v) -> {
			try {
				((Closeable) v).close();
			} catch (IOException e) {
				e.printStackTrace();
				LOG.error("Error while trying to close subScope {} from Primary Scope {}",v.toString(),getManagedScope().toString());
			}
		});

		factories.forEach((k,v) -> {
			if(v instanceof Closeable) {
				try {
					((Closeable) v).close();
				} catch (IOException e) {
					LOG.error("Error while trying to close factory {} in scope {}",v.toString(),getManagedScope().toString());
				}
			}
		});
	}
}
