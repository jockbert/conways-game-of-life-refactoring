package gol;

import java.util.OptionalInt;

public class Simulation {

	static class SimulationConfig {
		int stepLimit;
		boolean quietMode;
		World world;
		PeriodicBlocker periodicBlocker;
		LoopDetector loopDetector;
		StepPrinter stepPrinter;
	}

	void runSimulation(SimulationConfig conf) {
		OptionalInt loop = OptionalInt.empty();
		World world = conf.world;

		for (int step = 0; step <= conf.stepLimit && !loop.isPresent(); ++step) {
			if (step != 0)
				world = world.nextWorld();

			loop = conf.loopDetector.addSimulationStepAndDetect(world);

			if (!conf.quietMode || step == conf.stepLimit || loop.isPresent())
				conf.stepPrinter.printStep(world, step, loop);

			conf.periodicBlocker.blockRestOfPeriodAndRestart();
		}
	}
}
