package gol;

import gol.output.DefaultHashDashFormat;
import gol.output.OutputFormat;

import java.util.LinkedList;
import java.util.List;

public class Simulation {

	private static final int NO_LOOP = 0;

	int steps = -1;
	World world = null;
	int height = -1;
	int width = -1;
	long computationTimeStart;
	int historyLength;
	int stepDelay = -1;
	boolean quietMode = false;
	OutputFormat outputFormat = new DefaultHashDashFormat();

	private List<History> history = new LinkedList<History>();

	private void printWorldLine(String line) {
		StringBuilder result = new StringBuilder();

		for (char c : line.toCharArray())
			result.append(outputFormat.cell(world.isAlive(c)));

		System.out.println(result.toString());
	}

	void runSimulation() {

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
		if (!quietMode || stepCount == steps || loopLength != NO_LOOP) {
			for (int y = 0; y < height; ++y) {
				String line = "";
				for (int x = 0; x < width; ++x) {
					line += world.isAliveAbsolute(x, y) ? '#' : '-';
				}
				printWorldLine(line);
			}

			if (stepCount == 0) {
				System.out.println("start");
			} else {
				String loopText = loopLength == NO_LOOP ? ""
						: " - loop of length " + loopLength + " detected";

				System.out.println("step " + stepCount + loopText);
			}
			System.out.println();
		}
	}

	private int detectLoop() {
		History itemToFind = new History(world.list, world.heightOffset,
				world.widthOffset);
		int index = history.indexOf(itemToFind);
		return (index != -1) ? index + 1 : NO_LOOP;
	}

	private void iterateSimulationOneStep() {
		World oldWorld = world;
		world = world.nextWorld();

		history.add(0, new History(oldWorld.list, oldWorld.heightOffset,
				oldWorld.widthOffset));
		if (history.size() == historyLength + 1)
			history.remove(historyLength);
	}
}
