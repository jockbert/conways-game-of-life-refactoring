package gol;

import gol.Simulation.SimulationConfig;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;

public class GameOfLife {
	private static final int DEFAULT_HEIGHT = 15;
	private static final int DEFAULT_WIDTH = 20;
	private static final int DEFAULT_STEP_LIMIT = 100;

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

	public static void main(String[] args) {

		try {
			Setup setup = new Setup(DEFAULT_WIDTH, DEFAULT_HEIGHT);
			ProgramConfig progConf = parseArguments(args);

			boolean hasNoInputFile = !progConf.filePath.isPresent();
			boolean hasNoSteps = progConf.stepLimit == 0;
			int width = progConf.width.orElse(DEFAULT_WIDTH);
			int height = progConf.height.orElse(DEFAULT_HEIGHT);
			OutputFormat outputFormat = progConf.outputFormat;

			if (hasNoSteps && hasNoInputFile) {
				printZeroStepRandomWorld(width, height, outputFormat);
			} else {

				SimulationConfig simConf = setup
						.programToSimulationConf(progConf);

				new Simulation().runSimulation(simConf);
			}

		} catch (Exception e) {
			printHelp(e.getMessage());
		}
	}

	private static void printZeroStepRandomWorld(int width, int height,
			OutputFormat format) {

		Random rand = new Random();

		for (int y = 0; y < height; ++y) {
			StringBuilder sb = new StringBuilder();
			for (int x = 0; x < width; ++x)
				sb.append(format.cell(rand.nextBoolean()));
			System.out.println(sb.toString());
		}

		System.out.println("start");
		System.out.println();
	}

	private static ProgramConfig parseArguments(String[] args) throws Exception {

		ProgramConfig conf = new ProgramConfig();
		ArgumentParser parser = new ArgumentParser();

		parser.onError(GameOfLife::gotoHelp);
		parser.strArg("-f", s -> conf.filePath = Optional.of(s));

		parser.intArg("-s", n -> conf.stepLimit = n);
		parser.intArg("-w", n -> conf.width = opt(n));
		parser.intArg("-h", n -> conf.height = opt(n));
		parser.intArg("-l",
				n -> conf.loopDetector = LoopDetector.ofMaxLength(n));
		parser.intArg("-t", n -> conf.periodicBlocker.setPeriod(n));

		parser.flag("-?", () -> gotoHelp("Help requested"));
		parser.flag("-@", () -> conf.outputFormat = OutputFormat.spacedAt());
		parser.flag("-O", () -> conf.outputFormat = OutputFormat.bigO());
		parser.flag("-q", () -> {
			conf.quietMode = true;
			conf.periodicBlocker = PeriodicBlocker.none();
		});

		parser.apply(args);

		return conf;
	}

	static OptionalInt opt(int n) {
		return OptionalInt.of(n);
	}

	private static void gotoHelp(String message) {
		throw new RuntimeException(message);
	}

	private static void printHelp(String message) {
		line(message);

		line("");
		line("Usage");
		line(" java gol.GameOfLife [ARGUMENTS...]");
		line("");
		line(" arguments:");
		line("   -?              Prints this usage help.");
		line("   -w <WIDTH>      Width of simulation view port. Default is 20.");
		line("   -h <HEIGHT>     Height of simulation view port. Default is 15.");
		line("   -f <FILE_PATH>  File with start state. Default is a random start state.");
		line("   -@              Use spaced '@' and '.' instead of default '#' and '-'.");
		line("   -O              Use 'O' instead of default '#'.");
		line("   -s <STEPS>      Number of maximum generation steps. Default is 100.");
		line("   -l <X>          Detect loops of maximum length x. Default is 0 - no loop detection.");
		line("   -t <MS>         Time delay (ms) to wait between each step. Default is 0 ms.");
		line("   -q              Quiet mode. Only outputs the last step in a simulation. Ignores time delay.");
	}

	static void line(String s) {
		System.out.println(s);
	}
}
