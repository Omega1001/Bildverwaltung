package bildverwaltung.dbdata.testdata;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import bildverwaltung.dao.entity.Album;
import bildverwaltung.dao.entity.Picture;

public class TestDataInserter extends DefaultHandler {

	private Map<String, String> vars;
	private Pattern varPattern = Pattern.compile("\\$\\{(.+?)\\}");
	private EntityManager em;
	private Picture p = null;
	private Album a = null;

	private String lastReadValue = "";
	private List<UUID> pids = null;

	public static void insertResourceStrings(EntityManager em, InputStream in, Map<String, String> vars)
			throws SAXException, IOException, ParserConfigurationException {
		SAXParserFactory.newInstance().newSAXParser().parse(in, new TestDataInserter(em, vars));
	}

	public TestDataInserter(EntityManager em, Map<String, String> vars) {
		this.em = em;
		this.vars = vars != null ? vars : new HashMap<>();
	}

	@Override
	public void startDocument() throws SAXException {
		em.getTransaction().begin();
		em.createQuery("DELETE FROM Picture").executeUpdate();
		em.createQuery("DELETE FROM Album").executeUpdate();
		em.getTransaction().commit();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if ("picture".equals(qName)) {
			p = new Picture();
			p.setId(UUID.fromString(attributes.getValue("id")));
		} else if ("album".equals(qName)) {
			a = new Album();
			a.setId(UUID.fromString(attributes.getValue("id")));
		} else if (a != null && "pictures".equals(qName)) {
			pids = new LinkedList<>();
		}
		lastReadValue = "";
	}

	private void processAlbumElement(String uri, String localName, String qName) {
		if ("name".equals(qName)) {
			a.setName(processVars(lastReadValue));
		} else if ("comment".equals(qName)) {
			a.setComment(processVars(lastReadValue));
		} else if ("pictures".equals(qName)) {
			a.getPictures().addAll(picturesWithIds(pids));
		} else if (pids != null && "id".equals(qName)) {
			pids.add(UUID.fromString(processVars(lastReadValue)));
		}
	}

	private String processVars(String str) {
		String res = str;
		Matcher m = varPattern.matcher(res);
		while (m.find()) {
			res = res.replace(m.group(), vars.getOrDefault(m.group(1), ""));
			m = varPattern.matcher(res);
		}
		return res;
	}

	private List<Picture> picturesWithIds(List<UUID> pids2) {
		TypedQuery<Picture> query = em.createQuery("SELECT p FROM Picture p WHERE p.id IN :ids", Picture.class);
		query.setParameter("ids", pids2);
		return query.getResultList();
	}

	private void processPictureElement(String uri, String localName, String qName) throws URISyntaxException {
		if ("name".equals(qName)) {
			p.setName(processVars(lastReadValue));
		} else if ("uri".equals(qName)) {
			String sUri = processVars(lastReadValue).replace('\\', '/').replace(" ", "%20");
			p.setUri(new URI(sUri));
		} else if ("extension".equals(qName)) {
			p.setExtension(processVars(lastReadValue));
		} else if ("comment".equals(qName)) {
			p.setComment(processVars(lastReadValue));
		} else if ("width".equals(qName)) {
			p.setWidth(Integer.valueOf(processVars(lastReadValue)));
		} else if ("height".equals(qName)) {
			p.setHeigth(Integer.valueOf(processVars(lastReadValue)));
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		lastReadValue += String.copyValueOf(ch, start, length).trim();
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if ("picture".equals(qName)) {
			if (p != null) {
				em.getTransaction().begin();
				em.merge(p);
				em.getTransaction().commit();
				p = null;
			}
		} else if ("album".equals(qName)) {
			if (a != null) {
				em.getTransaction().begin();
				em.merge(a);
				em.getTransaction().commit();
				a = null;
			}
		} else if (p != null) {
			try {
				processPictureElement(uri, localName, qName);
			} catch (URISyntaxException e) {
				throw new SAXException(e);
			}
		} else if (a != null) {
			processAlbumElement(uri, localName, qName);
		}
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}

}
