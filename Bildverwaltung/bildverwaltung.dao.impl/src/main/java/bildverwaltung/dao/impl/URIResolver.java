package bildverwaltung.dao.impl;

import java.io.InputStream;
import java.net.URI;

public interface URIResolver {

	public boolean canHandle(URI uri);
	
	public InputStream handle(URI uri);
	
}
