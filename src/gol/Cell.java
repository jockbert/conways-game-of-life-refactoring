package gol;

public final class Cell {
	public final int x;
	public final int y;
	
	public static final Cell cell(int x, int y) {
		return new Cell(x,y);
	}

	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Cell add(Cell other) {
		return new Cell(x+other.x, y+other.y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
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
