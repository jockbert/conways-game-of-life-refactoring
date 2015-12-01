package gol;

import java.util.ArrayList;
import java.util.List;
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
		
		
		return new WorldSourceResult() {
			
			@Override
			public World world() {
				return createRandomWorld();
			}
			
			@Override
			public int width() {
				return width;
			}
			
			@Override
			public int height() {
				return height;
			}
		};
	}
	
	private World createRandomWorld() {
		List<String> lines = new ArrayList<>();

		for (int h = 0; h < height; h++) {
			String line = "";
			for (int w = 0; w < width; w++) 
				line += rand.nextBoolean() ? '#' : '-';
			
			lines.add(line);
		}

		return new AliveCellsWorld(lines);
	}
}
