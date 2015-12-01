package gol;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileWorldSource implements WorldSource {

	private String filePath;

	public FileWorldSource(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public WorldSourceResult generate() {
		final List<String> lines = readWorldFile();
		return new WorldSourceResult() {
			@Override
			public World world() {
				return new AliveCellsWorld(lines);
			}

			@Override
			public int width() {
				return lines.isEmpty() ? 0 : lines.get(0).length();
			}

			@Override
			public int height() {
				return lines.size();
			}
		};
	}

	private ArrayList<String> readWorldFile() {
		Scanner scanner;
		try {
			File file = new File(filePath);
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e.getMessage());
		}

		ArrayList<String> lines = new ArrayList<String>();
		int maxWidth = 0;
		for (int lineNumber = 1; scanner.hasNextLine(); lineNumber++) {
			String line = scanner.nextLine();
			Pattern pattern = Pattern.compile("[^#-]");
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				scanner.close();
				throw new RuntimeException("Invalid character '"
						+ matcher.group() + "' on line " + lineNumber
						+ " in file " + filePath);
			}

			maxWidth = Math.max(maxWidth, line.length());

			lines.add(line);
		}

		ensureThatAllLinesHasTheSameWidth(lines, maxWidth);

		scanner.close();
		return lines;
	}

	private void ensureThatAllLinesHasTheSameWidth(ArrayList<String> lines,
			int maxWidth) {
		for (int i = 0; i < lines.size(); ++i) {
			String line = lines.get(i);

			while (line.length() < maxWidth)
				line += '-';

			lines.set(i, line);
		}
	}
}
