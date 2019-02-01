package bildverwaltung.factory.dao;

import java.util.Locale;

import javax.persistence.EntityManager;

import bildverwaltung.container.Factory;
import bildverwaltung.container.ManagedContainer;
import bildverwaltung.container.Scope;
import bildverwaltung.dao.ResourceStringDao;
import bildverwaltung.factory.utils.transactions.TransactionProxy;
import bildverwaltung.service.ResourceStringService;
import bildverwaltung.service.impl.ResourceStringServiceImpl;

public class ResourceStringServiceFacory implements Factory<ResourceStringService> {

	@Override
	public Class<ResourceStringService> getInterfaceType() {
		return ResourceStringService.class;
	}

	@Override
	public ResourceStringService generate(ManagedContainer container, Scope scope) {
		EntityManager em = container.materialize(EntityManager.class);
		ResourceStringDao dao = container.materialize(ResourceStringDao.class);
		ResourceStringService res = new ResourceStringServiceImpl(dao,Locale.ENGLISH);
		return TransactionProxy.proxyFor(res, getInterfaceType(), em, false, false);
	}

}
