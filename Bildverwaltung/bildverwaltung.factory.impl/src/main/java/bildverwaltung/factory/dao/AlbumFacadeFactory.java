package bildverwaltung.factory.dao;

import javax.persistence.EntityManager;

import bildverwaltung.container.Factory;
import bildverwaltung.container.ManagedContainer;
import bildverwaltung.container.Scope;
import bildverwaltung.facade.AlbumFacade;
import bildverwaltung.facade.impl.AlbumFacadeImpl;
import bildverwaltung.factory.utils.transactions.TransactionProxy;
import bildverwaltung.service.AlbumService;

public class AlbumFacadeFactory implements Factory<AlbumFacade> {

	@Override
	public AlbumFacade generate(ManagedContainer container, Scope scope) {
		EntityManager em = container.materialize(EntityManager.class,scope);
		AlbumService aService = container.materialize(AlbumService.class, scope);
		AlbumFacade obj = new AlbumFacadeImpl(aService);
		return TransactionProxy.proxyFor(obj, AlbumFacade.class, em,false,true);
	}

	@Override
	public Class<AlbumFacade> getInterfaceType() {
		return AlbumFacade.class;
	}

}
