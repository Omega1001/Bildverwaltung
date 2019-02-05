package bildverwaltung.gui.fx.util;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class StringProxy<E> {

	public static final <E> List<StringProxy<E>> convertList(List<E> items, Function<E, String> toStringTransformer) {
		List<StringProxy<E>> res = new LinkedList<>();
		for(E item : items) {
			res.add(new StringProxy<E>(item, toStringTransformer));
		}
		return res;
	}

	private final E actual;
	private final Function<E, String> toStringTransformer;

	public StringProxy(E actual, Function<E, String> toStringTransformer) {
		super();
		this.actual = actual;
		this.toStringTransformer = toStringTransformer;
	}

	public E getActual() {
		return actual;
	}

	@Override
	public String toString() {
		return actual != null ? toStringTransformer.apply(actual) : "";
	}
}
