package bildverwaltung.factory.dao;

import javax.persistence.EntityManager;

import bildverwaltung.container.Factory;
import bildverwaltung.container.ManagedContainer;
import bildverwaltung.container.Scope;
import bildverwaltung.facade.PictureFacade;
import bildverwaltung.facade.impl.PictureFacadeImpl;
import bildverwaltung.factory.utils.transactions.TransactionProxy;
import bildverwaltung.service.PictureService;

public class PictureFacadeFactory implements Factory<PictureFacade> {

	@Override
	public PictureFacade generate(ManagedContainer container, Scope scope) {
		EntityManager em = container.materialize(EntityManager.class,scope);
		PictureService pService = container.materialize(PictureService.class, scope);
		PictureFacade obj = new PictureFacadeImpl(pService);
		return TransactionProxy.proxyFor(obj, PictureFacade.class, em,false,true);
	}

	@Override
	public Class<PictureFacade> getInterfaceType() {
		return PictureFacade.class;
	}

}
