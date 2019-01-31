package bildverwaltung.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries(value = { @NamedQuery(name = "resourceString.forLocale", query = "SELECT RS from ResourceString as RS WHERE RS.languageKey = :languageKey AND RS.countryKey = :countryKey") })
public class ResourceString {

	private String resourceStringId;
	private String translation;
	private String languageKey;
	private String countryKey;

	public ResourceString() {
		super();
	}

	public ResourceString(String resourceStringId, String translation, String languageKey, String countryKey) {
		super();
		this.resourceStringId = resourceStringId;
		this.translation = translation;
		this.languageKey = languageKey;
		this.countryKey = countryKey;
	}

	@Id
	public String getResourceStringId() {
		return resourceStringId;
	}

	public void setResourceStringId(String resourceStringId) {
		this.resourceStringId = resourceStringId;
	}

	@Column(nullable = false)
	public String getTranslation() {
		return translation;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

	@Column(nullable = false, length = 2)
	public String getLanguageKey() {
		return languageKey;
	}

	public void setLanguageKey(String languageKey) {
		this.languageKey = languageKey;
	}

	@Column(nullable = false, length = 3)
	public String getCountryKey() {
		return countryKey;
	}

	public void setCountryKey(String countryKey) {
		this.countryKey = countryKey;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ResourceString [resourceStringId=").append(resourceStringId).append(", languageKey=")
				.append(languageKey).append(", countryKey=").append(countryKey).append(", translation=")
				.append(translation).append("]");
		return builder.toString();
	}
	
	

}
