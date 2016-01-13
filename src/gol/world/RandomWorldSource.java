package gol.world;

import java.util.Random;

public class RandomWorldSource implements WorldSource {

	private int width;
	private int height;
	private Random rand = new Random();

	public RandomWorldSource(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public WorldSourceResult generate() {
		return WorldSource.result(createRandomWorld(), width, height);
	}

	private World createRandomWorld() {

		World world = World.create();

		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				if (rand.nextBoolean())
					world.setAlive(x, y);

		return world;
	}
}
