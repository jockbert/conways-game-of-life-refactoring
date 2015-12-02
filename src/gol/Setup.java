package gol;

import gol.GameOfLife.ProgramConfig;
import gol.Simulation.SimulationConfig;
import gol.WorldSource.WorldSourceResult;

import java.util.Optional;
import java.util.function.Supplier;

public class Setup {

	private int defaultWidth;
	private int defaultHeight;
	private int defaultStepLimit;

	Setup(int defaultWidth, int defaultHeight, int defaultStepLimit) {
		this.defaultWidth = defaultWidth;
		this.defaultHeight = defaultHeight;
		this.defaultStepLimit = defaultStepLimit;
	}

	SimulationConfig programToSimulationConf(ProgramConfig progConf) {
		
		WorldSource ws = fileWS(progConf).orElseGet(randomWS(progConf));
		WorldSourceResult result = ws.generate();

		int width = progConf.width.orElse(result.width());
		int height = progConf.height.orElse(result.height());

		SimulationConfig simConf = new SimulationConfig();
		simConf.quietMode = progConf.quietMode;
		simConf.stepLimit = progConf.stepLimit.orElse(defaultStepLimit);
		simConf.loopDetector = progConf.loopDetector;
		simConf.periodicBlocker = progConf.periodicBlocker;
		simConf.world = result.world();
		simConf.stepPrinter = stepPrinter(progConf, width, height);
		return simConf;
	}

	private StepPrinter stepPrinter(ProgramConfig progConf, int width,
			int height) {
		return StepPrinter.fixedViewPort(width, height, progConf.outputFormat);
	}

	private Optional<WorldSource> fileWS(ProgramConfig progConf) {
		return progConf.filePath.map(FileWorldSource::new);
	}

	private Supplier<WorldSource> randomWS(ProgramConfig progConf) {
		return () -> {
			int width = progConf.width.orElse(defaultWidth);
			int height = progConf.height.orElse(defaultHeight);
			return new RandomWorldSource(width, height);
		};
	}
}
