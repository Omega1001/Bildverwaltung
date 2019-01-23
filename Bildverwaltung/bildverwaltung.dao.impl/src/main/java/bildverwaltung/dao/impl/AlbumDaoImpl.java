package bildverwaltung.dao.impl;

import javax.persistence.EntityManager;

import bildverwaltung.dao.AbstractDao;
import bildverwaltung.dao.AlbumDao;
import bildverwaltung.dao.entity.Album;

public class AlbumDaoImpl extends AbstractDao<Album> implements AlbumDao {

	public AlbumDaoImpl(EntityManager entityManager) {
		super(Album.class, entityManager);
	}

}
