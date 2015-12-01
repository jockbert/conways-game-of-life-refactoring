package gol;

import gol.output.BigOFormat;
import gol.output.SpacedAtFormat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

			game.world = game.world == null ? createRandomWorld(game.width,
					game.height) : game.world;

			game.runSimulation();

		} catch (Exception e) {
			printHelp(e.getMessage());
		}
	}

	private static World createRandomWorld(int width, int height) {
		List<String> lines = new ArrayList<>();

		Random rand = new Random();
		for (int h = 0; h < height; h++) {
			String line = "";
			for (int w = 0; w < width; w++) {

				line += rand.nextBoolean() ? '#' : '-';
			}
			lines.add(line);
		}

		return new AliveCellsWorld(lines);
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
				List<String> lines = readWorldFile(game, filePath);

				if (game.height == -1)
					game.height = lines.size();
				if (game.width == -1)
					game.width = lines.isEmpty() ? 0 : lines.get(0).length();

				game.world = new AliveCellsWorld(lines);
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

	private static ArrayList<String> readWorldFile(Simulation game,
			String filePath) throws FileNotFoundException {
		File file = new File(filePath);
		Scanner scanner = new Scanner(file);
		ArrayList<String> world = new ArrayList<String>();
		int lineNumber = 1;
		int maxWidth = 0;
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			Pattern pattern = Pattern.compile("[^#-]");
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				scanner.close();
				throw new RuntimeException("Invalid character '"
						+ matcher.group() + "' on line " + lineNumber
						+ " in file " + filePath);
			}

			maxWidth = Math.max(maxWidth, line.length());

			world.add(line);
			lineNumber++;
		}

		for (int i = 0; i < world.size(); ++i) {
			String line = world.get(i);

			while (line.length() < maxWidth)
				line += '-';

			world.set(i, line);
		}
		scanner.close();
		return world;
	}
}
