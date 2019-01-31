package bildverwaltung.factory.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bildverwaltung.container.Factory;
import bildverwaltung.container.ManagedContainer;
import bildverwaltung.container.Scope;

public class JPAConnectionFactory implements Factory<EntityManager>{
	private static final Logger LOG = LoggerFactory.getLogger(JPAConnectionFactory.class);
	private static EntityManagerFactory FACTORY;
	
	public static void set(EntityManagerFactory factory) {
		if(FACTORY != null && FACTORY.isOpen()) {
			LOG.warn("Tried to set Factory, but already had an open Factory, doing noting");
		}else {
			FACTORY = factory;
		}
	}

	@Override
	public void close() {
		FACTORY.close();
	}
	
	@Override
	public EntityManager generate(ManagedContainer container, Scope scope) {
		if(FACTORY == null) {
			LOG.error("Can not create em, because no factory has been set");
			throw new IllegalStateException("Missing Factory");
		}
		if(!FACTORY.isOpen()) {
			LOG.error("Can not create em, because factory is closed");
			throw new IllegalStateException("Factory not opened");
		}
		return FACTORY.createEntityManager();
	}

	@Override
	public Class<EntityManager> getInterfaceType() {
		return EntityManager.class;
	}
	
}
