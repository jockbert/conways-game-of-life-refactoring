package gol.world.neo;

import gol.Cell;
import gol.world.World;

public class LookupWorld implements World {

	Sequence<Line> lines = new Sequence<>(() -> new BasicLine());

	@Override
	public boolean isAlive(int x, int y) {
		return lines.getOrMiss(y).isSet(x);
	}

	@Override
	public void setAlive(int x, int y) {
		lines.getOrAdd(y).set(x);
	}

	@Override
	public World nextWorld() {
		return null;
	}

	@Override
	public Cell firstAlive() {
		return nextAlive(Cell.MIN);
	}

	@Override
	public Cell nextAlive(Cell exclusiveStart) {
		if (lines.isEmpty())
			return null;

		Cell c = exclusiveStart.incX();

		if (c.y < lines.getMin()) {
			Line firstLine = lines.getOrMiss(lines.getMin());
			c = Cell.cell(firstLine.minSetBit(), lines.getMin());
		}

		while (c.y <= lines.getMax()) {

			Integer nextX = lines.getOrMiss(c.y).nextAlive(c.x);

			if (nextX == null) {
				c = c.incY(lines.getOrMiss(c.y + 1).minSetBit());
			} else {
				return Cell.cell(nextX, c.y);
			}

		}

		return null;
	}
}
