package bildverwaltung.factory.service;

import bildverwaltung.container.Factory;
import bildverwaltung.container.ManagedContainer;
import bildverwaltung.container.Scope;
import bildverwaltung.dao.PictureDao;
import bildverwaltung.facade.PictureFacade;
import bildverwaltung.factory.utils.transactions.TransactionProxy;
import bildverwaltung.service.pictureimport.PictureImportService;
import bildverwaltung.service.pictureimport.impl.PictureImportServiceImpl;

import javax.persistence.EntityManager;

public class PictureImportServiceFactory implements Factory<PictureImportService> {
    @Override
    public Class<PictureImportService> getInterfaceType() {
        return PictureImportService.class;
    }

    @Override
    public PictureImportService generate(ManagedContainer container, Scope scope) {

        EntityManager em = container.materialize(EntityManager.class, scope);

        PictureDao facade = container.materialize(PictureDao.class, scope);
        PictureImportService importService = new PictureImportServiceImpl(facade);

        /*
        TODO ask what the two booleans actually do and how they should be set here
         */
        return TransactionProxy.proxyFor(importService, getInterfaceType(),em,  true, false);
    }
}
