package gol;

import java.util.Random;

public class ZeroStepRandomSimulation implements Simulation {

	private int width;
	private int height;
	private OutputFormat format;

	ZeroStepRandomSimulation(int width, int height, OutputFormat format) {
		this.width = width;
		this.height = height;
		this.format = format;
	}

	@Override
	public void runSimulation() {
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
}
