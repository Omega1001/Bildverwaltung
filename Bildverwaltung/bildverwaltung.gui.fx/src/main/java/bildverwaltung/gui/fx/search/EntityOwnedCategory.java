package bildverwaltung.gui.fx.search;

import java.util.LinkedList;
import java.util.List;

import bildverwaltung.dao.entity.UUIDBase;
import bildverwaltung.dao.helper.DataFilter;
import bildverwaltung.dao.helper.FilterDiscriptor;

public class EntityOwnedCategory<E extends UUIDBase> implements SearchCategory<E> {

	private List<SearchEntry<E, ?>> entries = new LinkedList<>();
	private String name;
	
	public EntityOwnedCategory(List<SearchEntry<E, ?>> entries, String name) {
		super();
		this.entries = entries;
		this.name = name;
	}

	@Override
	public void addToFilterList(DataFilter<E> filterList) {
		for (SearchEntry<E, ?> entry : entries) {
			for (FilterDiscriptor<E, ?> descriptor : entry.asDescriptor()) {
				filterList.addOwnedFilter(descriptor);
			}
		}
	}

	@Override
	public void render(SearchRenderer renderer) {
		if(renderer == null) {
			throw new IllegalArgumentException("Renderer must be not null");
		}
		renderer.beginCathegory(getName());
		for(SearchEntry<E, ?> entry : entries) {
			entry.render(renderer);
		}
		renderer.endCathegory();
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void reset() {
		for(SearchEntry<E, ?> e : entries) {
			e.reset();
		}
	}
	
	

}
