package bildverwaltung.gui.fx.search;

import bildverwaltung.dao.entity.UUIDBase;
import bildverwaltung.dao.helper.DataFilter;

public interface SearchCategory<E extends UUIDBase> {
	
	public String getName();
	
	public void addToFilterList(DataFilter<E> filterList);
	
	public void render(SearchRenderer renderer);

	public void reset();
	
}
