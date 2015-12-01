package gol;

import gol.WorldSource.WorldSourceResult;
import gol.output.BigOFormat;
import gol.output.SpacedAtFormat;

import java.util.Arrays;
import java.util.Iterator;

public class GameOfLife {

	static void line(String s) {
		System.out.println(s);
	}

	static int nextArgAsInt(Iterator<String> args) {
		int n = Integer.parseInt(args.next());
		if (n < 0)
			throw new RuntimeException("Invalid argument value " + n);
		return n;
	}

	public static void main(String[] args) {

		try {
			Simulation game = new Simulation();
			game.computationTimeStart = System.currentTimeMillis();
			parseArguments(args, game);

			game.height = game.height == -1 ? 15 : game.height;
			game.width = game.width == -1 ? 20 : game.width;
			game.steps = game.steps == -1 ? 100 : game.steps;

			if (game.world == null) {
				WorldSource source = new RandomWorldSource(game.width,
						game.height);
				WorldSourceResult result = source.generate();
				game.world = result.world();
			}

			game.runSimulation();

		} catch (Exception e) {
			printHelp(e.getMessage());
		}
	}

	private static void parseArguments(String[] args, Simulation game)
			throws Exception {

		Iterator<String> argIterator = Arrays.asList(args).iterator();

		while (argIterator.hasNext()) {

			String arg = argIterator.next();
			switch (arg) {
			case "-s":
				game.steps = nextArgAsInt(argIterator);
				break;
			case "-f":
				String filePath = argIterator.next();
				WorldSource source = new FileWorldSource(filePath);
				WorldSourceResult result = source.generate();
				game.world = result.world();

				if (game.height == -1)
					game.height = result.height();
				if (game.width == -1)
					game.width = result.width();

				break;
			case "-?":
				throw new Exception("Help requested");
			case "-@":
				game.outputFormat = new SpacedAtFormat();
				break;
			case "-O":
				game.outputFormat = new BigOFormat();
				break;
			case "-w":
				game.width = nextArgAsInt(argIterator);
				break;
			case "-h":
				game.height = nextArgAsInt(argIterator);
				break;
			case "-l":
				game.historyLength = nextArgAsInt(argIterator);
				break;
			case "-t":
				game.stepDelay = nextArgAsInt(argIterator);
				break;
			case "-q":
				game.quietMode = true;
				break;
			default:
				throw new Exception("Unknown argument " + arg);
			}
		}
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
