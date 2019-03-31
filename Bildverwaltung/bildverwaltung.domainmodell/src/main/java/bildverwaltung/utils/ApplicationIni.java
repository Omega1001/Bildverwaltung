package bildverwaltung.utils;

public class ApplicationIni {

	public static final String DB_SECTION_NAME = "db config";

	public static final String DIR_SECTION_NAME = "directory";
	public static final String DIR_SECTION_PICTURESDIRECTORY_KEY = "picturesDirectory";
	
	public static final String CACHE_SIZE_NAME = "cache";
	public static final String CACHE_SIZE_KEY = "maxSize_MB";
	
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
