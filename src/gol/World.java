package gol;

import java.util.ArrayList;
import java.util.List;

public class World {

	List<String> list;

	int heightOffset = 0;
	int widthOffset = 0;

	World() {
		this.list = new ArrayList<String>();
	}

	World(List<String> list) {
		this.list = list;
	}

	boolean isAlive(int x, int y) {
		if (x < 0 || y < 0 || y >= list.size())
			return false;

		String line = list.get(y);

		if (x >= line.length())
			return false;

		char c = line.charAt(x);

		return isAlive(c);
	}

	boolean isAlive(char cell) {
		return cell == '#';
	}

	int height() {
		return list.size();
	}

	int width() {
		return list.isEmpty() ? 0 : list.get(0).length();
	}

	boolean isEmpty() {
		return list.isEmpty();
	}

	void add(String line) {
		list.add(line);
	}

	String emptyLine() {
		if (isEmpty())
			return "";
		String result = "";
		while (result.length() < list.get(0).length())
			result += '-';
		return result;
	}

	void addMarginsToWorld() {
		list.add(emptyLine());
		list.add(0, emptyLine());

		for (int i = 0; i < height(); i++) {
			String line = list.get(i);
			list.set(i, '-' + line + '-');
		}

		heightOffset--;
		widthOffset--;
	}

	boolean isColumnEmpty(int column) {

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).charAt(column) == '#')
				return false;
		}
		return true;
	}

	String get(int index) {
		return list.get(index);
	}

	void stripMarginsFromWorld() {
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
}
