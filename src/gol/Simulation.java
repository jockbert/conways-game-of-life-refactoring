package gol;

import gol.world.World;

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
		WorldIncrementor incrementor = WorldIncrementor.basic();

		for (int step = 0; doIterate(conf, loop, step); ++step) {
			if (step != 0)
				world = incrementor.next(world);

			loop = conf.loopDetector.addSimulationStepAndDetect(world);

			if (doPrintStep(conf, loop, step))
				conf.stepPrinter.printStep(world, step, loop);

			conf.periodicBlocker.blockRestOfPeriodAndRestart();
		}
	}

	private boolean doIterate(SimulationConfig conf, OptionalInt loop, int step) {
		return step <= conf.stepLimit && !loop.isPresent();
	}

	private boolean doPrintStep(SimulationConfig conf, OptionalInt loop,
			int step) {
		return !conf.quietMode || step == conf.stepLimit || loop.isPresent();
	}
}
