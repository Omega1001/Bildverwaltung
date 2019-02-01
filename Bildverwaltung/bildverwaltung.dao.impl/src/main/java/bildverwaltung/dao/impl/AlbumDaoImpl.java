package bildverwaltung.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import bildverwaltung.dao.AbstractDao;
import bildverwaltung.dao.AlbumDao;
import bildverwaltung.dao.entity.Album;
import bildverwaltung.dao.exception.DaoException;
import bildverwaltung.dao.utils.DbDataReferenceBuilder;
import bildverwaltung.utils.DBDataRefference;

public class AlbumDaoImpl extends AbstractDao<Album> implements AlbumDao {

	public AlbumDaoImpl(EntityManager entityManager) {
		super(Album.class, entityManager);
	}

	@Override
	public List<DBDataRefference<String>> getAllAlbumNameReferences() throws DaoException {
		@SuppressWarnings("unchecked")
		List<Object[]> res = em().createNamedQuery("album.all_id_name").getResultList();
		return DbDataReferenceBuilder.buildRef(res, String.class);
	}

}
