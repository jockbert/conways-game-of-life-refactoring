package gol;

import java.util.LinkedList;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;

import gol.world.World;

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

	static LoopDetector ofMaxLength(int maxLoopLength) {

		return new LoopDetector() {
			private List<Set<Cell>> history = new LinkedList<>();

			@Override
			public OptionalInt addSimulationStepAndDetect(World world) {
				OptionalInt result = detectLoop(world);
				addToHistory(world);
				return result;
			}

			private OptionalInt detectLoop(World world) {
				Set<Cell> itemToFind = world.getAliveCells();
				return indexToResult(history.indexOf(itemToFind));
			}

			private OptionalInt indexToResult(int index) {
				if (index == -1)
					return OptionalInt.empty();
				else
					return OptionalInt.of(index + 1);
			}

			private void addToHistory(World world) {
				history.add(0, world.getAliveCells());
				if (history.size() == maxLoopLength + 1)
					history.remove(maxLoopLength);
			}
		};
	}
}
