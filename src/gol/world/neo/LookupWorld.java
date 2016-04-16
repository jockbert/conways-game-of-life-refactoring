package gol.world.neo;

import gol.Cell;
import gol.world.World;

import java.util.function.Supplier;

public class LookupWorld implements World {

	private static final int FRAG_SIZE = 5;
	private static final MiddleLineCalculator calc = new LookupCalc(FRAG_SIZE);

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
		Supplier<Line> lineFactory = () -> Line.defaultLine();

		LineCalculator lineCalculator = LineCalculator.defaultCalc(lineFactory,
				calc);

		LookupWorld result = new LookupWorld();

		Line line1 = null;
		Line line2 = new BasicLine();
		Line line3 = new BasicLine();

		int max = lines.getMax() + 1;

		for (int lineIndex = lines.getMin() - 1; lineIndex <= max; lineIndex++) {
			line1 = line2;
			line2 = line3;
			line3 = lines.getOrMiss(lineIndex + 1);

			Line newLine2 = lineCalculator.nextMiddleLine(asFrags(line1),
					asFrags(line2), asFrags(line3));

			if (!newLine2.isEmpty()) {
				result.lines.set(lineIndex, newLine2);
			}
		}

		return result;
	}

	private LineFragments asFrags(Line line1) {
		return new LineFragments(FRAG_SIZE, line1);
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

			Line line = lines.getOrMiss(c.y);
			Integer nextX = line.nextAliveInclusive(c.x);

			if (nextX == null) {
				Line nextLine = lines.getOrMiss(c.y + 1);
				c = c.incY(nextLine.minSetBit());
			} else {
				return Cell.cell(nextX, c.y);
			}

		}

		return null;
	}
}
