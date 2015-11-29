package gol;

import java.util.ArrayList;
import java.util.List;

public class World {

	List<String> list;

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
		list.add(0,emptyLine());

		for (int i = 0; i < height(); i++) {
			String line = list.get(i);
			list.set(i, '-' + line + '-');
		}
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
}
