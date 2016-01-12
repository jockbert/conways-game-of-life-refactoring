package gol.world;

import static gol.Cell.cell;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Consumer;

import gol.Cell;

public class FileWorldSource implements WorldSource {

	private String filePath;

	public FileWorldSource(String filePath) {
		this.filePath = filePath;
	}

	private class LineScope {
		int lineNumber = 0;
		int maxWidth = 0;

		void incrementLine(String line) {
			maxWidth = Math.max(maxWidth, line.length());
			lineNumber++;
		}
	}

	@Override
	public WorldSourceResult generate() {

		LineScope rs = new LineScope();
		Set<Cell> aliveCells = new HashSet<>();

		readWorldFile(line -> {
			rs.incrementLine(line);
			addAliveCellsToSet(aliveCells, rs.lineNumber - 1, line);
			ensureValidCharacters(rs.lineNumber, line);
		});

		World world = new AliveCellsWorld(aliveCells);
		return WorldSource.result(world, rs.maxWidth, rs.lineNumber);
	}

	private void readWorldFile(Consumer<String> lineConsumer) {
		Scanner scanner = openScanner();
		while (scanner.hasNextLine())
			lineConsumer.accept(scanner.nextLine());

		scanner.close();
	}

	private Scanner openScanner() {
		try {
			return new Scanner(new File(filePath));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private void ensureValidCharacters(int lineNumber, String line) {
		for (int i = 0; i < line.length(); ++i) {
			char c = line.charAt(i);
			if (c != '#' && c != '-') {
				String format = "Invalid character '%s' on line %s in file %s";
				String message = String.format(format, c, lineNumber, filePath);
				throw new RuntimeException(message);
			}
		}
	}

	private void addAliveCellsToSet(Set<Cell> result, int y, String line) {
		int width = line.length();
		for (int x = 0; x < width; ++x) {
			if (line.charAt(x) == '#')
				result.add(cell(x, y));
		}
	}
}
