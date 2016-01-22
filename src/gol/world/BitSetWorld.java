package gol.world;

import gol.Cell;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

public class BitSetWorld implements World {

	int yStart = 0;
	List<Line> lines = new ArrayList<>();

	@Override
	public Iterator<Cell> iterator() {

		return new Iterator<Cell>() {
			int nextXIndex = 0;
			int nextYIndex = 0;

			@Override
			public boolean hasNext() {
				if (nextYIndex >= lines.size())
					return false;

				Line line = lines.get(nextYIndex);

				if (nextXIndex == -1) {
					nextYIndex++;
					nextXIndex = 0;
					return hasNext();
				}

				if (line.bs.get(nextXIndex))
					return true;

				nextXIndex = line.bs.nextSetBit(nextXIndex);

				return hasNext();
			}

			@Override
			public Cell next() {
				if (!hasNext())
					throw new NoSuchElementException();

				Line line = lines.get(nextYIndex);
				int x = line.xStart + nextXIndex;
				int y = yStart + nextYIndex;
				nextXIndex++;

				return new Cell(x, y);
			}
		};
	}

	@Override
	public boolean isAlive(int x, int y) {
		if (yIndex(y) < 0)
			return false;
		if (yIndex(y) >= lines.size())
			return false;

		return lines.get(yIndex(y)).isSet(x);
	}

	private int yIndex(int y) {
		return y - yStart;
	}

	@Override
	public boolean isAlive(Cell cell) {
		return isAlive(cell.x, cell.y);
	}

	@Override
	public void setAlive(int x, int y) {
		while (yIndex(y) < 0)
			shiftDown();

		while (yIndex(y) >= lines.size())
			lines.add(new Line());

		lines.get(yIndex(y)).set(x);
	}

	private void shiftDown() {
		List<Line> newLines = new ArrayList<>();

		for (int i = 0; i < 10; i++)
			newLines.add(new Line());

		newLines.addAll(lines);
		lines = newLines;
		yStart -= 10;
	}

	@Override
	public void setAlive(Cell cell) {
		setAlive(cell.x, cell.y);
	}

	static class Line implements Iterable<Integer> {
		int xStart = 0;
		BitSet bs = new BitSet();

		void set(int x) {
			while (xIndex(x) < 0)
				shiftRight();

			bs.set(xIndex(x));
		}

		private int xIndex(int x) {
			return x - xStart;
		}

		boolean isSet(int x) {
			if (xIndex(x) < 0)
				return false;

			return bs.get(xIndex(x));
		}

		private void shiftRight() {
			long[] oldArr = bs.toLongArray();
			long[] newArr = new long[2 + oldArr.length];

			for (int i = 0; i < oldArr.length; i++)
				newArr[i + 2] = oldArr[i];

			bs = BitSet.valueOf(newArr);
			xStart -= 2 * 64;
		}

		@Override
		public Iterator<Integer> iterator() {
			return new Iterator<Integer>() {
				int min = 0;

				@Override
				public boolean hasNext() {
					return bs.nextSetBit(min) != -1;
				}

				@Override
				public Integer next() {
					int result = bs.nextSetBit(min);
					min = result + 1;
					return result + xStart;
				}
			};
		}
	}

	@Override
	public World nextWorld() {

		World nextWorld = new BitSetWorld();
		BookKeeper bk = new BookKeeper(yStart);

		for (int yIndex = 0; yIndex < lines.size(); yIndex++) {
			Line line = lines.get(yIndex);

			for (int x : line)
				bk.set(x);

			processLine(nextWorld, bk);
		}

		processLine(nextWorld, bk);
		processLine(nextWorld, bk);
		processLine(nextWorld, bk);

		return nextWorld;
	}

	private void processLine(World nextWorld, BookKeeper bk) {
		int y = bk.line1Y;
		for (Entry<Integer, Integer> entry : bk.count1.entrySet()) {
			int x = entry.getKey();
			int count = entry.getValue();

			boolean isAlive = count == 3 || (count == 2 && this.isAlive(x, y));

			if (isAlive)
				nextWorld.setAlive(x, y);
		}

		bk.shift();
	}

	private static class BookKeeper {
		Map<Integer, Integer> count1 = new HashMap<>();
		Map<Integer, Integer> count2 = new HashMap<>();
		Map<Integer, Integer> count3 = new HashMap<>();
		int line1Y;

		public BookKeeper(int yStart) {
			line1Y = yStart - 1;
		}

		void shift() {
			count1 = count2;
			count2 = count3;
			count3 = new HashMap<>();
			line1Y++;
		}

		private void inc(Map<Integer, Integer> count, int x) {
			count.put(x, count.getOrDefault(x, 0) + 1);
		}

		void set(int x) {
			inc(count1, x - 1);
			inc(count1, x);
			inc(count1, x + 1);

			inc(count2, x - 1);
			inc(count2, x + 1);

			inc(count3, x - 1);
			inc(count3, x);
			inc(count3, x + 1);
		}

	}
}
