package bildverwaltung.gui.fx.util;

import bildverwaltung.dao.entity.Picture;

import java.util.List;
import java.util.ListIterator;

public class PictureIterator implements ListIterator<Picture> {

	List<Picture> pictures;
	ListIterator<Picture> stdIterator;
	private long iteratorSize;

	public PictureIterator(List<Picture> pictures, int position) {
		this.pictures = pictures;
		stdIterator = pictures.listIterator();
		iteratorSize = pictures.spliterator().getExactSizeIfKnown();
		seek(position);
	}

	public void seek(int pos) {
		stdIterator = pictures.listIterator();
		for(int i = 0; i < pos; i++)  {
			stdIterator.next();
		}
	}

	public long getIteratorSize() {
		return iteratorSize;
	}

	@Override
	public boolean hasNext() {
		return stdIterator.hasNext();
	}

	@Override
	public Picture next() {
		if(!stdIterator.hasNext()) {
			stdIterator = pictures.listIterator();
		}
		return stdIterator.next();
	}

	@Override
	public boolean hasPrevious() {
		return stdIterator.hasPrevious();
	}

	@Override
	public Picture previous() {
		if(!stdIterator.hasPrevious()) {
			stdIterator = pictures.listIterator();
			while(stdIterator.hasNext()) {
				stdIterator.next();
			}
		}

		return stdIterator.previous();
	}

	@Override
	public int nextIndex() {
		return stdIterator.nextIndex();
	}

	@Override
	public int previousIndex() {
		return stdIterator.previousIndex();
	}

	@Override
	public void remove() {
		stdIterator.remove();
	}

	@Override
	public void set(Picture picture) {
		stdIterator.set(picture);
	}

	@Override
	public void add(Picture picture) {
		stdIterator.add(picture);
	}
}
