package gol.world;

import java.util.Random;

public interface WorldGenerator {

	World generate(int width, int height);
	
	static WorldGenerator randomGenerator() {
		return new RandomWorldGenerator();
	}
	
	static class RandomWorldGenerator implements WorldGenerator {

		private Random rand = new Random();

		@Override
		public World generate(int width, int height) {
			World world = World.create();

			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++)
					if (rand.nextBoolean())
						world.setAlive(x, y);

			return world;
		}	
	}
}
