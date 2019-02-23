package bildverwaltung.dao.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ResourceStringKey implements Serializable{

	private String resourceStringId;
	private String languageKey;
	private String countryKey;

	public ResourceStringKey(String resourceStringId, String languageKey, String countryKey) {
		this.resourceStringId = resourceStringId;
		this.languageKey = languageKey;
		this.countryKey = countryKey;
	}

	public ResourceStringKey() {
		// TODO Auto-generated constructor stub
	}

	public String getResourceStringId() {
		return resourceStringId;
	}

	public void setResourceStringId(String resourceStringId) {
		this.resourceStringId = resourceStringId;
	}

	public String getLanguageKey() {
		return languageKey;
	}

	public void setLanguageKey(String languageKey) {
		this.languageKey = languageKey;
	}

	public String getCountryKey() {
		return countryKey;
	}

	public void setCountryKey(String countryKey) {
		this.countryKey = countryKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((countryKey == null) ? 0 : countryKey.hashCode());
		result = prime * result + ((languageKey == null) ? 0 : languageKey.hashCode());
		result = prime * result + ((resourceStringId == null) ? 0 : resourceStringId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourceStringKey other = (ResourceStringKey) obj;
		if (countryKey == null) {
			if (other.countryKey != null)
				return false;
		} else if (!countryKey.equals(other.countryKey))
			return false;
		if (languageKey == null) {
			if (other.languageKey != null)
				return false;
		} else if (!languageKey.equals(other.languageKey))
			return false;
		if (resourceStringId == null) {
			if (other.resourceStringId != null)
				return false;
		} else if (!resourceStringId.equals(other.resourceStringId))
			return false;
		return true;
	}
	
	

}
