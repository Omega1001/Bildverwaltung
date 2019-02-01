/**
 * 
 */
package bildverwaltung.dao.impl;

import javax.persistence.EntityManager;

import bildverwaltung.dao.AbstractFilterDao;
import bildverwaltung.dao.PictureDao;
import bildverwaltung.dao.entity.Picture;

/**
 * @author jannik
 *
 */
public class PictureDaoImpl extends AbstractFilterDao<Picture> implements PictureDao {

	public PictureDaoImpl(EntityManager entityManager) {
		super(Picture.class, entityManager);
	}

}
