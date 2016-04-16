package gol.world.neo;

import static org.junit.Assert.assertEquals;

import java.util.function.Supplier;

import org.junit.Test;

public class LineCalculatorTest {

	@Test
	public void testBlinkerHorisontal() throws Exception {
		int fragSize = 2;
		Supplier<Line> lineFactory = () -> Line.defaultLine();
		MiddleLineCalculator fragCalc = MiddleLineCalculator
				.defaultCalc(fragSize);
		LineCalculator calc = LineCalculator.defaultCalc(lineFactory, fragCalc);

		Line line1 = lineFactory.get();
		Line line2 = lineFactory.get();
		Line line3 = lineFactory.get();

		line3.set(1);
		line3.set(2);
		line3.set(3);

		LineFragments frag1 = new LineFragments(fragSize, line1);
		LineFragments frag2 = new LineFragments(fragSize, line2);
		LineFragments frag3 = new LineFragments(fragSize, line3);

		Line actualLine = calc.nextMiddleLine(frag1, frag2, frag3);

		Line expectedLine = Line.defaultLine();
		expectedLine.set(2);

		assertEquals(expectedLine, actualLine);
	}
}
