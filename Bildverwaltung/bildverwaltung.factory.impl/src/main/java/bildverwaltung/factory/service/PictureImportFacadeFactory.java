package bildverwaltung.factory.service;

import bildverwaltung.container.Factory;
import bildverwaltung.container.ManagedContainer;
import bildverwaltung.container.Scope;
import bildverwaltung.facade.PictureImportFacade;
import bildverwaltung.facade.impl.PictureImportFacadeImpl;
import bildverwaltung.factory.utils.transactions.TransactionProxy;
import bildverwaltung.service.pictureimport.PictureImportService;

import javax.persistence.EntityManager;

public class PictureImportFacadeFactory implements Factory<PictureImportFacade> {

    @Override
    public Class<PictureImportFacade> getInterfaceType() {
        return PictureImportFacade.class;
    }

    @Override
    public PictureImportFacade generate(ManagedContainer container, Scope scope) {
        EntityManager em = container.materialize(EntityManager.class, scope);
        PictureImportService service = container.materialize(PictureImportService.class, scope);
        PictureImportFacade facade = new PictureImportFacadeImpl(service);

        return TransactionProxy.proxyFor(facade, PictureImportFacade.class, em,true,true);

    }
}
