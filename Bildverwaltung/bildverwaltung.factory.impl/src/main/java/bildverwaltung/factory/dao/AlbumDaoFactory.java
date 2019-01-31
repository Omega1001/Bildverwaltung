package bildverwaltung.factory.dao;

import javax.persistence.EntityManager;

import bildverwaltung.container.Factory;
import bildverwaltung.container.ManagedContainer;
import bildverwaltung.container.Scope;
import bildverwaltung.dao.AlbumDao;
import bildverwaltung.dao.impl.AlbumDaoImpl;
import bildverwaltung.factory.utils.transactions.TransactionProxy;

public class AlbumDaoFactory implements Factory<AlbumDao> {

	@Override
	public AlbumDao generate(ManagedContainer container, Scope scope) {
		EntityManager em = container.materialize(EntityManager.class,scope);
		AlbumDao obj = new AlbumDaoImpl(em);
		return TransactionProxy.proxyFor(obj, AlbumDao.class, em,true,false);
	}

	@Override
	public Class<AlbumDao> getInterfaceType() {
		return AlbumDao.class;
	}

}
