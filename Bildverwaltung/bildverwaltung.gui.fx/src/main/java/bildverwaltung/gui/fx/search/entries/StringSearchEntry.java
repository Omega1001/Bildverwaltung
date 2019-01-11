package bildverwaltung.gui.fx.search.entries;

import java.util.Arrays;
import java.util.List;
import javax.persistence.metamodel.SingularAttribute;

import bildverwaltung.dao.entity.UUIDBase;
import bildverwaltung.dao.helper.ComparisonMode;
import bildverwaltung.dao.helper.FilterDiscriptor;
import bildverwaltung.gui.fx.search.SearchEntry;
import bildverwaltung.gui.fx.search.SearchFieldGenerator;
import bildverwaltung.gui.fx.search.SearchRenderer;
import javafx.scene.control.TextField;

public class StringSearchEntry<E extends UUIDBase> extends SearchEntry<E, String> {

	private TextField value = new TextField();

	public StringSearchEntry(String name, SingularAttribute<E, String> field, String defaultValue, ComparisonMode defaultMode) {
		super(name, field,defaultValue,defaultMode);
	}

	@Override
	public List<FilterDiscriptor<E, ?>> asDescriptor() {
		FilterDiscriptor<E, ?> res = new FilterDiscriptor<E, String>(getField(), value.getText(),
				getComparisonMode());
		return Arrays.asList(res);
	}

	@Override
	public void render(SearchRenderer renderer) {
		renderer.beginSearchEntry();
		renderer.putSearchFieldLabel(getName());
		setComparisonMode(renderer.putCompairMode(ComparisonMode.values()));
		renderer.putInputField(value);
		renderer.newLine();
		renderer.endEntry();
	}
	
	@Override
	public void reset() {
		super.reset();
		value.setText(getDefaultValue());
	}

	public static class Generator implements SearchFieldGenerator<String> {

		@Override
		public <E extends UUIDBase> SearchEntry<E, ?> generate(String name, SingularAttribute<E, String> field, String defaultValue,ComparisonMode defaultMode) {
			if (String.class.equals(field.getBindableJavaType())) {
				return new StringSearchEntry<E>(name,field,(String)defaultValue,defaultMode);
			}else {
				throw new IllegalArgumentException("Type does not match");
			}
		}

	}

}
