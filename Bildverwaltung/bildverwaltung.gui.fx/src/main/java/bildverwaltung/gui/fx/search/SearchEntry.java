package bildverwaltung.gui.fx.search;

import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

import bildverwaltung.dao.entity.UUIDBase;
import bildverwaltung.dao.helper.ComparisonMode;
import bildverwaltung.dao.helper.FilterDiscriptor;
import bildverwaltung.gui.fx.util.ValueProxy;

public abstract class SearchEntry<E extends UUIDBase, V> {

	private final String name;
	private final SingularAttribute<E, V> field;
	private ValueProxy<ComparisonMode> comparisonMode = null;
	private final V defaultValue;
	private final ComparisonMode defaultMode;

	public SearchEntry(String name, SingularAttribute<E, V> field) {
		this(name, field, null, null);
	}

	public SearchEntry(String name, SingularAttribute<E, V> field, V defaultValue, ComparisonMode defaultMode) {
		super();
		this.name = name;
		this.field = field;
		this.defaultValue = defaultValue;
		this.defaultMode = defaultMode;
	}

	public abstract List<FilterDiscriptor<E, ?>> asDescriptor();

	protected SingularAttribute<E, V> getField() {
		return field;
	}

	protected ComparisonMode getComparisonMode() {
		return comparisonMode != null ? comparisonMode.get() : ComparisonMode.DISABLED;
	}

	protected void setComparisonMode(ValueProxy<ComparisonMode> valueProxy) {
		this.comparisonMode = valueProxy;
	}

	// public abstract Parent getGuiElement();

	public String getName() {
		return name;
	}

	public abstract void render(SearchRenderer renderer);

	public void reset() {
		if (comparisonMode != null) {
			comparisonMode.set(defaultMode != null ? defaultMode : ComparisonMode.DISABLED);
		}
	}

	public V getDefaultValue() {
		return defaultValue;
	}

}
