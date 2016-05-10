package gol.world.neo;

import gol.Cell;
import gol.world.World;

import java.util.function.Supplier;

public class LookupWorld implements World {

	private static final int FRAG_SIZE = 5;
	private static final Supplier<FragmentedLine> NULL_SUPPLIER = () -> new FragmentedLine(
			createFragments());

	private static BasicFragments createFragments() {
		return new BasicFragments(FRAG_SIZE);
	}

	private static final MiddleLineCalculator calc = new LookupCalc(FRAG_SIZE);

	Sequence<FragmentedLine> lines = new Sequence<>(NULL_SUPPLIER);

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
		LineCalculator lineCalculator = LineCalculator.defaultCalc(
				LookupWorld::createFragments, calc);

		LookupWorld result = new LookupWorld();

		Fragments line1 = null;
		Fragments line2 = createFragments();
		Fragments line3 = createFragments();

		int max = lines.getMax() + 1;
		int min = lines.getMin() - 1;

		for (int lineIndex = min; lineIndex <= max; lineIndex++) {
			line1 = line2;
			line2 = line3;
			line3 = lines.getOrMiss(lineIndex + 1).fragments;

			Fragments newFrags2 = lineCalculator.nextMiddleLine(line1, line2,
					line3);

			FragmentedLine newLine2 = new FragmentedLine(newFrags2);

			if (!newLine2.isEmpty()) {
				result.lines.set(lineIndex, newLine2);
			}
		}

		return result;
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
