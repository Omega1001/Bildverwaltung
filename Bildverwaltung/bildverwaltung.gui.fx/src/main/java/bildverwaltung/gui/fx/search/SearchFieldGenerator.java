package bildverwaltung.gui.fx.search;

import javax.persistence.metamodel.SingularAttribute;

import bildverwaltung.dao.entity.UUIDBase;
import bildverwaltung.dao.helper.ComparisonMode;

public interface SearchFieldGenerator<V>{
	
	public <E extends UUIDBase> SearchEntry<E, ?> generate(String name,SingularAttribute<E, V> field, V defaultValue, ComparisonMode defaultMode);
	
}
