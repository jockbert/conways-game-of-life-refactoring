package gol.world.neo;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

public abstract class CalcTest {

	abstract MiddleLineCalculator calc(int width);

	public static class RawCalcTest extends CalcTest {
		@Override
		MiddleLineCalculator calc(int width) {
			return new RawCalc(width);
		}
	}

	public static class LookupTableCalcTest extends CalcTest {
		@Override
		LookupCalc calc(int width) {
			return new LookupCalc(width);
		}

		@Test
		public void testTableSize() throws Exception {
			assertEquals(512, calc(1).getTableSize());
			assertEquals(16777216, calc(6).getTableSize());
		}

		@Test
		public void testMaxLineValue() throws Exception {
			assertEquals(7, calc(1).getMaxLineValue());
			assertEquals(255, calc(6).getMaxLineValue());
		}

		@Test
		public void testLookupKey() throws Exception {
			assertEquals(0b111011001, calc(1).getLookupKey(0b111, 0b011, 0b001));
		}

		@Test
		@Ignore
		public void testTime() throws Exception {
			long startTime = System.currentTimeMillis();

			LookupCalc calc = new LookupCalc(6);

			long endTime = System.currentTimeMillis();

			System.out.println("Duration " + (endTime - startTime) + " ms");
			System.out.println("Result "
					+ calc.calculate(0b00000110, 0, 0b00000010));

		}
	}

	@Test
	public void testWidhtOne() throws Exception {
		MiddleLineCalculator calc = calc(1);

		assertCalculation(calc, 0b000, 0b000, 0b000, 0);
		assertCalculation(calc, 0b001, 0b000, 0b000, 0);
		assertCalculation(calc, 0b010, 0b000, 0b000, 0);
		assertCalculation(calc, 0b100, 0b000, 0b000, 0);
		assertCalculation(calc, 0b011, 0b000, 0b000, 0);
		assertCalculation(calc, 0b101, 0b000, 0b000, 0);
		assertCalculation(calc, 0b110, 0b000, 0b000, 0);
		assertCalculation(calc, 0b111, 0b000, 0b000, 1);
		assertCalculation(calc, 0b010, 0b010, 0b010, 1);
		assertCalculation(calc, 0b101, 0b010, 0b000, 1);
		assertCalculation(calc, 0b000, 0b100, 0b110, 1);
		assertCalculation(calc, 0b000, 0b101, 0b100, 1);
		assertCalculation(calc, 0b000, 0b111, 0b000, 1);

		assertCalculation(calc, 0b1000, 0b011, 0b000, 0);
		assertCalculation(calc, 0b000, 0b1011, 0b000, 0);
		assertCalculation(calc, 0b000, 0b011, 0b1000, 0);
	}

	@Test
	public void testWidhtTwo() throws Exception {
		MiddleLineCalculator calc = calc(2);

		assertCalculation(calc, 0b0000, 0b0000, 0b0000, 0b00);
		assertCalculation(calc, 0b1000, 0b1000, 0b1000, 0b10);
		assertCalculation(calc, 0b0000, 0b1001, 0b1000, 0b00);
		assertCalculation(calc, 0b0000, 0b0111, 0b0000, 0b01);
		assertCalculation(calc, 0b0000, 0b0111, 0b1000, 0b11);
		assertCalculation(calc, 0b0000, 0b0000, 0b1110, 0b10);
	}

	@Test
	public void testWidhtFive() throws Exception {
		MiddleLineCalculator calc = calc(5);

		assertCalculation(calc, 0b0000000, 0b0000000, 0b0000000, 0b00000);
		assertCalculation(calc, 0b1110111, 0b0000000, 0b0000000, 0b10001);
		assertCalculation(calc, 0b0000000, 0b1111011, 0b0000000, 0b11000);
	}

	private void assertCalculation(MiddleLineCalculator calc, int line1,
			int line2, int line3, int expected) {
		int actual = calc.calculate(line1, line2, line3);
		assertEquals(expected, actual);
	}
}
