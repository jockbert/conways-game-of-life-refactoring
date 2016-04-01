package gol.world;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileReader {

	public WorldResult read(String filePath) {

		InputStream is = openStream(filePath);

		int y = 0;
		int x = 0;
		int maxWidth = 0;

		World world = World.create();

		try {
			for (int i = is.read(); i != -1; i = is.read()) {
				char c = (char) i;
				switch (c) {
				case '#':
					world.setAlive(x, y);
				case '-':
					x++;
					break;
				case '\n':
					maxWidth = Math.max(maxWidth, x);
					y++;
					x = 0;
				case '\r':
					break;

				default:
					throwInvalidCharacter((char) i, y + 1, filePath);
				}
			}
			maxWidth = Math.max(maxWidth, x);
			is.close();
		} catch (IOException e) {
			// silence
		}

		int height = y + (x != 0 ? 1 : 0);
		return WorldResult.result(world, maxWidth, height);
	}

	private InputStream openStream(String filePath) {
		File source = new File(filePath);
		if (!source.exists())
			doThrow(filePath + " (No such file or directory)");
		else if (source.isDirectory())
			doThrow(filePath + " (Is a directory)");
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(source));
		} catch (FileNotFoundException e) {
			doThrow(e.getMessage());
		}

		return is;
	}

	private void throwInvalidCharacter(char c, int lineNumber, String filePath) {
		String format = "Invalid character '%s' on line %s in file %s";
		String message = String.format(format, c, lineNumber, filePath);
		doThrow(message);
	}

	private void doThrow(String msg) {
		throw new RuntimeException(msg);
	}
}
