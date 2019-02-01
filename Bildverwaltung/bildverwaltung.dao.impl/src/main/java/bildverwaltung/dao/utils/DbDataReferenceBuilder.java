package bildverwaltung.dao.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import bildverwaltung.utils.DBDataRefference;

public class DbDataReferenceBuilder {

	@SuppressWarnings("unchecked")
	public static <E> DBDataRefference<E> buildRef(Object[] tuple, Class<E> palceholder) {
		UUID key = null;
		E target = null;
		
		for (Object o : tuple) {
			if (UUID.class.isInstance(o)) {
				key = (UUID) o;
			} else if (palceholder.isInstance(o)) {
				target = (E) o;
			}
		}
		if(key != null) {
			return new DBDataRefference<E>(target, key);
		}else {
			throw new IllegalArgumentException();
		}
	}
	
	public static <E> List<DBDataRefference<E>> buildRef(List<Object[]> tuple, Class<E> palceholder) {
		List<DBDataRefference<E>> refs = new LinkedList<>();
		for(Object[] t : tuple) {
			refs.add(buildRef(t, palceholder));
		}
		return refs;
	}

}
