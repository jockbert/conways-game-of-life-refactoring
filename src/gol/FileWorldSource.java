package gol;

import static gol.Cell.cell;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileWorldSource implements WorldSource {

	private String filePath;
	private Pattern pattern = Pattern.compile("[^#-]");

	public FileWorldSource(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public WorldSourceResult generate() {
		final List<String> lines = readWorldFile();
		ensureAllLinesHasValidCharacters(lines);
		Set<Cell> aliveCells = convertLinesToAliveCellSet(lines);

		return new WorldSourceResult() {
			@Override
			public World world() {
				return new AliveCellsWorld(aliveCells);
			}

			@Override
			public int width() {
				return maxWidth(lines);
			}

			@Override
			public int height() {
				return lines.size();
			}
		};
	}

	private List<String> readWorldFile() {
		Scanner scanner = openScanner();
		List<String> lines;
		try {
			lines = scanAllLines(scanner);
		} finally {
			scanner.close();
		}
		return lines;
	}

	private List<String> scanAllLines(Scanner scanner) {
		List<String> lines = new ArrayList<String>();
		while (scanner.hasNextLine())
			lines.add(scanner.nextLine());

		return lines;
	}

	private Scanner openScanner() {
		try {
			return new Scanner(new File(filePath));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private void ensureAllLinesHasValidCharacters(List<String> lines) {
		for (int i = 0; i < lines.size(); ++i)
			ensureValidCharacters(i+1, lines.get(i));
	}

	private void ensureValidCharacters(int lineNumber, String line) {
		Matcher matcher = pattern.matcher(line);
		if (matcher.find()) {
			String match = matcher.group();
			String format = "Invalid character '%s' on line %s in file %s";
			String message = String.format(format, match, lineNumber, filePath);
			throw new RuntimeException(message);
		}
	}

	private Set<Cell> convertLinesToAliveCellSet(List<String> lines) {
		Set<Cell> result = new HashSet<>();

		int height = lines.size();
		for (int y = 0; y < height; ++y) {
			String line = lines.get(y);
			int width = line.length();
			for (int x = 0; x < width; ++x) {
				if (line.charAt(x) == '#')
					result.add(cell(x, y));
			}
		}

		return result;
	}

	private int maxWidth(List<String> lines) {
		int maxWidth = 0;
		for (String line : lines)
			maxWidth = Math.max(maxWidth, line.length());
		return maxWidth;
	}
}
