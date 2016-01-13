package gol.world;

import static gol.Cell.cell;
import gol.Cell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AliveCellsWorld implements World {

	private final static Cell[] ALL_DIRECTIONS = new Cell[] { cell(-1, -1),
			cell(-1, 0), cell(-1, 1), cell(1, -1), cell(1, 0), cell(1, 1),
			cell(0, -1), cell(0, 1) };

	private final Set<Cell> aliveCells;

	public AliveCellsWorld(Set<Cell> cells) {
		aliveCells = new HashSet<>(cells);
	}

	@Override
	public boolean isAlive(int x, int y) {
		return isAlive(cell(x, y));
	}

	private boolean isAlive(Cell c) {
		return aliveCells.contains(c);
	}

	@Override
	public Set<Cell> getAliveCells() {
		return new HashSet<>(aliveCells);
	}

	@Override
	public World nextWorld() {
		return new AliveCellsWorld(nextWorldSet());
	}

	private Set<Cell> nextWorldSet() {
		int initialCapacity = aliveCells.size() * 2;
		Set<Cell> result = new HashSet<>(initialCapacity);

		for (Cell c : aliveCells)
			result.addAll(getAllNextGenNearbyCells(c));

		return result;
	}

	private Collection<Cell> getAllNextGenNearbyCells(Cell c) {

		List<Cell> result = new ArrayList<>();

		for (Cell neighbour : ALL_DIRECTIONS)
			if (willLive(c.add(neighbour)))
				result.add(c.add(neighbour));

		if (willLive(c))
			result.add(c);

		return result;

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
		setAlive(cell(x, y));
	}

	@Override
	public void setAlive(Cell cell) {
		aliveCells.add(cell);
	}
}
