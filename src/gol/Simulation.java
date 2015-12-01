package gol;

import gol.output.DefaultHashDashFormat;
import gol.output.OutputFormat;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Simulation {

	private static final int NO_LOOP = 0;

	int steps = -1;
	World world = null;
	int height = -1;
	int width = -1;
	int historyLength;
	boolean quietMode = false;
	OutputFormat outputFormat = new DefaultHashDashFormat();
	PeriodicBlocker periodicBlocker = PeriodicBlocker.DefaultNoPeriod();

	private List<Set<Cell>> history = new LinkedList<>();

	void runSimulation() {

		for (int stepCount = 0; stepCount <= steps; ++stepCount) {

			if (stepCount != 0)
				iterateSimulationOneStep();

			int loopLength = detectLoop();

			printStep(stepCount, loopLength);

			periodicBlocker.blockRestOfPeriodAndRestart();

			if (loopLength != NO_LOOP)
				break;
		}
	}

	private void printStep(int stepCount, int loopLength) {
		if (!quietMode || stepCount == steps || loopLength != NO_LOOP) {
			for (int y = 0; y < height; ++y) {
				String line = "";
				for (int x = 0; x < width; ++x) {
					line += outputFormat.cell(world.isAlive(x, y));
				}
				System.out.println(line);
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
		Set<Cell> itemToFind = world.getAliveCells();
		int index = history.indexOf(itemToFind);
		return (index != -1) ? index + 1 : NO_LOOP;
	}

	private void iterateSimulationOneStep() {
		World oldWorld = world;
		world = world.nextWorld();

		history.add(0, oldWorld.getAliveCells());
		if (history.size() == historyLength + 1)
			history.remove(historyLength);
	}
}
