package gol;

import gol.output.BigOFormat;
import gol.output.DefaultHashDashFormat;
import gol.output.OutputFormat;
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

	private static final int NO_LOOP = 0;

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

	private int steps = -1;
	private World world = null;
	private int height = -1;
	private int width = -1;
	private List<History> history = new LinkedList<History>();
	private long computationTimeStart;
	private int historyLength;
	private int stepDelay = -1;
	private boolean quietMode = false;
	private OutputFormat outputFormat = new DefaultHashDashFormat();

	public static void main(String[] args) {
		GameOfLife game = new GameOfLife();
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

	private static ArrayList<String> readWorldFile(GameOfLife game,
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

	void printWorldLine(String line) {
		StringBuilder result = new StringBuilder();

		for (char c : line.toCharArray())
			result.append(outputFormat.cell(world.isAlive(c)));

		System.out.println(result.toString());
	}

	private void runSimulation() {

		for (int stepCount = 0; stepCount <= steps; ++stepCount) {

			if (stepCount != 0)
				iterateSimulationOneStep();

			int loopLength = detectLoop();

			printStep(stepCount, loopLength);

			long computationTime = System.currentTimeMillis()
					- computationTimeStart;

			if (!quietMode && stepDelay - computationTime >= 0) {
				try {
					Thread.sleep(stepDelay - computationTime);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}

			computationTimeStart = System.currentTimeMillis();

			if (loopLength != NO_LOOP)
				break;
		}
	}

	private void printStep(int stepCount, int loopLength) {

		String linePrefix = "";

		for (int i = 0; i < world.widthOffset; i++) {
			linePrefix += '-';
		}

		String lineSuffix = "";

		int worldWidth = world.isEmpty() ? 0 : world.width();
		for (int i = 0; i < width - worldWidth - world.widthOffset; i++) {
			lineSuffix += '-';
		}

		int printHeight = 0;

		if (!quietMode || stepCount == steps || loopLength != NO_LOOP) {
			for (int i = 0; i < Math.min(world.heightOffset, height); i++) {
				String line = "";
				while (line.length() < width) {
					line += '-';
				}
				printWorldLine(line);
				printHeight++;
			}

			for (int i = Math.max(0, -world.heightOffset); i < world.height(); i++) {

				if (printHeight == height)
					break;
				String line = world.get(i);

				line = linePrefix + line + lineSuffix;

				if (world.widthOffset < 0)
					line = line.substring(-world.widthOffset);

				if (line.length() > width)
					line = line.substring(0, width);
				printWorldLine(line);
				printHeight++;
			}

			for (; printHeight < height; printHeight++) {
				String line = "";
				while (line.length() < width) {
					line += '-';
				}
				printWorldLine(line);
			}

			if (stepCount == 0) {
				System.out.println("start");
			} else {
				String loopText = loopLength == NO_LOOP ? ""
						: " - loop of length " + loopLength + " detected";

				line("step " + stepCount + loopText);
			}
			System.out.println();
		}
	}

	private int detectLoop() {
		History itemToFind = new History(world.list, world.heightOffset, world.widthOffset);
		int index = history.indexOf(itemToFind);
		return (index != -1) ? index + 1 : NO_LOOP;
	}

	private void iterateSimulationOneStep() {
		world.addMarginsToWorld();

		int newHeightOffset = world.heightOffset;
		int newWidthtOffset = world.widthOffset;

		List<String> newWorld = nextWorld();

		world.stripMarginsFromWorld();

		history.add(0, new History(world.list, world.heightOffset, world.widthOffset));
		if (history.size() == historyLength + 1)
			history.remove(historyLength);

		world = new World(newWorld);
		world.heightOffset = newHeightOffset;
		world.widthOffset = newWidthtOffset;

		world.stripMarginsFromWorld();
	}

	private List<String> nextWorld() {
		List<String> newWorld = new ArrayList<>();

		for (int h = 0; h < world.height(); h++) {
			String line = "";
			for (int w = 0; w < world.get(0).length(); w++) {

				int n = 0;
				n += aliveCellsAt(w - 1, h - 1);
				n += aliveCellsAt(w, h - 1);
				n += aliveCellsAt(w + 1, h - 1);
				n += aliveCellsAt(w - 1, h);
				n += aliveCellsAt(w + 1, h);
				n += aliveCellsAt(w - 1, h + 1);
				n += aliveCellsAt(w, h + 1);
				n += aliveCellsAt(w + 1, h + 1);

				boolean willLive = n == 3 || (n == 2 && world.isAlive(w, h));

				line += willLive ? '#' : '-';
			}
			newWorld.add(line);
		}
		return newWorld;
	}

	private int aliveCellsAt(int x, int y) {
		return world.isAlive(x, y) ? 1 : 0;
	}
}
