package gol;

import gol.output.BigOFormat;
import gol.output.SpacedAtFormat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameOfLife {

	static void line(String s) {
		System.out.println(s);
	}

	static String getArg(List<String> argList) {
		String arg = argList.get(0);
		argList.remove(0);
		return arg;
	}

	static int getIntArg(List<String> argList) {
		String arg = getArg(argList);
		int n = Integer.parseInt(arg);
		if (n < 0)
			throw new RuntimeException("Invalid argument value " + n);
		return n;
	}

	public static void main(String[] args) {
		Simulation game = new Simulation();
		game.computationTimeStart = System.currentTimeMillis();

		try {
			List<String> argList = new LinkedList<String>(Arrays.asList(args));
			while (argList.size() > 0) {
				String arg = argList.get(0);
				argList.remove(0);
				if ("-s".equals(arg)) {
					game.steps = getIntArg(argList);
				} else if ("-f".equals(arg)) {
					String filePath = getArg(argList);
					game.world = new World(readWorldFile(game, filePath));

					if (game.height == -1)
						game.height = game.world.height();
					if (game.width == -1)
						game.width = game.world.width();

				} else if ("-?".equals(arg)) {
					throw new Exception("Help requested");
				} else if ("-@".equals(arg)) {
					game.outputFormat = new SpacedAtFormat();
				} else if ("-O".equals(arg)) {
					game.outputFormat = new BigOFormat();
				} else if (arg.equals("-w")) {
					game.width = getIntArg(argList);
				} else if ("-h".equals(arg))
					game.height = getIntArg(argList);
				else if ("-l".equals(arg))
					game.historyLength = getIntArg(argList);
				else if ("-t".equals(arg))
					game.stepDelay = getIntArg(argList);
				else if ("-q".equals(arg)) {
					game.quietMode = true;

				} else
					throw new Exception("Unknown argument " + arg);
			}

			if (game.world == null) {
				game.world = new World();

				game.height = game.height == -1 ? 15 : game.height;
				game.width = game.width == -1 ? 20 : game.width;

				Random rand = new Random();
				for (int h = 0; h < game.height; h++) {
					String line = "";
					for (int w = 0; w < game.width; w++) {

						line += rand.nextBoolean() ? '#' : '-';
					}
					game.world.add(line);
				}

			}

			if (game.steps == -1)
				game.steps = 100;

			game.runSimulation();

		} catch (Exception e) {
			line(e.getMessage());

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
