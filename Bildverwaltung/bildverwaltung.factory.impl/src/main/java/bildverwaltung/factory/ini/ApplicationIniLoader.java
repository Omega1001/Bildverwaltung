package bildverwaltung.factory.ini;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import bildverwaltung.container.startup.ContainerStartupException;
import bildverwaltung.container.startup.StartUpPhase;
import bildverwaltung.container.startup.StartupTask;
import bildverwaltung.utils.ApplicationIni;
import bildverwaltung.utils.IniFileReader;

public class ApplicationIniLoader implements StartupTask {

	@Override
	public void execute() throws ContainerStartupException {
		File f = new File("app.ini");
		try {
			ApplicationIni.set(IniFileReader.readFromFile(new BufferedInputStream(new FileInputStream(f))));
		} catch (FileNotFoundException e) {
			throw new ContainerStartupException(e);
		}
	}
	
	@Override
	public StartUpPhase getExecutionPhase() {
		return StartUpPhase.PRE_CONFIGURE;
	}
	
	@Override
	public String getTaskName() {
		return "Application ini loader";
	}

}
