package gol.world.neo;

public interface Line {

	void set(int i);

	boolean isSet(int i);

	int minSetBit();

	int maxSetBit();

	boolean isEmpty();

	Integer nextAliveInclusive(int fromX);

	public static final class HashEqualsLine implements Line {

		private Line line;

		HashEqualsLine(Line line) {
			this.line = line;
		}

		@Override
		public void set(int i) {
			line.set(i);
		}

		@Override
		public boolean isSet(int i) {
			return line.isSet(i);
		}

		@Override
		public int minSetBit() {
			return line.minSetBit();
		}

		@Override
		public int maxSetBit() {
			return line.maxSetBit();
		}

		@Override
		public boolean isEmpty() {
			return line.isEmpty();
		}

		@Override
		public Integer nextAliveInclusive(int fromX) {
			return line.nextAliveInclusive(fromX);
		}

		@Override
		public int hashCode() {

			final int prime = 31;
			int result = 1;

			Integer index = minSetBit();
			while (index != null) {
				result = prime * result + index;
				index = nextAliveInclusive(index + 1);
			}

			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof Line))
				return false;

			Line other = (Line) obj;

			if (isEmpty())
				return other.isEmpty();
			if (minSetBit() == other.minSetBit()
					&& maxSetBit() == other.maxSetBit()) {
				Integer x = minSetBit();

				while (true) {
					Integer next = nextAliveInclusive(x + 1);
					Integer otherNext = other.nextAliveInclusive(x + 1);
					if (next == null)
						return otherNext == null;

					if (!next.equals(otherNext))
						return false;

					x = next;
				}
			}

			return false;
		}

		@Override
		public String toString() {

			String at = isEmpty() ? "" : "@" + minSetBit();
			String bits = "";

			for (int i = minSetBit(); i <= maxSetBit(); i++)
				bits += isSet(i) ? '1' : '0';

			return "Line" + at + "{" + bits + "}";
		}

	}
}
