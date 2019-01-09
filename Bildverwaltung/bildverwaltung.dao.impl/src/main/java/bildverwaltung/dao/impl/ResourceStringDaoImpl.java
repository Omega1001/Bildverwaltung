package bildverwaltung.dao.impl;

import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import bildverwaltung.dao.ResourceStringDao;
import bildverwaltung.dao.entity.ResourceString;
import bildverwaltung.dao.exception.DaoException;

public class ResourceStringDaoImpl implements ResourceStringDao {

	
	private final EntityManager em;
		
	public ResourceStringDaoImpl(EntityManager em) {
		super();
		this.em = em;
	}

	@Override
	public List<ResourceString> getResourceStringsForLocale(Locale locale) throws DaoException {
		TypedQuery<ResourceString> query = em.createNamedQuery("resourceString.forLocale", ResourceString.class);
		query.setParameter("languageKey", locale.getLanguage());
		query.setParameter("countryKey", locale.getCountry());
		return query.getResultList();
	}

}
