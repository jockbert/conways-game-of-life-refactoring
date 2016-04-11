package gol.world.neo;

import java.util.Iterator;

public class RangeIterator implements Iterator<Integer> {

	int n;
	int max;

	public RangeIterator(int min, int max) {
		this.n = min;
		this.max = max;
	}

	@Override
	public boolean hasNext() {
		return n <= max;
	}

	@Override
	public Integer next() {
		return n++;
	}
}
