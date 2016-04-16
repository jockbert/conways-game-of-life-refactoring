package gol.world;

import gol.Cell;

import java.util.Iterator;

public interface World {

	public abstract boolean isAlive(int x, int y);

	public abstract void setAlive(int x, int y);

	public abstract World nextWorld();

	public abstract Cell firstAlive();

	public abstract Cell nextAlive(Cell exclusiveStart);

	public static World create() {
		return withHashAndEquals(new BitSetWorld());
	}

	public static Iterable<Cell> allAlive(World world) {
		return new AliveIterable(world);
	}

	public static class AliveIterable implements Iterable<Cell> {

		private World world;

		public AliveIterable(World world) {
			this.world = world;
		}

		@Override
		public Iterator<Cell> iterator() {
			return new Iterator<Cell>() {

				Cell next = world.firstAlive();

				@Override
				public boolean hasNext() {
					return next != null;
				}

				@Override
				public Cell next() {
					Cell result = next;
					next = world.nextAlive(next);
					return result;
				}
			};
		}
	}

	public static World withHashAndEquals(World world) {
		return new World() {

			Integer hc = null;

			@Override
			public boolean isAlive(int x, int y) {
				return world.isAlive(x, y);
			}

			@Override
			public void setAlive(int x, int y) {
				world.setAlive(x, y);
				hc = null;
			}

			@Override
			public int hashCode() {
				if (hc == null) {
					hc = 0;
					for (Cell c : allAlive(world)) {
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

				for (Cell c : allAlive(this)) {
					if (!otherWorld.isAlive(c.x, c.y))
						return false;
				}

				for (Cell c : allAlive(otherWorld)) {
					if (!this.isAlive(c.x, c.y))
						return false;
				}
				return true;
			}

			@Override
			public String toString() {
				Cell min = Cell.MAX;
				Cell max = Cell.MIN;

				Cell alive = firstAlive();

				while (alive != null) {
					min = min.min(alive);
					max = max.max(alive);
					alive = nextAlive(alive);
				}
				String result = "min=" + min + "\n";

				for (int y = min.y; y <= max.y; y++) {
					for (int x = min.x; x <= max.x; x++)
						result += isAlive(x, y) ? '#' : '-';
					result += '\n';
				}

				return result;
			}

			@Override
			public World nextWorld() {
				return withHashAndEquals(world.nextWorld());
			}

			@Override
			public Cell firstAlive() {
				return world.firstAlive();
			}

			@Override
			public Cell nextAlive(Cell exclusiveStart) {
				return world.nextAlive(exclusiveStart);
			}
		};
	}
}