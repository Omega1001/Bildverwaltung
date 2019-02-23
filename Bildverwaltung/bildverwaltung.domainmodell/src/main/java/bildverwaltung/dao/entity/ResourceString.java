package bildverwaltung.dao.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

@Entity
@NamedQueries(value = {
		@NamedQuery(name = "resourceString.forLocale", query = "SELECT RS from ResourceString as RS WHERE RS.id.languageKey = :languageKey AND RS.id.countryKey = :countryKey") })
public class ResourceString {
	@EmbeddedId
	private ResourceStringKey id;
	@Column(nullable = false)
	private String translation;

	public ResourceString() {
		super();
		id = new ResourceStringKey();
	}

	public ResourceString(String resourceStringId, String translation, String languageKey, String countryKey) {
		super();
		this.id = new ResourceStringKey(resourceStringId, languageKey, countryKey);
		this.translation = translation;
	}

	public ResourceStringKey getId() {
		return id;
	}

	public void setId(ResourceStringKey id) {
		this.id = id;
	}

	@Transient
	public String getResourceStringId() {
		return id.getResourceStringId();
	}

	public void setResourceStringId(String resourceStringId) {
		id.setResourceStringId(resourceStringId);
	}

	@Transient
	public String getLanguageKey() {
		return id.getLanguageKey();
	}

	public void setLanguageKey(String languageKey) {
		id.setLanguageKey(languageKey);
	}

	@Transient
	public String getCountryKey() {
		return id.getCountryKey();
	}

	public void setCountryKey(String countryKey) {
		id.setCountryKey(countryKey);
	}

	public String getTranslation() {
		return translation;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ResourceString [resourceStringId=").append(id.getResourceStringId()).append(", languageKey=")
				.append(id.getLanguageKey()).append(", countryKey=").append(id.getCountryKey()).append(", translation=")
				.append(translation).append("]");
		return builder.toString();
	}

}
