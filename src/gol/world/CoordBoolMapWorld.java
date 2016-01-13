package gol.world;

import static gol.Cell.cell;
import gol.Cell;

import java.util.Set;

public class CoordBoolMapWorld implements World {

	private final static Cell[] ALL_DIRECTIONS = new Cell[] { cell(-1, -1),
			cell(-1, 0), cell(-1, 1), cell(1, -1), cell(1, 0), cell(1, 1),
			cell(0, -1), cell(0, 1) };

	private final CoordBoolMap map = CoordBoolMap.twoDimSetBoolMap();

	public CoordBoolMapWorld(Set<Cell> cells) {
		for (Cell cell : cells)
			map.setTrue(cell.x, cell.y);
	}

	@Override
	public boolean isAlive(int x, int y) {
		return isAlive(cell(x, y));
	}

	private boolean isAlive(Cell c) {
		return map.isTrue(c.x, c.y);
	}

	@Override
	public Set<Cell> getAliveCells() {
		return map.getAllTrue();
	}

	@Override
	public World nextWorld() {
		World world = World.create();

		for (Cell c : getAliveCells())
			getAllNextGenNearbyCells(world, c);

		return world;
	}

	private void getAllNextGenNearbyCells(World world, Cell c) {
		for (Cell neighbour : ALL_DIRECTIONS)
			if (willLive(c.add(neighbour)))
				world.setAlive(c.add(neighbour));

		if (willLive(c))
			world.setAlive(c);
	}

	private boolean willLive(Cell cell) {

		int count = 0;

		for (Cell neighbour : ALL_DIRECTIONS) {
			if (isAlive(cell.add(neighbour)))
				count++;
		}

		return count == 3 || (count == 2 && isAlive(cell));
	}

	@Override
	public void setAlive(int x, int y) {
		map.setTrue(x, y);
	}

	@Override
	public void setAlive(Cell c) {
		setAlive(c.x, c.y);
	}
}
