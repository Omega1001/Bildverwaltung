package bildverwaltung.dao;

import java.util.List;

import bildverwaltung.dao.entity.Album;
import bildverwaltung.dao.exception.DaoException;
import bildverwaltung.utils.DBDataRefference;

public interface AlbumDao extends CRUDDao<Album> {

	List<DBDataRefference<String>> getAllAlbumNameReferences() throws DaoException;

}
