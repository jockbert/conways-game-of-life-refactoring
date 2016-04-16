package gol.world.neo;

public interface MiddleLineCalculator {

	int calculate(int line1, int line2, int line3);

	static MiddleLineCalculator defaultCalc(int fragLength) {
		return new LookupCalc(fragLength);
	}
}
