package bildverwaltung.factory.dao;

import javax.persistence.EntityManager;

import bildverwaltung.container.Factory;
import bildverwaltung.container.ManagedContainer;
import bildverwaltung.container.Scope;
import bildverwaltung.facade.ResourceStringFacade;
import bildverwaltung.facade.impl.ResourceStringFacadeImpl;
import bildverwaltung.factory.utils.transactions.TransactionProxy;
import bildverwaltung.service.ResourceStringService;

public class ResourceStringFacadeFacory implements Factory<ResourceStringFacade> {

	@Override
	public Class<ResourceStringFacade> getInterfaceType() {
		return ResourceStringFacade.class;
	}

	@Override
	public ResourceStringFacade generate(ManagedContainer container, Scope scope) {
		EntityManager em = container.materialize(EntityManager.class);
		ResourceStringService service = container.materialize(ResourceStringService.class);
		ResourceStringFacade res = new ResourceStringFacadeImpl(service );
		return TransactionProxy.proxyFor(res, getInterfaceType(), em, true, false);
	}

}
