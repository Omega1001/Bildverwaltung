package bildverwaltung.facade.impl;

import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bildverwaltung.dao.exception.ExceptionType;
import bildverwaltung.dao.exception.FacadeException;
import bildverwaltung.facade.ResourceStringFacade;
import bildverwaltung.service.ResourceStringService;

public class ResourceStringFacadeImpl implements ResourceStringFacade {

	private static final Logger LOG = LoggerFactory.getLogger(ResourceStringFacadeImpl.class);
	private ResourceStringService rsService;

	public ResourceStringFacadeImpl(ResourceStringService rsService) {
		super();
		this.rsService = rsService;
	}

	@Override
	public ResourceBundle fetchBundleForLocale(Locale locale) throws FacadeException {
		LOG.trace("Enter fetchBundleForLocale locale={}", locale);
		try {
			ResourceBundle res = rsService.fetchBundleForLocale(locale);
			LOG.trace("Exit fetchBundleForLocale res={}", res);
			return res;
		}catch(FacadeException e) {
			//keep going ...
			throw e;
		}catch (Exception e) {
			LOG.error("Cought unexpected Exception during fetching resource bundle : ",e);
			throw new FacadeException(ExceptionType.UNKNOWN, e);
		}
	}

}
