package bildverwaltung.factory.dao;

import javax.persistence.EntityManager;

import bildverwaltung.container.Factory;
import bildverwaltung.container.ManagedContainer;
import bildverwaltung.container.Scope;
import bildverwaltung.dao.PictureDao;
import bildverwaltung.dao.URIResolutionDao;
import bildverwaltung.factory.utils.transactions.TransactionProxy;
import bildverwaltung.service.PictureService;
import bildverwaltung.service.impl.PictureServiceImpl;

public class PictureServiceFactory implements Factory<PictureService> {

	@Override
	public PictureService generate(ManagedContainer container, Scope scope) {
		EntityManager em = container.materialize(EntityManager.class,scope);
		PictureDao pDao = container.materialize(PictureDao.class, scope);
		URIResolutionDao uriDao = container.materialize(URIResolutionDao.class,scope);
		PictureService obj = new PictureServiceImpl(pDao,uriDao);
		return TransactionProxy.proxyFor(obj, PictureService.class, em);
	}

	@Override
	public Class<PictureService> getInterfaceType() {
		return PictureService.class;
	}

}
