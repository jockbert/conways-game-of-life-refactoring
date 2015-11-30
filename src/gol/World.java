package gol;

import java.util.Set;

public interface World {

	public abstract boolean isAlive(int x, int y);

	public abstract Set<Cell> getAliveCells();

	public abstract World nextWorld();

}