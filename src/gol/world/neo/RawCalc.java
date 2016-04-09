package gol.world.neo;

public final class RawCalc implements MiddleLineCalculator {

	private final int width;

	public RawCalc(int width) {
		this.width = width;
	}

	public int calculate(int line1, int line2, int line3) {
		int result = 0;
		for (int i = 0; i < width; ++i) {
			result <<= 1;
			result += willLive(line1, line2, line3);
			line1 >>= 1;
			line2 >>= 1;
			line3 >>= 1;
		}

		return reverse(result);
	}

	private int reverse(int value) {
		int result = 0;
		for (int i = 0; i < width; ++i) {
			result <<= 1;
			result += value & 1;
			value >>= 1;
		}
		return result;
	}

	private int willLive(int line1, int line2, int line3) {
		return count(line1) + count(line2) + count(line3) == 3 ? 1 : 0;
	}

	private int count(int line) {
		int count = 0;
		for (int i = 0; i < 3; i++) {
			count += line & 1;
			line >>= 1;
		}
		return count;
	}
}
