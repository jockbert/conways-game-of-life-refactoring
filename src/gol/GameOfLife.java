package gol;

import java.io.File;
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

	static class History {
		List<String> world;
		int heightOffset;
		int widthOffset;

		History(List<String> world, int heightOffset, int widthOffset) {
			this.world = world;
			this.heightOffset = heightOffset;
			this.widthOffset = widthOffset;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			History other = (History) obj;
			if (heightOffset != other.heightOffset)
				return false;
			if (widthOffset != other.widthOffset)
				return false;
			if (world == null) {
				if (other.world != null)
					return false;
			} else if (!world.equals(other.world))
				return false;
			return true;
		}
	}

	int steps = -1;
	List<String> world = null;
	int height = -1;
	int width = -1;
	int heightOffset = 0;
	private List<History> history = new LinkedList<History>();
	private int widthOffset = 0;
	private static long computationTimeStart;
	static int stepCount = 0;
	static String dorun = "yes";
	private static int historyLength;
	private static int stepDelay = -1;
	private static boolean quietMode = false;

	public static void main(String[] args) {
		GameOfLife game = new GameOfLife();

		dorun = "yes";

		computationTimeStart = System.currentTimeMillis();

		try {
			List<String> argList = new LinkedList<String>(Arrays.asList(args));
			while (argList.size() > 0) {
				String arg = argList.get(0);
				argList.remove(0);
				if ("-s".equals(arg)) {
					game.steps = getIntArg(argList);
				} else if ("-f".equals(arg)) {
					String filePath = getArg(argList);
					File file = new File(filePath);
					@SuppressWarnings("resource")
					Scanner scanner = new Scanner(file);
					ArrayList<String> world = new ArrayList<String>();
					game.world = world;
					int lineNumber = 1;
					int maxWidth = 0;
					while (scanner.hasNextLine()) {
						String line = scanner.nextLine();
						Pattern pattern = Pattern.compile("[^#-]");
						Matcher matcher = pattern.matcher(line);
						if (matcher.find()) {
							scanner.close();
							throw new RuntimeException("Invalid character '"
									+ matcher.group() + "' on line "
									+ lineNumber + " in file " + filePath);
						}

						maxWidth = Math.max(maxWidth, line.length());

						world.add(line);
						game.world = world;
						lineNumber++;
					}

					for (int i = 0; i < world.size(); ++i) {
						String line = world.get(i);

						while (line.length() < maxWidth)
							line += '-';

						world.set(i, line);
					}

					if (game.height == -1)
						game.height = world.size();
					if (game.width == -1)
						game.width = world.isEmpty() ? 0 : world.get(0)
								.length();
				} else if ("-?".equals(arg)) {
					throw new Exception("Help requested");
				} else if ("-@".equals(arg)) {
					GameOfLifeSpecialOutputFormat newGame = new GameOfLifeSpecialOutputFormat();
					newGame.isAtSigns = true;
					newGame.world = game.world;
					newGame.height = game.height;
					newGame.width = game.width;
					newGame.steps = game.steps;
					game = newGame;
				} else if ("-O".equals(arg)) {
					GameOfLifeSpecialOutputFormat newGame = new GameOfLifeSpecialOutputFormat();
					newGame.isAtSigns = false;
					newGame.world = game.world;
					newGame.height = game.height;
					newGame.width = game.width;
					newGame.steps = game.steps;
					game = newGame;
				} else if (arg.equals("-w")) {
					game.width = getIntArg(argList);
				} else if ("-h".equals(arg))
					game.height = getIntArg(argList);
				else if ("-l".equals(arg))
					historyLength = getIntArg(argList);
				else if ("-t".equals(arg))
					stepDelay = getIntArg(argList);
				else if ("-q".equals(arg)) {
					quietMode = true;

				} else
					throw new Exception("Unknown argument " + arg);
			}

			if (game.world == null) {
				game.world = new ArrayList<String>();

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

		} catch (Exception e) {
			line(e.getMessage());

			dorun = "no";
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

		if (dorun.equals("yes"))
			game.runSimulation();
	}

	void printWorldLine(String line) {
		System.out.println(line);
	}

	public boolean isAlive(int x, int y) {
		char c = world.get(y).charAt(x);

		if (c == '#')
			return true;
		else
			return false;
	}

	String emptyLine() {
		if (world.isEmpty())
			return "";
		String result = "";
		while (result.length() < world.get(0).length())
			result += '-';
		return result;
	}

	void addMarginsToWorld() {
		world.add(emptyLine());
		world.add(0, emptyLine());
		heightOffset--;

		for (int i = 0; i < world.size(); i++) {
			String line = world.get(i);
			world.set(i, '-' + line + '-');
		}
		widthOffset--;
	}

	boolean isColumnEmpty(int column) {

		for (int i = 0; i < world.size(); i++) {
			if (world.get(i).charAt(column) == '#')
				return false;
		}
		return true;
	}

	void stripMarginsFromWorld() {
		while (!world.isEmpty() && world.get(0).equals(emptyLine())) {
			world.remove(0);
			heightOffset++;
		}
		while (!world.isEmpty()
				&& world.get(world.size() - 1).equals(emptyLine())) {
			world.remove(world.size() - 1);
		}

		while (!world.isEmpty() && world.get(0).length() != 0
				&& isColumnEmpty(0)) {
			for (int i = 0; i < world.size(); i++) {
				String line = world.get(i);
				world.set(i, line.substring(1));
			}
			widthOffset++;
		}

		while (!world.isEmpty() && world.get(0).length() != 0
				&& isColumnEmpty(world.get(0).length() - 1)) {
			for (int i = 0; i < world.size(); i++) {
				String line = world.get(i);
				world.set(i, line.substring(0, world.get(i).length() - 1));
			}
		}
	}

	private void runSimulation() {

		while (stepCount <= steps) {

			if (stepCount != 0)
				iterateSimulationOneStep();

			int loopLength = detectLoop();

			String linePrefix = "";

			for (int i = 0; i < widthOffset; i++) {
				linePrefix += '-';
			}

			String lineSuffix = "";

			int worldWidth = world.isEmpty() ? 0 : world.get(0).length();
			for (int i = 0; i < width - worldWidth - widthOffset; i++) {
				lineSuffix += '-';
			}

			int printHeight = 0;

			if (!quietMode || stepCount == steps || loopLength != NO_LOOP) {
				for (int i = 0; i < Math.min(heightOffset, height); i++) {
					String line = "";
					while (line.length() < width) {
						line += '-';
					}
					printWorldLine(line);
					printHeight++;
				}

				for (int i = Math.max(0, -heightOffset); i < world.size(); i++) {

					if (printHeight == height)
						break;
					String line = world.get(i);

					line = linePrefix + line + lineSuffix;

					if (widthOffset < 0)
						line = line.substring(-widthOffset);

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

			stepCount++;
		}
	}

	private int detectLoop() {
		int index = history.indexOf(new History(world, heightOffset,
				widthOffset));

		return (index != -1) ? index + 1 : NO_LOOP;
	}

	private void iterateSimulationOneStep() {
		addMarginsToWorld();

		int newHeightOffset = heightOffset;
		int newWidthtOffset = widthOffset;

		List<String> newWorld = nextWorld();

		stripMarginsFromWorld();

		history.add(0, new History(world, heightOffset, widthOffset));
		if (history.size() == historyLength + 1)
			history.remove(historyLength);

		world = newWorld;
		heightOffset = newHeightOffset;
		widthOffset = newWidthtOffset;

		stripMarginsFromWorld();
	}

	private List<String> nextWorld() {
		List<String> newWorld = new ArrayList<>();

		for (int h = 0; h < world.size(); h++) {
			String line = "";
			for (int w = 0; w < world.get(0).length(); w++) {
				/* count alive neighbors */
				int n = 0;

				if (h != 0 && w != 0 && isAlive(w - 1, h - 1))
					n++;
				if (h != 0 && isAlive(w, h - 1))
					n++;
				if (h != 0 && w != world.get(0).length() - 1
						&& isAlive(w + 1, h - 1))
					n++;

				if (w != 0 && isAlive(w - 1, h))
					n++;

				if (w != world.get(0).length() - 1 && isAlive(w + 1, h))
					n++;

				if (h != world.size() - 1 && w != 0 && isAlive(w - 1, h + 1))
					n++;
				if (h != world.size() - 1 && isAlive(w, h + 1))
					n++;
				if (h != world.size() - 1 && w != world.get(0).length() - 1
						&& isAlive(w + 1, h + 1))
					n++;

				char cell = '-';

				if (isAlive(w, h)) {
					if (n == 2 || n == 3)
						cell = '#';
				} else {
					if (n == 3)
						cell = '#';
				}

				line += cell;
			}
			newWorld.add(line);
		}
		return newWorld;
	}
}
