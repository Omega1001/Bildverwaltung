package bildverwaltung.container.shutdown;

public enum ShutdownPhase{
	PRE_CONFIGURE,
	CONFIGURE,
	RESOURCE_LOGOUT,
	POST_CONFIGURE,
	PREPAIR_APP_SHUTDOWN,
	POST_PREPAIR_APP_SHUTDOWN,
	CLEAN_UP;
	
	
}