package gol.world;

import gol.Cell;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public interface CoordBoolMap extends Iterable<Cell> {

	boolean isTrue(int x, int y);

	void setTrue(int x, int y);

	static CoordBoolMap twoDimSetBoolMap() {
		final Set<Integer> empty = new HashSet<>();

		return new CoordBoolMap() {

			private Map<Integer, Set<Integer>> map = new HashMap<Integer, Set<Integer>>();

			@Override
			public void setTrue(int x, int y) {
				Set<Integer> line = map.get(y);

				if (line == null) {
					line = new HashSet<Integer>();
					map.put(y, line);
				}

				line.add(x);
			}

			@Override
			public boolean isTrue(int x, int y) {
				return map.getOrDefault(y, empty).contains(x);
			}

			@Override
			public Iterator<Cell> iterator() {
				return new NestedMapIterator<>(map, set -> set.iterator());
			}
		};
	}
}
