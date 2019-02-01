package bildverwaltung.factory.ini;

import javax.persistence.Persistence;

import bildverwaltung.container.startup.ContainerStartupException;
import bildverwaltung.container.startup.StartUpPhase;
import bildverwaltung.container.startup.StartupTask;
import bildverwaltung.factory.dao.JPAConnectionFactory;
import bildverwaltung.utils.ApplicationIni;

public class JPADBConnector implements StartupTask {

	@Override
	public void execute() throws ContainerStartupException {
		JPAConnectionFactory.set(Persistence.createEntityManagerFactory("Domain Modell",
				ApplicationIni.get().getSectionAsMap(ApplicationIni.DB_SECTION_NAME)));
	}

	@Override
	public StartUpPhase getExecutionPhase() {
		return StartUpPhase.RESOURCE_LOGIN;
	}
	
	@Override
	public String getTaskName() {
		return "JPA connect to db";
	}
	
}
