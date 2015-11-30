package gol;

import static gol.Cell.cell;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AliveCellsWorld implements World {

	private final static List<Cell> ALL_DIRECTIONS = Arrays.asList(
			cell(-1, -1), cell(-1, 0), cell(-1, 1), cell(1, -1), cell(1, 0),
			cell(1, 1), cell(0, -1), cell(0, 1));

	private final Set<Cell> aliveCells;

	public AliveCellsWorld(Set<Cell> cells) {
		aliveCells = new HashSet<>(cells);
	}

	public AliveCellsWorld(List<String> lines) {
		aliveCells = getAliveCells(lines);
	}

	public static Set<Cell> getAliveCells(List<String> lines) {
		Set<Cell> result = new HashSet<>();

		int height = lines.size();
		int width = lines.isEmpty() ? 0 : lines.get(0).length();

		for (int y = 0; y < height; ++y) {
			String line = lines.get(y);
			for (int x = 0; x < width; ++x) {
				if (line.charAt(x) == '#')
					result.add(cell(x, y));
			}
		}

		return result;
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
		Stream<Cell> neighbours = aliveCells.stream().flatMap(
				this::neighboursOf);
		Stream<Cell> candidates = Stream
				.concat(aliveCells.stream(), neighbours).distinct();
		Stream<Cell> nextGen = candidates
				.filter(this::shouldLiveNextGeneration);

		return new AliveCellsWorld(nextGen.collect(Collectors.toSet()));
	}

	private Stream<Cell> neighboursOf(Cell c) {
		return ALL_DIRECTIONS.stream().map(dir -> dir.add(c));
	}

	private long countAliveNeighboursOf(Cell c) {
		return neighboursOf(c).filter(this::isAlive).count();
	}

	private boolean shouldLiveNextGeneration(Cell c) {
		long aliveNeighs = countAliveNeighboursOf(c);
		return aliveNeighs == 3 || (isAlive(c) && aliveNeighs == 2);
	}
}
