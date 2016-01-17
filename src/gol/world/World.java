package gol.world;

import gol.Cell;

import java.util.Iterator;
import java.util.Set;

public interface World extends Iterable<Cell> {

	public abstract boolean isAlive(int x, int y);

	public abstract boolean isAlive(Cell cell);

	public abstract void setAlive(int x, int y);

	public abstract void setAlive(Cell cell);

	public abstract Set<Cell> getAliveCells();

	public static World create() {
		return new CoordBoolMapWorld();
	}
}