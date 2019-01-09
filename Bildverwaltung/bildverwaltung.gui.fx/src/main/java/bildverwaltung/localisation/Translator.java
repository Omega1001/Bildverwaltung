package bildverwaltung.localisation;

import java.util.Locale;

public interface Translator {

	public String translate(String resourceKey);

	public void switchLanguage(Locale locale);

}
