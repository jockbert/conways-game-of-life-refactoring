package gol;

public final class Cell {

	public static final Cell MIN = new Cell(Integer.MIN_VALUE,
			Integer.MIN_VALUE);
	public static final Cell MAX = new Cell(Integer.MAX_VALUE,
			Integer.MAX_VALUE);

	public final int x;
	public final int y;

	public static final Cell cell(int x, int y) {
		return new Cell(x, y);
	}

	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Cell add(Cell other) {
		return new Cell(x + other.x, y + other.y);
	}

	public Cell incX() {
		return new Cell(x + 1, y);
	}

	public Cell incY(int newX) {
		return new Cell(newX, y + 1);
	}

	public Cell min(Cell other) {
		return new Cell(Math.min(x, other.x), Math.min(y, other.y));
	}

	public Cell max(Cell other) {
		return new Cell(Math.max(x, other.x), Math.max(y, other.y));
	}

	@Override
	public int hashCode() {
		final int prime = 9973;
		return prime * x + y;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cell other = (Cell) obj;
		return x == other.x && y == other.y;
	}

	@Override
	public String toString() {
		return "Cell(" + x + "," + y + ")";
	}
}
