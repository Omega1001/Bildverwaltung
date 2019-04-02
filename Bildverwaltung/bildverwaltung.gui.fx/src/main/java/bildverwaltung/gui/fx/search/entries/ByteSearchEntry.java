package bildverwaltung.gui.fx.search.entries;

import bildverwaltung.dao.entity.UUIDBase;
import bildverwaltung.dao.helper.ComparisonMode;
import bildverwaltung.dao.helper.FilterDiscriptor;
import bildverwaltung.dao.helper.FilterValueDiscriptor;
import bildverwaltung.gui.fx.search.SearchEntry;
import bildverwaltung.gui.fx.search.SearchFieldGenerator;
import bildverwaltung.gui.fx.search.SearchRenderer;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Arrays;
import java.util.List;

public class ByteSearchEntry <E extends UUIDBase> extends SearchEntry<E, Byte> {
    private TextField value;
    private TextField secondValue;
    // private Supplier<ComparisonMode> compMode;

    public ByteSearchEntry(String name, SingularAttribute<E, Byte> field, Byte defaultValue, ComparisonMode defaultMode) {
        super(name, field, defaultValue, defaultMode);
        value = constructValueElement();
        secondValue = constructValueElement();
        secondValue.setVisible(false);
    }

    private TextField constructValueElement() {
        TextField value = new TextField();
        value.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCharacter().length() != 0) {
                    char c = event.getCharacter().charAt(0);
                    if (c >= '0' && c <= '9') {
                        // Check if given number and already existing number in textbox exceeds the lmit of a Byte
                       String text = value.getText();
                       if(text != null) {
                           int size = Integer.parseInt(text + c);
                           if(size > 127 || size < -128) {
                               event.consume();
                           }
                       }
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
    	
    	
        Byte val = asByte(value.getText());
        Byte secondVal = asByte(secondValue.getText());
        FilterDiscriptor<E, ?> res = new FilterDiscriptor<E, Byte>(getField(), new FilterValueDiscriptor<Byte>(val, secondVal), getComparisonMode());
        return Arrays.asList(res);
    }

    private Byte asByte(String text) {
    	if(text != null && !"".equals(text)) {
    		return Byte.parseByte(text);
    	}
    	return null;
	}

	public void render(SearchRenderer renderer) {
        renderer.beginSearchEntry(this);
        renderer.putSearchFieldLabel(getName());
        setComparisonMode(renderer.putCompairMode(ComparisonMode.values()));
        renderer.putInputField(value);
        renderer.newLine();
        renderer.putInputField(secondValue);
        renderer.endEntry();
    }

    @Override
    public void reset() {
        super.reset();
        Byte dv = getDefaultValue();
        value.setText(dv != null ? dv.toString() : null);
    }
    
    @Override
    public void handleComparisonModeChange(ComparisonMode oldMode, ComparisonMode newMode) {
    	if (newMode == oldMode) {
    		//Do nothing
    	}else {
    		//Actual change
    		value.setDisable(ComparisonMode.DISABLED.equals(newMode));
    		secondValue.setVisible(requiresSecondValue(newMode));
    	}
    }

    private boolean requiresSecondValue(ComparisonMode newMode) {
		return ComparisonMode.IS_BETWEEN.equals(newMode) ||
				ComparisonMode.NOT_BETWEEN.equals(newMode);
	}

	public static class Generator implements SearchFieldGenerator<Byte> {

        @Override
        public <E extends UUIDBase> SearchEntry<E, ?> generate(String name, SingularAttribute<E, Byte> field,
                                                               Byte defaultValue, ComparisonMode defaultMode) {
            if (Byte.class.equals(field.getBindableJavaType()) || int.class.equals(field.getBindableJavaType())) {
                return new ByteSearchEntry<E>(name, field, (Byte) defaultValue, defaultMode);
            } else {
                throw new IllegalArgumentException("Type does not match");
            }
        }
    }
}
