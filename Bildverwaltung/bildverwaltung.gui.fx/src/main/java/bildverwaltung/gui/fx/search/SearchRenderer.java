package bildverwaltung.gui.fx.search;

import bildverwaltung.dao.entity.UUIDBase;
import bildverwaltung.dao.helper.ComparisonMode;
import bildverwaltung.gui.fx.util.ValueProxy;
import bildverwaltung.localisation.Translator;
import javafx.scene.Node;
import javafx.scene.Parent;

public interface SearchRenderer {

	//public <E extends UUIDBase> Parent renderSearch(List<SearchCategory<E>> cathegories);

	//public void renderCategory(String name, List<SearchEntry<?, ?>> entries);
	
	public void beginSearch();
	
	public void endSearch();
	
	public <E extends UUIDBase> Parent fetchResult();
	
	public void beginCathegory(String name);
	
	public void endCathegory();
	
	public void beginSearchEntry();

	public void putSearchFieldLabel(String name);

	public ValueProxy<ComparisonMode> putCompairMode(ComparisonMode[] values);

	public void putInputField(Node value);

	public void newLine();

	public void endEntry();
	
	public Translator getTranslator();
	
	public void setTranslator(Translator translator);
	
	
}
