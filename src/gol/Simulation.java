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

		loopDetector.addSimulationStepAndDetect(world);
		printStep("start", 0, OptionalInt.empty());
		periodicBlocker.blockRestOfPeriodAndRestart();

		for (int stepCount = 1; stepCount <= steps; ++stepCount) {

			world = world.nextWorld();

			OptionalInt loop = loopDetector.addSimulationStepAndDetect(world);

			String loopText = loop.isPresent() ? " - loop of length "
					+ loop.getAsInt() + " detected" : "";
			String stepTitle = "step " + stepCount + loopText;

			printStep(stepTitle, stepCount, loop);
			periodicBlocker.blockRestOfPeriodAndRestart();

			if (loop.isPresent())
				break;
		}
	}

	private void printStep(String title, int stepCount, OptionalInt loop) {
		if (!quietMode || stepCount == steps || loop.isPresent()) {
			for (int y = 0; y < height; ++y) {
				String line = "";
				for (int x = 0; x < width; ++x) {
					line += outputFormat.cell(world.isAlive(x, y));
				}
				System.out.println(line);
			}

			System.out.println(title);
			System.out.println();
		}
	}
}
