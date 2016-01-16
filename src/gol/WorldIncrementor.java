package gol;

import static gol.Cell.cell;
import gol.world.World;

public interface WorldIncrementor {

	World next(World oldWorld);

	static WorldIncrementor basic() {
		final Cell[] ALL_DIRECTIONS = new Cell[] { cell(-1, -1), cell(-1, 0),
				cell(-1, 1), cell(1, -1), cell(1, 0), cell(1, 1), cell(0, -1),
				cell(0, 1) };

		return new WorldIncrementor() {

			World newWorld;
			World oldWorld;

			@Override
			public World next(World world) {
				oldWorld = world;
				newWorld = World.create();

				for (Cell c : oldWorld.getAliveCells())
					calcNextGenAliveCellsNearby(c);

				return newWorld;
			}

			private void calcNextGenAliveCellsNearby(Cell c) {
				for (Cell neighbour : ALL_DIRECTIONS)
					if (willLive(c.add(neighbour)))
						newWorld.setAlive(c.add(neighbour));

				if (willLive(c))
					newWorld.setAlive(c);
			}

			private boolean willLive(Cell cell) {

				int count = 0;

				for (Cell neighbour : ALL_DIRECTIONS) {
					if (oldWorld.isAlive(cell.add(neighbour)))
						count++;
					if (count > 3)
						return false;
				}

				return count == 3 || (count == 2 && oldWorld.isAlive(cell));
			}
		};
	}

}
