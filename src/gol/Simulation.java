package gol;

import gol.output.DefaultHashDashFormat;
import gol.output.OutputFormat;

import java.util.OptionalInt;

public class Simulation {

	int steps = -1;
	World world = null;
	int height = -1;
	int width = -1;
	boolean quietMode = false;
	OutputFormat outputFormat = new DefaultHashDashFormat();
	PeriodicBlocker periodicBlocker = PeriodicBlocker.defaultWithNoPeriod();
	LoopDetector loopDetector = LoopDetector.none();

	void runSimulation() {

		for (int stepCount = 0; stepCount <= steps; ++stepCount) {

			if (stepCount != 0)
				world = world.nextWorld();

			OptionalInt loop = loopDetector.addSimulationStepAndDetect(world);

			printStep(stepCount, loop);

			periodicBlocker.blockRestOfPeriodAndRestart();

			if (loop.isPresent())
				break;
		}
	}

	private void printStep(int stepCount, OptionalInt loop) {
		if (!quietMode || stepCount == steps || loop.isPresent()) {
			for (int y = 0; y < height; ++y) {
				String line = "";
				for (int x = 0; x < width; ++x) {
					line += outputFormat.cell(world.isAlive(x, y));
				}
				System.out.println(line);
			}

			if (stepCount == 0) {
				System.out.println("start");
			} else {
				String loopText = loop.isPresent() ? " - loop of length " + loop.getAsInt() + " detected": "";

				System.out.println("step " + stepCount + loopText);
			}
			System.out.println();
		}
	}
}
