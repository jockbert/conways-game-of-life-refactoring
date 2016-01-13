package gol.world;

import gol.Cell;

import java.util.HashSet;
import java.util.Set;

public interface World {

	public abstract boolean isAlive(int x, int y);

	public abstract void setAlive(int x, int y);

	public abstract void setAlive(Cell cell);

	public abstract Set<Cell> getAliveCells();

	public abstract World nextWorld();

	public static World create() {
		return new CoordBoolMapWorld(new HashSet<>());
	}
}