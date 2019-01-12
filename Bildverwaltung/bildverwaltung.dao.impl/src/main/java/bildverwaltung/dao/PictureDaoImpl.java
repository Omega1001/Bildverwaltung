package bildverwaltung.dao;

import bildverwaltung.dao.entity.Picture;
import bildverwaltung.dao.exception.DaoException;
import bildverwaltung.dao.helper.FilterCreteria;
import bildverwaltung.dao.helper.SortCreteria;

import javax.persistence.EntityManager;
import java.util.List;

public class PictureDaoImpl extends AbstractDao<Picture> implements PictureDao {

    public PictureDaoImpl(Class<Picture> entityClass, EntityManager entityManager) {
        super(entityClass, entityManager);

    }

    @Override
    public List<Picture> getFiltered(List<FilterCreteria<?, ?, ?>> filters, List<SortCreteria> order) throws DaoException {

        return null;
    }
}
