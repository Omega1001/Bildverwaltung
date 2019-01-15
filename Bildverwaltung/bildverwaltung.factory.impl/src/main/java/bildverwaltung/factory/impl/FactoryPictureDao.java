package bildverwaltung.factory.impl;

import bildverwaltung.container.Factory;
import bildverwaltung.container.ManagedContainer;
import bildverwaltung.container.Scope;
import bildverwaltung.container.ScopeContainer;
import bildverwaltung.dao.PictureDao;
import bildverwaltung.dao.PictureDaoImpl;
import bildverwaltung.dao.entity.Picture;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class FactoryPictureDao implements Factory<PictureDao> {
    @Override
    public PictureDao generate(ManagedContainer container, Scope scope, ScopeContainer scopeContainer) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Domain Modell");
        EntityManager em = emf.createEntityManager();

        PictureDao dao = new PictureDaoImpl(Picture.class, em);

        return dao;
    }
}
