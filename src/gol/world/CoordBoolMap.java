package gol.world;

import static gol.Cell.cell;
import gol.Cell;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface CoordBoolMap {

	boolean isTrue(int x, int y);

	void setTrue(int x, int y);

	Set<Cell> getAllTrue();

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
			public Set<Cell> getAllTrue() {
				Set<Cell> result = new HashSet<>();

				for (int y : map.keySet()) {
					Set<Integer> line = map.get(y);
					for (int x : line)
						result.add(cell(x, y));
				}

				return result;
			}
		};
	}
}
