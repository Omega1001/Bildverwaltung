package bildverwaltung.factory.dao;

import java.util.List;

import bildverwaltung.container.Factory;
import bildverwaltung.container.ManagedContainer;
import bildverwaltung.container.Scope;
import bildverwaltung.dao.URIResolutionDao;
import bildverwaltung.dao.URIResolver;
import bildverwaltung.dao.impl.URIResolutionDaoImpl;

public class URIResolutionDaoFactory implements Factory<URIResolutionDao> {

	@Override
	public URIResolutionDao generate(ManagedContainer container, Scope scope) {
		List<URIResolver> resolvers = container.materializeAll(URIResolver.class, scope);
		URIResolutionDao obj = new URIResolutionDaoImpl(resolvers);
		return obj;
	}

	@Override
	public Class<URIResolutionDao> getInterfaceType() {
		return URIResolutionDao.class;
	}

}
