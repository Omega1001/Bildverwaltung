package bildverwaltung.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class IniFile {

	
	private Map<String,IniSection> sections = new HashMap<>();
	
	public IniFile(Set<IniSection> sections) {
		for(IniSection s : sections) {
			this.sections.put(s.sectionName,s);
		}
	}
	public String get(String key) {
		return get(null,key);
	}
	public String getOrDefault(String key, String defaultValue) {
		return getOrDefault(null,key,defaultValue);
	}
	
	public String get(String section, String key) {
		return getOrDefault(section,key,null);
	}
	
	public String getOrDefault(String section, String key, String defaultValue) {
		IniSection s = sections.get(section);
		return s != null ? s.getEntries().getOrDefault(key,defaultValue) : defaultValue;
	}
	
	public Map<String,String> getSectionAsMap(String section){
		IniSection s = sections.get(section);
		return s != null ? s.getEntries() : Collections.emptyMap(); 
	}
	
	public static class IniSection{
		private final String sectionName;
		private final Map<String, String> entries;
		
		public IniSection(String sectionName,Map<String, String> entries) {
			this.sectionName = sectionName;
			this.entries = Collections.unmodifiableMap(entries);
		}
		public String getSectionName() {
			return sectionName;
		}
		public Map<String, String> getEntries() {
			return entries;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((sectionName == null) ? 0 : sectionName.hashCode());
			return result;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			IniSection other = (IniSection) obj;
			if (sectionName == null) {
				if (other.sectionName != null)
					return false;
			} else if (!sectionName.equals(other.sectionName))
				return false;
			return true;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (Entry<String, String> entry : entries.entrySet()) {
				sb.append(entry.getKey()).append('=').append(entry.getValue()).append("\r\n");
			}
			return sb.toString();
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(sections.containsKey(null)) {
			sb.append(sections.get(null)).append("\r\n");
		}
		for(String s : sections.keySet()) {
			if(s != null) {
				sb.append("[").append(s).append("]\r\n");
				sb.append(sections.get(s)).append("\r\n");
			}
		}
		return sb.toString();
	}

}
