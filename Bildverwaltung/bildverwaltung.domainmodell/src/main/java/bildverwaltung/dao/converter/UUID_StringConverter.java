package bildverwaltung.dao.converter;

import java.util.UUID;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class UUID_StringConverter implements AttributeConverter<UUID,String> {

	@Override
	public String convertToDatabaseColumn(UUID arg0) {
		return arg0.toString();
	}

	@Override
	public UUID convertToEntityAttribute(String arg0) {
		return UUID.fromString(arg0);
	}

}
