package gol;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LineWorld implements World {

	private List<String> list;

	private int heightOffset = 0;
	private int widthOffset = 0;

	public LineWorld() {
		this.list = new ArrayList<String>();
	}

	public LineWorld(List<String> list) {
		this.list = list;
	}

	@Override
	public boolean isAlive(int x, int y) {
		return isAliveRelativeOffset(x - widthOffset, y - heightOffset);
	}

	@Override
	public Set<Cell> getAliveCells() {
		Set<Cell> result = new HashSet<Cell>();

		for(int y = 0; y < height(); ++y) {
			String line = list.get(y);
			for(int x = 0; x < width(); ++x) {
				if (isAlive(line.charAt(x)))
					result.add(new Cell(x+widthOffset,y+heightOffset));
			}
		}

		return result;
	}

	private boolean isAliveRelativeOffset(int x, int y) {
		if (x < 0 || y < 0 || y >= list.size())
			return false;

		String line = list.get(y);

		if (x >= line.length())
			return false;

		char c = line.charAt(x);

		return isAlive(c);
	}

	private boolean isAlive(char cell) {
		return cell == '#';
	}

	private int height() {
		return list.size();
	}

	private int width() {
		return list.isEmpty() ? 0 : list.get(0).length();
	}

	private boolean isEmpty() {
		return list.isEmpty();
	}

	private String emptyLine() {
		if (isEmpty())
			return "";
		String result = "";
		while (result.length() < list.get(0).length())
			result += '-';
		return result;
	}

	private void addMarginsToWorld() {
		list.add(emptyLine());
		list.add(0, emptyLine());

		for (int i = 0; i < height(); i++) {
			String line = list.get(i);
			list.set(i, '-' + line + '-');
		}

		heightOffset--;
		widthOffset--;
	}

	private boolean isColumnEmpty(int column) {

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).charAt(column) == '#')
				return false;
		}
		return true;
	}

	private void stripMarginsFromWorld() {
		while (!isEmpty() && list.get(0).equals(emptyLine())) {
			list.remove(0);
			heightOffset++;
		}
		while (!isEmpty() && list.get(height() - 1).equals(emptyLine())) {
			list.remove(height() - 1);
		}

		while (!isEmpty() && list.get(0).length() != 0 && isColumnEmpty(0)) {
			for (int i = 0; i < height(); i++) {
				String line = list.get(i);
				list.set(i, line.substring(1));
			}
			widthOffset++;
		}

		while (!isEmpty() && width() != 0 && isColumnEmpty(width() - 1)) {
			for (int i = 0; i < height(); i++) {
				String line = list.get(i);
				list.set(i, line.substring(0, list.get(i).length() - 1));
			}
		}
	}

	@Override
	public World nextWorld() {
		addMarginsToWorld();
		List<String> list = new ArrayList<>();

		for (int h = 0; h < height(); h++) {
			String line = "";
			for (int w = 0; w < width(); w++) {

				int n = 0;
				n += aliveCellsAt(w - 1, h - 1);
				n += aliveCellsAt(w, h - 1);
				n += aliveCellsAt(w + 1, h - 1);
				n += aliveCellsAt(w - 1, h);
				n += aliveCellsAt(w + 1, h);
				n += aliveCellsAt(w - 1, h + 1);
				n += aliveCellsAt(w, h + 1);
				n += aliveCellsAt(w + 1, h + 1);

				boolean willLive = n == 3 || (n == 2 && isAliveRelativeOffset(w, h));

				line += willLive ? '#' : '-';
			}
			list.add(line);
		}
		LineWorld newWorld = new LineWorld(list);
		newWorld.heightOffset = heightOffset;
		newWorld.widthOffset = widthOffset;
		newWorld.stripMarginsFromWorld();
		this.stripMarginsFromWorld();
		return newWorld;
	}

	private int aliveCellsAt(int x, int y) {
		return isAliveRelativeOffset(x, y) ? 1 : 0;
	}
}
