package gol;

import gol.Simulation.SimulationConfig;
import gol.WorldSource.WorldSourceResult;
import gol.output.BigOFormat;
import gol.output.DefaultHashDashFormat;
import gol.output.OutputFormat;
import gol.output.SpacedAtFormat;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Supplier;

public class GameOfLife {

	static void line(String s) {
		System.out.println(s);
	}

	static int intArg(Iterator<String> args) {
		int n = Integer.parseInt(args.next());
		if (n < 0)
			throw new RuntimeException("Invalid argument value " + n);
		return n;
	}

	static OptionalInt optIntArg(Iterator<String> args) {
		return OptionalInt.of(intArg(args));
	}

	static OptionalInt keepOrElse(OptionalInt keep, int otherwise) {
		return keep.isPresent() ? keep : OptionalInt.of(otherwise);
	}

	private static class ProgramConfig {
		OptionalInt stepLimit = OptionalInt.empty();
		OptionalInt height = OptionalInt.empty();
		OptionalInt width = OptionalInt.empty();
		boolean quietMode = false;
		OutputFormat outputFormat = new DefaultHashDashFormat();
		PeriodicBlocker periodicBlocker = PeriodicBlocker.defaultWithNoPeriod();
		LoopDetector loopDetector = LoopDetector.none();
		Optional<String> filePath = Optional.empty();
	}

	public static void main(String[] args) {

		try {
			Iterator<String> argIt = Arrays.asList(args).iterator();
			ProgramConfig progConf = parseArguments(argIt);
			SimulationConfig simConf = programToSimulationConfig(progConf);

			Simulation sim = new Simulation();
			sim.runSimulation(simConf);

		} catch (Exception e) {
			printHelp(e.getMessage());
		}
	}

	private static SimulationConfig programToSimulationConfig(
			ProgramConfig progConf) {
		SimulationConfig simConf = new SimulationConfig();

		simConf.quietMode = progConf.quietMode;
		simConf.stepLimit = progConf.stepLimit.orElse(100);
		simConf.loopDetector = progConf.loopDetector;
		simConf.periodicBlocker = progConf.periodicBlocker;

		WorldSource ws = fileWS(progConf).orElseGet(randomWS(progConf));
		WorldSourceResult result = ws.generate();

		int width = progConf.width.orElse(result.width());
		int height = progConf.height.orElse(result.height());

		simConf.world = result.world();
		simConf.stepPrinter = StepPrinter.fixedViewPort(width, height,
				progConf.outputFormat);

		return simConf;
	}

	private static Optional<WorldSource> fileWS(ProgramConfig progConf) {
		return progConf.filePath.map(FileWorldSource::new);
	}

	private static Supplier<WorldSource> randomWS(ProgramConfig progConf) {
		return () -> {
			int width = progConf.width.orElse(20);
			int height = progConf.height.orElse(15);
			return new RandomWorldSource(width, height);
		};
	}

	private static ProgramConfig parseArguments(Iterator<String> args)
			throws Exception {

		ProgramConfig conf = new ProgramConfig();

		while (args.hasNext()) {
			String arg = args.next();
			switch (arg) {
			case "-s":
				conf.stepLimit = keepOrElse(conf.stepLimit, intArg(args));
				break;
			case "-f":
				conf.filePath = Optional.of(args.next());
				break;
			case "-?":
				throw new Exception("Help requested");
			case "-@":
				conf.outputFormat = new SpacedAtFormat();
				break;
			case "-O":
				conf.outputFormat = new BigOFormat();
				break;
			case "-w":
				conf.width = optIntArg(args);
				break;
			case "-h":
				conf.height = optIntArg(args);
				break;
			case "-l":
				conf.loopDetector = LoopDetector.ofMaxLength(intArg(args));
				break;
			case "-t":
				conf.periodicBlocker.setPeriod(intArg(args));
				break;
			case "-q":
				conf.quietMode = true;
				conf.periodicBlocker = PeriodicBlocker.none();
				break;
			default:
				throw new Exception("Unknown argument " + arg);
			}
		}

		return conf;
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
}
