package gol.world;

import gol.Cell;

import java.util.Iterator;

public interface World extends Iterable<Cell> {

	public abstract boolean isAlive(int x, int y);

	public abstract boolean isAlive(Cell cell);

	public abstract void setAlive(int x, int y);

	public abstract void setAlive(Cell cell);

	public static World create() {
		return withHashAndEquals(new BitSetWorld());
	}

	public static World withHashAndEquals(World world) {
		return new World() {

			Integer hc = null;

			@Override
			public Iterator<Cell> iterator() {
				return world.iterator();
			}

			@Override
			public boolean isAlive(int x, int y) {
				return world.isAlive(x, y);
			}

			@Override
			public boolean isAlive(Cell cell) {
				return world.isAlive(cell);
			}

			@Override
			public void setAlive(int x, int y) {
				world.setAlive(x, y);
				hc = null;
			}

			@Override
			public void setAlive(Cell cell) {
				world.setAlive(cell);
				hc = null;
			}

			@Override
			public int hashCode() {
				if (hc == null) {
					hc = 0;
					for (Cell c : world) {
						hc += c.hashCode();
					}
				}

				return hc;
			}

			@Override
			public boolean equals(Object other) {
				if (this == other)
					return true;
				if (other == null)
					return false;
				if (this.hashCode() != other.hashCode())
					return false;
				if (!(other instanceof World))
					return false;

				World otherWorld = (World) other;

				for (Cell c : this) {
					if (!otherWorld.isAlive(c))
						return false;
				}

				for (Cell c : otherWorld) {
					if (!this.isAlive(c))
						return false;
				}
				return true;
			}
		};
	}
}