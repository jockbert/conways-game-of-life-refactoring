package gol.world;

import gol.Cell;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public interface CoordCountMap extends Iterable<Cell> {

	int getCount(int x, int y);

	void increment(int x, int y);

	static CoordCountMap basic() {
		return new CoordCountMap() {

			private final Map<Integer, Integer> EMPTY_LINE = new HashMap<>();
			private final Integer ZERO = 0;

			Map<Integer, Map<Integer, Integer>> map = new HashMap<>();

			@Override
			public int getCount(int x, int y) {
				return map.getOrDefault(y, EMPTY_LINE).getOrDefault(x, ZERO);
			}

			@Override
			public void increment(int x, int y) {
				Map<Integer, Integer> line = map.get(y);
				if (line == null) {
					line = new HashMap<>();
					map.put(y, line);
				}

				line.put(x, line.getOrDefault(x, ZERO) + 1);
			}

			@Override
			public Iterator<Cell> iterator() {
				return new NestedMapIterator<>(map, line -> line.keySet()
						.iterator());
			}
		};
	}
}
