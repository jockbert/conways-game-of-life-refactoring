package gol.world;

import static gol.Cell.cell;
import gol.Cell;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class NestedMapIterator<LINE> implements Iterator<Cell> {

	private Iterator<Entry<Integer, LINE>> lineIt;
	private Function<LINE, Iterator<Integer>> lineToXIteratorFn;
	private Iterator<Integer> xIt = null;
	private Entry<Integer, LINE> lineEntry;

	private Integer x = null;
	private Integer y = null;

	public NestedMapIterator(final Map<Integer, LINE> map,
			final Function<LINE, Iterator<Integer>> lineToXIteratorFn) {
		this.lineIt = map.entrySet().iterator();
		this.lineToXIteratorFn = lineToXIteratorFn;
	}

	@Override
	public boolean hasNext() {

		if (x != null)
			return true;

		if (xIt != null && xIt.hasNext()) {
			x = xIt.next();
			return true;
		}

		if (!lineIt.hasNext())
			return false;

		lineEntry = lineIt.next();
		LINE line = lineEntry.getValue();
		xIt = lineToXIteratorFn.apply(line);
		y = lineEntry.getKey();
		return hasNext();
	}

	@Override
	public Cell next() {
		if (!hasNext())
			throw new NoSuchElementException();

		Cell cell = cell(x, y);
		x = null;
		return cell;
	}
}
