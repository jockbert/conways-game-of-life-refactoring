package gol;

import gol.Simulation.SimulationConfig;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.OptionalInt;

public class GameOfLife {

	static class ProgramConfig {
		OptionalInt stepLimit = OptionalInt.empty();
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
			Setup setup = new Setup(20, 15, 100);
			Iterator<String> argIt = Arrays.asList(args).iterator();
			ProgramConfig progConf = parseArguments(argIt);
			SimulationConfig simConf = setup.programToSimulationConf(progConf);

			new Simulation().runSimulation(simConf);

		} catch (Exception e) {
			printHelp(e.getMessage());
		}
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
				conf.outputFormat = OutputFormat.spacedAt();
				break;
			case "-O":
				conf.outputFormat = OutputFormat.bigO();
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
