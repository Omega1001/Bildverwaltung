package bildverwaltung.dao;

import java.io.InputStream;
import java.net.URI;

public interface URIResolver {

	public boolean canHandle(URI uri);
	
	public InputStream handle(URI uri) throws Exception;
	
}
