package bildverwaltung.dao.impl.uriresolver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;

import bildverwaltung.dao.URIResolver;

public class URIFileResolver implements URIResolver {

	private static final String SCHEMA_NAME = "file";
	
	@Override
	public boolean canHandle(URI uri) {
		return SCHEMA_NAME.equals(uri.getScheme());
	}

	@Override
	public InputStream handle(URI uri) throws Exception {
		return new BufferedInputStream(new FileInputStream(new File(uri)));
	}

}
