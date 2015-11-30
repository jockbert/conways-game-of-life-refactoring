package gol;

import java.util.Set;

public interface World {

	public abstract boolean isAliveAbsolute(int x, int y);

	public abstract Set<Cell> getAliveCells();

	public abstract int height();

	public abstract int width();

	public abstract World nextWorld();

}