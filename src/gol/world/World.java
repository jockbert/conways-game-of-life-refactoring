package gol.world;

import java.util.Set;

import gol.Cell;

public interface World {

	public abstract boolean isAlive(int x, int y);

	public abstract Set<Cell> getAliveCells();

	public abstract World nextWorld();

}