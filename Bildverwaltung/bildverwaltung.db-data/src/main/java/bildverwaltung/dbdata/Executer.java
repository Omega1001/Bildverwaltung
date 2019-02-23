package bildverwaltung.dbdata;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import bildverwaltung.dbdata.localisation.ResourceStringInserter;
import bildverwaltung.dbdata.testdata.TestDataInserter;

public class Executer {

	private static final Logger LOG = LoggerFactory.getLogger(Executer.class);

	// Argument names
	public static final String JDBC_URL = "jdbc.url";
	public static final String JDBC_USERNAME = "jdbc.user";
	public static final String JDBC_PASSWORD = "jdbc.password";
	public static final String JDBC_CLASS = "jdbc.class";
	public static final String RS_SOURCE_FILE = "rs.source";
	public static final String TEST_DATA_SOURCE_FILE = "testdata.source";

	// Parameter Names
	public static final String BUILD_RS = "buildRS";
	public static final String BUILD_TEST_DATA = "testData";

	public static void main(String[] args) {
		LOG.info("Got the following atributes : {} ", (Object) args);
		Map<String, String> arguments = new HashMap<>();
		List<String> parameters = new LinkedList<>();
		generateArgumentMap(args, arguments, parameters);
		try {
			// Connect to db and generate schema if needed
			EntityManagerFactory emFactory = generateDefaultEMFactory(arguments.get(JDBC_CLASS),
					arguments.get(JDBC_URL), arguments.get(JDBC_USERNAME), arguments.get(JDBC_PASSWORD));
			EntityManager em = emFactory.createEntityManager();
			rebuildShema(em);
			Executer ex = new Executer(emFactory != null ? em : null);
			// Process tasks
			if (parameters.contains(BUILD_RS)) {
				ex.buildResourceBundles(arguments.get(RS_SOURCE_FILE));
			}
			if (parameters.contains(BUILD_TEST_DATA)) {
				ex.buildTestData(arguments.get(TEST_DATA_SOURCE_FILE), arguments);
			}
		} catch (Exception e) {
			LOG.error("Error during invocation : ", e);
		}
	}

	private static void rebuildShema(EntityManager em) {
		em.getTransaction().begin();
		Query q = em.createNativeQuery(getScript("bildverwaltung/dbdata/ddl/dropDDL.sql"));
		q = em.createNativeQuery(getScript("bildverwaltung/dbdata/ddl/createDDL.sql"));
		q.executeUpdate();
		em.getTransaction().commit();
	}

	private static String getScript(String resource) {
		StringBuilder sb = new StringBuilder();
		Scanner sc = null;
		try {
			sc = new Scanner(Executer.class.getClassLoader().getResourceAsStream(resource));
			while (sc.hasNextLine()) {
				sb.append(sc.nextLine()).append("\r\n");
			}
		} finally {
			if (sc != null) {
				sc.close();
			}
		}
		return sb.toString();
	}

	private static Map<String, String> generateArgumentMap(String[] args, Map<String, String> arguments,
			List<String> parameters) {
		for (String arg : args) {
			if (arg != null) {
				int seperator = arg.indexOf('=');
				if (seperator > 0) {
					String key = arg.substring(0, seperator);
					String val = arg.substring(seperator + 1, arg.length());
					LOG.debug("Adding argument key = {},value = {}");
					arguments.put(key, val);
				} else {
					LOG.debug("Adding parameter {}", arg);
					parameters.add(arg);
				}
			}
		}
		return arguments;
	}

	public static EntityManagerFactory generateDefaultEMFactory(String jdbcClassName, String jdbcUrl, String username,
			String password) {
		if (jdbcUrl == null || "".equals(jdbcUrl)) {
			LOG.info("No JDBC url specified, not opening database connection");
			return null;
		}
		Map<String, String> properties = new HashMap<>();
		properties.put("javax.persistence.jdbc.user", username);
		properties.put("javax.persistence.jdbc.password", password);
		properties.put("javax.persistence.jdbc.url", jdbcUrl);
		properties.put("javax.persistence.jdbc.driver", jdbcClassName);
		properties.put("eclipselink.ddl-generation", "none");
		// Persistence.generateSchema("Domain Modell", properties);
		return Persistence.createEntityManagerFactory("Domain Modell", properties);
	}

	private final EntityManager em;

	public Executer(EntityManager em) {
		this.em = em;
	}

	public void buildResourceBundles(String source) throws IOException {
		if (em == null) {
			throw new IllegalArgumentException("Missing open database connection");
		}
		if (source == null) {
			LOG.error("Unable to locate resource : {}", source);
			throw new IllegalArgumentException("Source file must be specified");
		}
		File src = new File(source);

		if (!src.exists() || src.isDirectory()) {
			LOG.error("Invalid resource existing:{}, directory:{}: {}", src.exists(), src.isDirectory(), src);
			throw new IllegalArgumentException("Invalid source file");
		}
		try {
			ResourceStringInserter.insertResourceStrings(em, new BufferedInputStream(new FileInputStream(src)));
		} catch (SAXException | IOException | ParserConfigurationException e) {
			LOG.error("Error during building resource Strings : ", e);
		}
	}

	public void buildTestData(String source, Map<String, String> vars) {
		if (em == null) {
			throw new IllegalArgumentException("Missing open database connection");
		}
		if (source == null) {
			LOG.error("Unable to locate resource : {}", source);
			throw new IllegalArgumentException("Source file must be specified");
		}
		File src = new File(source);

		if (!src.exists() || src.isDirectory()) {
			LOG.error("Invalid resource existing:{}, directory:{}: {}", src.exists(), src.isDirectory(), src);
			throw new IllegalArgumentException("Invalid source file");
		}
		try {
			TestDataInserter.insertResourceStrings(em, new BufferedInputStream(new FileInputStream(src)), vars);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			LOG.error("Error during building resource Strings : ", e);
		}
	}

}
