package gol.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.function.Consumer;

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
		World world = World.create();

		readWorldFile(line -> {
			rs.incrementLine(line);
			addAliveCells(world, rs.lineNumber - 1, line);
			ensureValidCharacters(rs.lineNumber, line);
		});

		return WorldSource.result(world, rs.maxWidth, rs.lineNumber);
	}

	private void readWorldFile(Consumer<String> lineConsumer) {
		Scanner scanner = openScanner();
		while (scanner.hasNextLine())
			lineConsumer.accept(scanner.nextLine());

		scanner.close();
	}

	private void doThrow(String msg) {
		throw new RuntimeException(msg);
	}

	private Scanner openScanner() {

		File source = new File(filePath);
		if (!source.exists())
			doThrow(filePath + " (No such file or directory)");
		else if (source.isDirectory())
			doThrow(filePath + " (Is a directory)");

		try {
			return new Scanner(source);
		} catch (FileNotFoundException e) {
			doThrow(e.getMessage());
		}
		return null;
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

	private void addAliveCells(World world, int y, String line) {
		int width = line.length();
		for (int x = 0; x < width; ++x) {
			if (line.charAt(x) == '#')
				world.setAlive(x, y);
		}
	}
}
