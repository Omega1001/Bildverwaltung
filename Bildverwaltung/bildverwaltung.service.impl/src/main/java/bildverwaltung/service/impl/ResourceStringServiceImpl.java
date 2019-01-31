package bildverwaltung.service.impl;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bildverwaltung.dao.ResourceStringDao;
import bildverwaltung.dao.entity.ResourceString;
import bildverwaltung.dao.exception.ServiceException;
import bildverwaltung.service.ResourceStringService;

public class ResourceStringServiceImpl implements ResourceStringService {
	private static final Logger LOG = LoggerFactory.getLogger(ResourceStringServiceImpl.class);
	private ResourceStringDao rsDao;
	private Locale standardLocale;

	public ResourceStringServiceImpl(ResourceStringDao rsDao, Locale standardLocale) {
		super();
		this.rsDao = rsDao;
		this.standardLocale = standardLocale;
	}

	@Override
	public ResourceBundle fetchBundleForLocale(Locale locale) throws ServiceException {
		LOG.trace("Enter fetchBundleForLocale locale={}", locale);
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
		LOG.trace("Exit fetchBundleForLocale result={}", result);
		return result;
	}

	private static class DBResourceBundel extends ResourceBundle {

		private Map<String, String> translations;

		public DBResourceBundel(List<ResourceString> rsStrings, ResourceBundle parent) {
			setParent(parent);
			translations = new HashMap<>(rsStrings.size() + 1, 1);
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
