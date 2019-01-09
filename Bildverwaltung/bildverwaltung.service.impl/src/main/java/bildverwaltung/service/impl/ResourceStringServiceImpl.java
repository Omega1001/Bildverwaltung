package bildverwaltung.service.impl;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import bildverwaltung.dao.ResourceStringDao;
import bildverwaltung.dao.entity.ResourceString;
import bildverwaltung.dao.exception.ServiceException;
import bildverwaltung.service.ResourceStringService;

public class ResourceStringServiceImpl implements ResourceStringService {

	private ResourceStringDao rsDao;
	private Locale standardLocale;

	public ResourceStringServiceImpl(ResourceStringDao rsDao, Locale standardLocale) {
		super();
		this.rsDao = rsDao;
		this.standardLocale = standardLocale;
	}

	@Override
	public ResourceBundle fetchBundleForLocale(Locale locale) throws ServiceException {
		ResourceBundle result = null;
		if (standardLocale.equals(locale)) {
			result = new DBResourceBundel(rsDao.getResourceStringsForLocale(locale), new FallbackResourceBundel());
		} else if (!"".equals(locale.getLanguage()) && "".equals(locale.getCountry())) {
			result = new DBResourceBundel(rsDao.getResourceStringsForLocale(locale),
					fetchBundleForLocale(standardLocale));
		} else if (!"".equals(locale.getLanguage()) && !"".equals(locale.getCountry())) {
			result = new DBResourceBundel(rsDao.getResourceStringsForLocale(locale),
					fetchBundleForLocale(new Locale(locale.getLanguage(), "")));
		}
		return result;
	}

	private static class DBResourceBundel extends ResourceBundle {

		private Map<String, String> translations;

		public DBResourceBundel(List<ResourceString> rsStrings, ResourceBundle parent) {
			setParent(parent);
			translations = new HashMap<>(translations.size() + 1, 0);
			for (ResourceString rs : rsStrings) {
				translations.put(rs.getResourceStringId(), rs.getTranslation());
			}
		}

		@Override
		protected Object handleGetObject(String key) {
			return translations.get(key);
		}

		@Override
		public Enumeration<String> getKeys() {
			return Collections.enumeration(translations.keySet());
		}

	}

	private static class FallbackResourceBundel extends ResourceBundle {

		@Override
		protected Object handleGetObject(String key) {
			return "???" + key + "???";
		}

		@Override
		public Enumeration<String> getKeys() {
			return Collections.enumeration(Collections.emptyList());
		}

	}

}
