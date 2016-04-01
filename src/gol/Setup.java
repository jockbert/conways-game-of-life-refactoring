package gol;

import gol.GameOfLife.ProgramConfig;
import gol.Simulation.SimulationConfig;
import gol.world.FileWorldSource;
import gol.world.World;
import gol.world.WorldGenerator;
import gol.world.WorldSource.WorldSourceResult;

import java.util.Optional;

public class Setup {

	private int defaultWidth;
	private int defaultHeight;

	Setup(int defaultWidth, int defaultHeight) {
		this.defaultWidth = defaultWidth;
		this.defaultHeight = defaultHeight;
	}

	SimulationConfig programToSimulationConf(ProgramConfig progConf) {

		World world;
		int width;
		int height;

		Optional<String> filePath = progConf.filePath;
		if (filePath.isPresent()) {
			WorldSourceResult result = readFile(filePath);

			world = result.world();
			width = progConf.width.orElse(result.width());
			height = progConf.height.orElse(result.height());
		} else {
			width = progConf.width.orElse(defaultWidth);
			height = progConf.height.orElse(defaultHeight);
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

	private WorldSourceResult readFile(Optional<String> filePath) {
		return new FileWorldSource(filePath.get()).generate();
	}

	private StepPrinter stepPrinter(ProgramConfig progConf, int width,
			int height) {
		return StepPrinter.fixedViewPort(width, height, progConf.outputFormat);
	}
}
