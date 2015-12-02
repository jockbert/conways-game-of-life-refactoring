package gol;

import gol.output.OutputFormat;

import java.util.OptionalInt;

public interface StepPrinter {
	void printStep(World world, int stepCount, OptionalInt loop);
	
	static StepPrinter fixedViewPort(int width, int height, OutputFormat cellFormat) {
		return new StepPrinter() {

			@Override
			public void printStep(World world,int stepCount, OptionalInt loop) {
				for (int y = 0; y < height; ++y) {
					String line = "";
					for (int x = 0; x < width; ++x) 
						line += cellFormat.cell(world.isAlive(x, y));
					System.out.println(line);
				}

				System.out.println(getTitle(stepCount, loop));
				System.out.println();
			}

			private String getTitle(int stepCount, OptionalInt loop) {
				if (stepCount == 0)
					return "start";
				else if (loop.isPresent())
					return String.format("step %s - loop of length %s detected",
							stepCount, loop.getAsInt());
				else
					return "step " + stepCount;
			}
		};
	}
}
