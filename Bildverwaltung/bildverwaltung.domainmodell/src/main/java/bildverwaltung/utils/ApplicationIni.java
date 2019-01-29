package bildverwaltung.utils;

public class ApplicationIni {

	public static final String DB_SECTION_NAME = "db config";
	
	private ApplicationIni() {
	}
	
	private static IniFile ini;
	
	public static IniFile get() {
		if(ini == null) {
			throw new IllegalStateException("Ini file not yet loaded");
		}
		return ini;
	}
	
	public static void set(IniFile f) {
		if(ini != null ) {
			throw new IllegalStateException("Ini already loaded");
		}
		ini = f;
	}

}
