package bildverwaltung.dao.helper;

public class FilterValueDiscriptor<V> {

	private final V value;
	private final V secondaryValue;

	public FilterValueDiscriptor(V value) {
		this(value, null);
	}

	public FilterValueDiscriptor(V value, V secondaryValue) {
		super();
		this.value = value;
		this.secondaryValue = secondaryValue;
	}

	public V getValue() {
		return value;
	}

	public V getSecondaryValue() {
		return secondaryValue;
	}

}