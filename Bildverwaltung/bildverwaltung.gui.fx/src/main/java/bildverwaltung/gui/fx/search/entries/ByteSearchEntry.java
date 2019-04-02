package bildverwaltung.gui.fx.search.entries;

import bildverwaltung.dao.entity.UUIDBase;
import bildverwaltung.dao.helper.ComparisonMode;
import bildverwaltung.dao.helper.FilterDiscriptor;
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
    // private Supplier<ComparisonMode> compMode;

    public ByteSearchEntry(String name, SingularAttribute<E, Byte> field, Byte defaultValue, ComparisonMode defaultMode) {
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
        Byte val = value.getText() != null ? Byte.parseByte(value.getText()) : null;
        FilterDiscriptor<E, ?> res = new FilterDiscriptor<E, Byte>(getField(), val, getComparisonMode());
        return Arrays.asList(res);
    }

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
        Byte dv = getDefaultValue();
        value.setText(dv != null ? dv.toString() : null);
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
