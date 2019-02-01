package bildverwaltung.factory.dao;

import javax.persistence.EntityManager;

import bildverwaltung.container.Factory;
import bildverwaltung.container.ManagedContainer;
import bildverwaltung.container.Scope;
import bildverwaltung.dao.ResourceStringDao;
import bildverwaltung.dao.impl.ResourceStringDaoImpl;
import bildverwaltung.factory.utils.transactions.TransactionProxy;

public class ResourceStringDaoFacory implements Factory<ResourceStringDao> {

	@Override
	public Class<ResourceStringDao> getInterfaceType() {
		return ResourceStringDao.class;
	}

	@Override
	public ResourceStringDao generate(ManagedContainer container, Scope scope) {
		EntityManager em = container.materialize(EntityManager.class);
		ResourceStringDao res = new ResourceStringDaoImpl(em);
		return TransactionProxy.proxyFor(res, getInterfaceType(), em, true, false);
	}

}
