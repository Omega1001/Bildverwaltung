package bildverwaltung.gui.fx.util;

import bildverwaltung.localisation.Translator;

public class TranslatedValue<E> {

	private final Translator translator;
	private final String resourceString;
	private final E value;

	private String translation;

	public TranslatedValue(Translator translator, String resourceString, E value) {
		super();
		this.translator = translator;
		this.resourceString = resourceString;
		this.value = value;
	}

	public String getTranslation() {
		if (translation == null) {
			translation = translator.translate(resourceString);
		}
		return translation;
	}

	public E getValue() {
		return value;
	}

	public void reset() {
		translation = null;
	}

	@Override
	public String toString() {
		return getTranslation();
	}

}
