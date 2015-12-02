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
		OptionalInt loop = OptionalInt.empty();

		for (int step = 0; step <= steps && !loop.isPresent(); ++step) {

			if (step != 0)
				world = world.nextWorld();

			loop = loopDetector.addSimulationStepAndDetect(world);
			printStep(step, loop);
			periodicBlocker.blockRestOfPeriodAndRestart();
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

			System.out.println(getTitle(stepCount, loop));
			System.out.println();
		}
	}

	private String getTitle(int stepCount, OptionalInt loop) {
		if (stepCount == 0)
			return "start";
		else if (loop.isPresent())
			return String.format("step %s - loop of length %s detected",
					stepCount, loop.getAsInt());
		else
			return "step " + stepCount;
	}
}
