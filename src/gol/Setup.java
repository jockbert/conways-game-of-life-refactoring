package gol;

import gol.Simulation.SimulationConfig;
import gol.world.FileReader;
import gol.world.World;
import gol.world.WorldGenerator;
import gol.world.WorldResult;

import java.util.Optional;
import java.util.OptionalInt;

public class Setup {

	static final int DEFAULT_HEIGHT = 15;
	static final int DEFAULT_WIDTH = 20;
	static final int DEFAULT_STEP_LIMIT = 100;

	static class ProgramConfig {
		int stepLimit = DEFAULT_STEP_LIMIT;
		OptionalInt height = OptionalInt.empty();
		OptionalInt width = OptionalInt.empty();
		boolean quietMode = false;
		OutputFormat outputFormat = OutputFormat.defaultHashDash();
		PeriodicBlocker periodicBlocker = PeriodicBlocker.defaultWithNoPeriod();
		LoopDetector loopDetector = LoopDetector.none();
		Optional<String> filePath = Optional.empty();
	}

	SimulationConfig programToSimulationConf(ProgramConfig progConf) {

		World world;
		int width;
		int height;

		Optional<String> filePath = progConf.filePath;
		if (filePath.isPresent()) {
			WorldResult result = readFile(filePath.get());

			world = result.world();
			width = progConf.width.orElse(result.width());
			height = progConf.height.orElse(result.height());
		} else {
			width = progConf.width.orElse(DEFAULT_WIDTH);
			height = progConf.height.orElse(DEFAULT_HEIGHT);
			world = WorldGenerator.randomGenerator().generate(width, height);
		}

		SimulationConfig simConf = new SimulationConfig();
		simConf.quietMode = progConf.quietMode;
		simConf.stepLimit = progConf.stepLimit;
		simConf.loopDetector = progConf.loopDetector;
		simConf.periodicBlocker = progConf.periodicBlocker;
		simConf.world = world;
		simConf.stepPrinter = stepPrinter(progConf, width, height);
		return simConf;
	}

	private WorldResult readFile(String filePath) {
		return new FileReader().read(filePath);
	}

	private StepPrinter stepPrinter(ProgramConfig progConf, int width,
			int height) {
		return StepPrinter.fixedViewPort(width, height, progConf.outputFormat);
	}
}
