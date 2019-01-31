package bildverwaltung.gui.fx.search;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import bildverwaltung.dao.entity.UUIDBase;
import bildverwaltung.dao.helper.DataFilter;
import bildverwaltung.dao.helper.FilterDiscriptor;

public class ForeignOwnedCategory<E extends UUIDBase, TE extends UUIDBase> implements SearchCategory<E> {

	private final SingularAttribute<E, TE> joinField;
	private final PluralAttribute<E, ?, TE> joinFieldPlural;
	private List<SearchEntry<TE, ?>> entries = new LinkedList<>();
	private String name;

	public ForeignOwnedCategory(String name, SingularAttribute<E, TE> joinField, List<SearchEntry<TE, ?>> entries) {
		super();
		this.name = name;
		this.joinField = joinField;
		this.joinFieldPlural = null;
		this.entries = entries;
	}

	public ForeignOwnedCategory(String name, PluralAttribute<E, ?, TE> joinField, List<SearchEntry<TE, ?>> entries) {
		super();
		this.name = name;
		this.joinField = null;
		this.joinFieldPlural = joinField;
		this.entries = entries;
	}

	@Override
	public void addToFilterList(DataFilter<E> filterList) {
		for (SearchEntry<TE, ?> entry : entries) {
			for (FilterDiscriptor<TE, ?> descriptor : entry.asDescriptor()) {
				if (joinField != null) {
					filterList.addForeignFilter(descriptor, joinField);
				}else if (joinFieldPlural != null) {
					filterList.addForeignFilter(descriptor, joinFieldPlural);
				}
			}
		}
	}

	@Override
	public void render(SearchRenderer renderer) {
		if (renderer == null) {
			throw new IllegalArgumentException("Renderer must be not null");
		}		
		renderer.beginCathegory(getName());
		for(SearchEntry<TE, ?> entry : entries) {
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
		for(SearchEntry<TE, ?> e : entries) {
			e.reset();
		}
	}

}
