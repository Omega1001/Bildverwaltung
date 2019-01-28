package bildverwaltung.utils;

import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IniFileReader {

	private static final Logger LOG = LoggerFactory.getLogger(IniFileReader.class);
	
	private static final Pattern COMMENT = Pattern.compile("^ *#.*$");
	private static final Pattern ENTRY = Pattern.compile("^ *(.+?)=(.+) *#?.*$");
	private static final Pattern SECTION = Pattern.compile("^ *\\[(.+)\\] *#?.*$");
	
	public static IniFile readFromFile(InputStream ins) {
		Scanner sc = null;
		IniFileBuilder builder = new IniFileBuilder();
		try {
			sc = new Scanner(ins);
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				processLine(line,builder);
			}
		}finally {
			if(sc != null) {
				sc.close();
			}
		}
		return builder.generate();
	}

	private static void processLine(String line, IniFileBuilder builder) {
		Matcher m = null;
		m = ENTRY.matcher(line);
		if(m.matches()) {
			builder.addEntry(m.group(1), m.group(2));
			LOG.debug("Found entry with key '{}' and value '{}'",m.group(1),m.group(2));
		}else if((m = SECTION.matcher(line)).matches()) {
			LOG.debug("Found section beginning '{}'",m.group(1));
			builder.beginSection(m.group(1));
		}else if((m = COMMENT.matcher(line)).matches()) {
			LOG.debug("Found comment line, ignoring");
		}else {
			LOG.debug("Found unidentified line, ignoring");
		}
		
		
	}

}
