package gol;

import static gol.Cell.cell;
import gol.world.CoordCountMap;
import gol.world.World;

public interface WorldIncrementor {

	World next(World oldWorld);

	static WorldIncrementor usingCountMap() {

		return new WorldIncrementor() {
			final Cell[] ALL_DIRECTIONS = new Cell[] { cell(-1, -1),
					cell(-1, 0), cell(-1, 1), cell(1, -1), cell(1, 0),
					cell(1, 1), cell(0, -1), cell(0, 1) };

			@Override
			public World next(World oldWorld) {
				return createNewWorld(oldWorld, countAliveNeighbors(oldWorld));
			}

			private CoordCountMap countAliveNeighbors(World oldWorld) {
				final CoordCountMap countMap = CoordCountMap.basic();

				for (final Cell aliveCell : oldWorld) {
					for (final Cell dir : ALL_DIRECTIONS) {
						final Cell neighbor = aliveCell.add(dir);
						countMap.increment(neighbor.x, neighbor.y);
					}
				}

				return countMap;
			}

			private World createNewWorld(World oldWorld,
					CoordCountMap aliveNeighborsCount) {
				final World newWorld = World.create();

				for (final Cell cell : aliveNeighborsCount) {
					final int count = aliveNeighborsCount.getCount(cell.x,
							cell.y);
					final boolean isAlive = oldWorld.isAlive(cell);

					if (willLive(isAlive, count))
						newWorld.setAlive(cell);
				}
				return newWorld;
			}

			private boolean willLive(boolean isAlive, int count) {
				return count == 3 || (count == 2 && isAlive);
			}
		};
	}

}
