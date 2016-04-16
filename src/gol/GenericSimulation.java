package gol;

import gol.world.World;

import java.util.OptionalInt;

public class GenericSimulation implements Simulation {

	static class SimulationConfig {
		int stepLimit;
		boolean quietMode;
		World world;
		PeriodicBlocker periodicBlocker;
		LoopDetector loopDetector;
		StepPrinter stepPrinter;
	}

	private SimulationConfig conf;

	GenericSimulation(SimulationConfig conf) {
		this.conf = conf;
	}

	@Override
	public void runSimulation() {
		OptionalInt loop = OptionalInt.empty();
		World world = conf.world;

		for (int step = 0; doIterate(conf, loop, step); ++step) {
			if (step != 0)
				world = world.nextWorld();

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
