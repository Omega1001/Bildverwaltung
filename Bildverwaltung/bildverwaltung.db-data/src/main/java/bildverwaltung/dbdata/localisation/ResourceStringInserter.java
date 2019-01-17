package bildverwaltung.dbdata.localisation;

import java.io.IOException;
import java.io.InputStream;

import javax.persistence.EntityManager;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import bildverwaltung.dao.entity.ResourceString;

public class ResourceStringInserter extends DefaultHandler {
	private static final Logger LOG = LoggerFactory.getLogger(ResourceStringInserter.class);
	private EntityManager em;

	private ResourceString work;

	private String langKey = "";
	private String countryKey = "";

	public ResourceStringInserter(EntityManager em) {
		this.em = em;
	}

	public static void insertResourceStrings(EntityManager em, InputStream in)
			throws SAXException, IOException, ParserConfigurationException {
		SAXParserFactory.newInstance().newSAXParser().parse(in, new ResourceStringInserter(em));
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if ("Language".equals(qName)) {
			langKey = attributes.getValue("languageCode");
			countryKey = attributes.getValue("countryCode");
			em.getTransaction().begin();
			LOG.info("Inserting language package for {}_{}", langKey, countryKey);
		} else if ("ResourceString".equals(qName)) {
			work = new ResourceString();
			work.setResourceStringId(attributes.getValue("id"));
			work.setTranslation("");
			work.setLanguageKey("".equals(langKey) ? null:langKey.substring(0, 2));
			work.setCountryKey("".equals(countryKey) ? null:countryKey.substring(0, 2));
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String chars = new String(ch, start, length);
		if (work != null) {
			work.setTranslation((work.getTranslation() + chars).trim());
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if ("Language".equals(qName)) {
			em.getTransaction().commit();
			LOG.info("Finished inserting Language for {}_{}", langKey, countryKey);
			langKey = null;
			countryKey = null;
		} else if ("ResourceString".equals(qName) && work != null) {
			if (work.getCountryKey() == null) {
				work.setCountryKey("");
			}
			if (work.getLanguageKey() != null && !"".equals(work.getTranslation())) {
				LOG.debug("Inserting Resource String {}", work);
				em.persist(work);
			} else {
				LOG.warn("Found invalid resource String {}, not adding it", work);
			}
		}
	}

}
