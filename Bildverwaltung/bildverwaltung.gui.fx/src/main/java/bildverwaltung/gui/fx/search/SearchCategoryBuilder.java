package bildverwaltung.gui.fx.search;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import bildverwaltung.dao.entity.UUIDBase;
import bildverwaltung.dao.helper.ComparisonMode;
import bildverwaltung.gui.fx.search.entries.IntegerSearchEntry;
import bildverwaltung.gui.fx.search.entries.StringSearchEntry;

public class SearchCategoryBuilder<E extends UUIDBase> {

	private static final Map<Class<?>, SearchFieldGenerator<?>> GENERATORS = new HashMap<>();
	
	static {
		GENERATORS.put(String.class, new StringSearchEntry.Generator());
		GENERATORS.put(Integer.class, new IntegerSearchEntry.Generator());
		GENERATORS.put(int.class, new IntegerSearchEntry.Generator());
	}

	private List<SearchEntry<E, ?>> entries = new LinkedList<>();

	public SearchCategoryBuilder() {
	}
	
	public <V> SearchCategoryBuilder<E> addEntry(String name, SingularAttribute<E, V> field) {
		return addEntry(name, field,null,null);
	}

	public <V> SearchCategoryBuilder<E> addEntry(String name, SingularAttribute<E, V> field, V defaultValue, ComparisonMode defaultMode) {
		@SuppressWarnings("unchecked")
		SearchFieldGenerator<V> gen = (SearchFieldGenerator<V>) GENERATORS.get(field.getJavaType());
		if (gen != null) {
			entries.add(gen.generate(name, field,defaultValue,defaultMode));
		}
		return this;
	}

	public EntityOwnedCategory<E> asEntityOwned(String name) {
		return new EntityOwnedCategory<>(entries, name);
	}

	public <SE extends UUIDBase> ForeignOwnedCategory<SE, E> asForeignOwned(String name,
			SingularAttribute<SE, E> joinField) {
		return new ForeignOwnedCategory<>(name, joinField, entries);
	}
	
	public <SE extends UUIDBase> ForeignOwnedCategory<SE, E> asForeignOwned(String name,
			PluralAttribute<SE,?, E> joinField) {
		return new ForeignOwnedCategory<>(name, joinField, entries);
	}
}
