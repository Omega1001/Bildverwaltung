package bildverwaltung.container.startup;

public enum StartUpPhase{
	PRE_CONFIGURE,
	CONFIGURE,
	DB_CONNECT,
	POST_CONFIGURE,
	PREPAIR_APP,
	POST_PREPAIR_APP,
	CLEAN_UP;
	
	
}