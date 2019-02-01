package bildverwaltung.localisation;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bildverwaltung.container.Factory;
import bildverwaltung.container.ManagedContainer;
import bildverwaltung.container.Scope;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.facade.ResourceStringFacade;

public class TranslatorImpl implements Translator {
	public static final Factory<Translator> FACTORY = new Factory<Translator>() {

		@Override
		public Class<Translator> getInterfaceType() {
			return Translator.class;
		}

		@Override
		public Translator generate(ManagedContainer container, Scope scope) {
			ResourceStringFacade ssf = container.materialize(ResourceStringFacade.class, Scope.APPLICATION);
			return new TranslatorImpl(ssf);
		}
	};

	private static final Logger LOG = LoggerFactory.getLogger(TranslatorImpl.class);
	private ResourceStringFacade resourceStringFacade;
	private Locale inUse = Locale.getDefault();
	private Map<Locale, ResourceBundle> translations = new HashMap<>(3);

	public TranslatorImpl(ResourceStringFacade resourceStringFacade) {
		super();
		this.resourceStringFacade = resourceStringFacade;
		switchLanguage(inUse);
	}

	@Override
	public String translate(String resourceKey) {
		ResourceBundle bundle = translations.get(inUse);
		if (bundle == null) {
			try {
				bundle = resourceStringFacade.fetchBundleForLocale(inUse);
			} catch (FacadeException e) {
				LOG.error("Unable to fetch resourceBundle");
				bundle = new ErrorBundle();
			}
			translations.put(inUse, bundle);
		}
		return bundle.getString(resourceKey);

	}

	@Override
	public void switchLanguage(Locale locale) {
		ResourceBundle bundle;
		try {
			bundle = resourceStringFacade.fetchBundleForLocale(locale);
		} catch (FacadeException e) {
			LOG.error("Error loading translations for locale {}", locale);
			bundle = new ErrorBundle();
		}
		translations.put(locale, bundle);
		inUse = locale;
	}

	private static class ErrorBundle extends ResourceBundle {
		@Override
		protected Object handleGetObject(String key) {
			return "ERROR_" + key + "_ERROR";
		}

		@Override
		public Enumeration<String> getKeys() {
			return Collections.enumeration(Collections.emptyList());
		}
	}

}
