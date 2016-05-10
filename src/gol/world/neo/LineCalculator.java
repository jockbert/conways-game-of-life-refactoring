package gol.world.neo;

import java.util.function.Supplier;

public interface LineCalculator {

	static LineCalculator defaultCalc(Supplier<Fragments> fragsFactory,
			MiddleLineCalculator calc) {
		return new LineCalculator() {

			@Override
			public Fragments nextMiddleLine(Fragments line1, Fragments line2,
					Fragments line3) {

				Fragments resultFragments = fragsFactory.get();

				int minIndex = Indexed.min(line1, line2, line3);
				int maxIndex = Indexed.max(line1, line2, line3);

				for (int i = minIndex; i <= maxIndex; i++) {
					int frag1 = line1.get(i);
					int frag2 = line2.get(i);
					int frag3 = line3.get(i);

					int resultFrag = calc.calculate(frag1, frag2, frag3);

					resultFragments.set(i, resultFrag << 1);
				}

				return resultFragments;
			}
		};
	}

	Fragments nextMiddleLine(Fragments line1, Fragments line2, Fragments line3);
}
