package gol.world.neo;

public class LookupCalc implements MiddleLineCalculator {

	private int tableSize;
	private int maxLineValue;
	private short[] table;
	private int paddedWidth;

	public LookupCalc(int width) {

		paddedWidth = width + 2;
		tableSize = 1 << (paddedWidth * 3);
		maxLineValue = (1 << paddedWidth) - 1;

		table = new short[tableSize];
		MiddleLineCalculator calc = new RawCalc(width);

		for (int line1 = 0; line1 <= maxLineValue; ++line1) {
			for (int line2 = 0; line2 <= maxLineValue; ++line2) {
				for (int line3 = 0; line3 <= maxLineValue; ++line3) {
					int key = getLookupKey(line1, line2, line3);
					int result = calc.calculate(line1, line2, line3);
					table[key] = (short) result;
				}
			}
		}
	}

	public int getTableSize() {
		return table.length;
	}

	@Override
	public int calculate(int line1, int line2, int line3) {

		int key = getLookupKey(line1, line2, line3);
		return table[key];
	}

	public int getMaxLineValue() {
		return maxLineValue;
	}

	public int getLookupKey(int line1, int line2, int line3) {
		int mask = maxLineValue;
		line1 &= mask;
		line2 &= mask;
		line3 &= mask;

		int line1Shifted = line1 << (2 * paddedWidth);
		int line2Shifted = line2 << paddedWidth;
		return line1Shifted + line2Shifted + line3;
	}
}
