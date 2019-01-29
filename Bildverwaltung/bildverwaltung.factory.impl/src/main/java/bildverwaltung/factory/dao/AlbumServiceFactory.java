package bildverwaltung.factory.dao;

import javax.persistence.EntityManager;

import bildverwaltung.container.Factory;
import bildverwaltung.container.ManagedContainer;
import bildverwaltung.container.Scope;
import bildverwaltung.dao.AlbumDao;
import bildverwaltung.factory.utils.transactions.TransactionProxy;
import bildverwaltung.service.AlbumService;
import bildverwaltung.service.impl.AlbumServiceImpl;

public class AlbumServiceFactory implements Factory<AlbumService> {

	@Override
	public AlbumService generate(ManagedContainer container, Scope scope) {
		EntityManager em = container.materialize(EntityManager.class,scope);
		AlbumDao aDao = container.materialize(AlbumDao.class, scope);
		AlbumService obj = new AlbumServiceImpl(aDao);
		return TransactionProxy.proxyFor(obj, AlbumService.class, em);
	}

	@Override
	public Class<AlbumService> getInterfaceType() {
		return AlbumService.class;
	}

}
