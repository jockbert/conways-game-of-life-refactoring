package gol.world.neo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

public final class Sequence<T> implements Iterable<Integer> {

	private static final int EMPTY_MAX = Integer.MIN_VALUE;
	private static final int EMPTY_MIN = Integer.MAX_VALUE;

	private Supplier<T> nullSupplier;
	List<T> arr = null;
	int arrOffset = EMPTY_MIN;

	public Sequence(Supplier<T> nullSupplier) {
		this.nullSupplier = nullSupplier;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new RangeIterator(getMin(), getMax());
	}

	public T getOrMiss(int index) {
		return isOutOfRange(index) ? nullSupplier.get() : arr
				.get(arrIndex(index));
	}

	public T getOrAdd(int index) {
		if (isOutOfRange(index))
			set(index, nullSupplier.get());

		return arr.get(arrIndex(index));
	}

	public int getMax() {
		return isEmpty() ? EMPTY_MAX : arrOffset + arr.size() - 1;
	}

	public int getMin() {
		return arrOffset;
	}

	public boolean isEmpty() {
		return arr == null;
	}

	public void set(int index, T element) {
		initIfEmpty(index);
		assertIndexNotTooLow(index);
		padWithNull(index);
		arr.add(element);
	}

	private void initIfEmpty(int index) {
		if (isEmpty()) {
			arr = new ArrayList<T>();
			arrOffset = index;
		}
	}

	private int arrIndex(int index) {
		return index - arrOffset;
	}

	private void assertIndexNotTooLow(int index) {
		if (arrIndex(index) < 0)
			throwLowerThanFirst(index);
	}

	private boolean isOutOfRange(int index) {
		return index < getMin() || index > getMax();
	}

	private void padWithNull(int index) {
		int arrIndex = arrIndex(index);
		while (arr.size() < arrIndex)
			arr.add(nullSupplier.get());
	}

	private void throwLowerThanFirst(int index) {
		throw new LowerThanFirstException("unexpected index " + index
				+ " is lower than first set index " + arrOffset);
	}

	@Override
	public String toString() {
		String result = "";
		for (T t : arr) {
			result += t + "\n";
		}
		return result;
	}
}
