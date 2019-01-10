package bildverwaltung.dao;

import bildverwaltung.dao.entity.Picture;

import javax.persistence.EntityManager;

public class PictureDao extends AbstractDao<Picture> {

    public PictureDao(Class<Picture> entityClass, EntityManager entityManager) {
        super(entityClass, entityManager);

    }
}
