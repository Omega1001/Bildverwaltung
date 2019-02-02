package bildverwaltung.utils;

import java.util.UUID;

public class DBDataRefference<E> {

	private final E placeholder;
	private final UUID refferencedComponentId;

	public DBDataRefference(E placeholder, UUID refferencedComponentId) {
		super();
		this.placeholder = placeholder;
		this.refferencedComponentId = refferencedComponentId;
	}

	public E getPlaceholder() {
		return placeholder;
	}

	public UUID getRefferencedComponentId() {
		return refferencedComponentId;
	}
	
	@Override
	public String toString() {
		return placeholder.toString();
	}

}
