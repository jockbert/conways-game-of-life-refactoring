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
	StepPrinter stepPrinter = null;

	void runSimulation() {
		OptionalInt loop = OptionalInt.empty();

		for (int step = 0; step <= steps && !loop.isPresent(); ++step) {
			if (step != 0)
				world = world.nextWorld();

			loop = loopDetector.addSimulationStepAndDetect(world);

			if (!quietMode || step == steps || loop.isPresent())
				stepPrinter.printStep(world, step, loop);

			periodicBlocker.blockRestOfPeriodAndRestart();
		}
	}
}
