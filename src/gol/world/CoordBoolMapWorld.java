package gol.world;

import static gol.Cell.cell;
import gol.Cell;

import java.util.Iterator;

public class CoordBoolMapWorld implements World {

	private final CoordBoolMap map = CoordBoolMap.twoDimSetBoolMap();

	@Override
	public boolean isAlive(int x, int y) {
		return isAlive(cell(x, y));
	}

	@Override
	public boolean isAlive(Cell c) {
		return map.isTrue(c.x, c.y);
	}

	@Override
	public void setAlive(int x, int y) {
		map.setTrue(x, y);
	}

	@Override
	public void setAlive(Cell c) {
		setAlive(c.x, c.y);
	}

	@Override
	public Iterator<Cell> iterator() {
		return map.iterator();
	}
}
