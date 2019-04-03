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
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class IntegerSearchEntry<E extends UUIDBase> extends SearchEntry<E, Integer> {

	private TextField value;
	// private Supplier<ComparisonMode> compMode;

	public IntegerSearchEntry(String name, SingularAttribute<E, Integer> field, Integer defaultValue, ComparisonMode defaultMode) {
		super(name, field, defaultValue, defaultMode);
		value = constructValueElement();
	}

	private TextField constructValueElement() {
		TextField value = new TextField();
		value.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCharacter().length() != 0) {
					char c = event.getCharacter().charAt(0);
					if (c >= '0' && c <= '9') {
						// Numbers are OK, do nothing
					} else {
						// Invalid char, stop event from spreading
						event.consume();
					}
				}
			}
		});
		return value;
	}

	@Override
	public List<FilterDiscriptor<E, ?>> asDescriptor() {
		Integer val = value.getText() != null ? Integer.parseInt(value.getText()) : null;
		FilterDiscriptor<E, ?> res = new FilterDiscriptor<E, Integer>(getField(), val, getComparisonMode());
		return Arrays.asList(res);
	}

	public void render(SearchRenderer renderer) {
		renderer.beginSearchEntry(this);
		renderer.putSearchFieldLabel(getName());
		setComparisonMode(renderer.putCompairMode(ComparisonMode.values()));
		renderer.putInputField(value);
		renderer.newLine();
		renderer.endEntry();
	}

	@Override
	public void reset() {
		super.reset();
		Integer dv = getDefaultValue();
		value.setText(dv != null ? dv.toString() : null);
	}
	
	 @Override
	    public void handleComparisonModeChange(ComparisonMode oldMode, ComparisonMode newMode) {
	    	if (newMode == oldMode) {
	    		//Do nothing
	    	}else {
	    		//Actual change
	    		value.setDisable(ComparisonMode.DISABLED.equals(newMode));
	    	}
	    }

	public static class Generator implements SearchFieldGenerator<Integer> {

		@Override
		public <E extends UUIDBase> SearchEntry<E, ?> generate(String name, SingularAttribute<E, Integer> field,
				Integer defaultValue, ComparisonMode defaultMode) {
			if (Integer.class.equals(field.getBindableJavaType()) || int.class.equals(field.getBindableJavaType())) {
				return new IntegerSearchEntry<E>(name, field, (Integer) defaultValue, defaultMode);
			} else {
				throw new IllegalArgumentException("Type does not match");
			}
		}
	}

}
