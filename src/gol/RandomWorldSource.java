package gol;

import static gol.Cell.cell;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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
		Set<Cell> aliveCells = new HashSet<>();

		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				if(rand.nextBoolean())
					aliveCells.add(cell(x,y));

		return new AliveCellsWorld(aliveCells);
	}
}
