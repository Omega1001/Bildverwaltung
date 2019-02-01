package bildverwaltung.factory.dao;

import javax.persistence.EntityManager;

import bildverwaltung.container.Factory;
import bildverwaltung.container.ManagedContainer;
import bildverwaltung.container.Scope;
import bildverwaltung.dao.PictureDao;
import bildverwaltung.dao.impl.PictureDaoImpl;
import bildverwaltung.factory.utils.transactions.TransactionProxy;

public class PictureDaoFactory implements Factory<PictureDao> {

	@Override
	public PictureDao generate(ManagedContainer container, Scope scope) {
		EntityManager em = container.materialize(EntityManager.class,scope);
		PictureDao obj = new PictureDaoImpl(em);
		return TransactionProxy.proxyFor(obj, PictureDao.class, em,true,false);
	}

	@Override
	public Class<PictureDao> getInterfaceType() {
		return PictureDao.class;
	}

}
