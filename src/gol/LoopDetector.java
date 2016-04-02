package gol;

import gol.world.World;

import java.util.LinkedList;
import java.util.List;
import java.util.OptionalInt;

public interface LoopDetector {

	OptionalInt addSimulationStepAndDetect(World world);

	static LoopDetector none() {
		return new LoopDetector() {
			@Override
			public OptionalInt addSimulationStepAndDetect(World world) {
				return OptionalInt.empty();
			}
		};
	}

	static LoopDetector ofLength(int maxLoopLength) {

		return new LoopDetector() {
			private List<World> history = new LinkedList<>();

			@Override
			public OptionalInt addSimulationStepAndDetect(World world) {
				OptionalInt result = detectLoop(world);
				addToHistory(world);
				return result;
			}

			private OptionalInt detectLoop(World world) {
				return indexToResult(history.indexOf(world));
			}

			private OptionalInt indexToResult(int index) {
				if (index == -1)
					return OptionalInt.empty();
				else
					return OptionalInt.of(index + 1);
			}

			private void addToHistory(World world) {
				history.add(0, world);
				if (history.size() == maxLoopLength + 1)
					history.remove(maxLoopLength);
			}
		};
	}
}
