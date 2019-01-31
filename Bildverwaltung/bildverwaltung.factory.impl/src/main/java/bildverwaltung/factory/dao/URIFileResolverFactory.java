package bildverwaltung.factory.dao;

import bildverwaltung.container.Factory;
import bildverwaltung.container.ManagedContainer;
import bildverwaltung.container.Scope;
import bildverwaltung.dao.URIResolver;
import bildverwaltung.dao.impl.uriresolver.URIFileResolver;

public class URIFileResolverFactory implements Factory<URIResolver> {

	@Override
	public Class<URIResolver> getInterfaceType() {
		return URIResolver.class;
	}

	@Override
	public URIResolver generate(ManagedContainer container, Scope scope) {
		return new URIFileResolver();
	}

}
