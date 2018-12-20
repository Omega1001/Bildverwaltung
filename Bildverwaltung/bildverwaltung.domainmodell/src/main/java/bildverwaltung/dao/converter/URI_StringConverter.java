package bildverwaltung.dao.converter;

import java.net.URI;
import java.net.URISyntaxException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class URI_StringConverter implements AttributeConverter<URI,String> {

	@Override
	public String convertToDatabaseColumn(URI arg0) {
		return arg0.toASCIIString();
	}

	@Override
	public URI convertToEntityAttribute(String arg0) {
		try {
			return new URI(arg0);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(arg0);
		}
	}


}
