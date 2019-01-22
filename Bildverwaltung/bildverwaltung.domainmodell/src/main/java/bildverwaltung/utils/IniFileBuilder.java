package bildverwaltung.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import bildverwaltung.utils.IniFile.IniSection;

public class IniFileBuilder {

	private Set<IniSection> sections = new HashSet<>();
	private String sectionName = null;
	private Map<String,String> entires = new HashMap<>();
	
	public void beginSection(String name) {
		if(entires.size() >0) {
			sections.add(new IniSection(sectionName, entires));
		}
		sectionName = name;
		entires = new HashMap<>();
	}
	
	public void addEntry(String key, String val) {
		entires.put(key, val);
	}
	
	public void endIni() {
		if(entires.size() >0) {
			sections.add(new IniSection(sectionName, entires));
		}
	}

	public IniFile generate() {
		if(entires.size() >0) {
			sections.add(new IniSection(sectionName, entires));
		}
		return new IniFile(sections);
	}
	
}
