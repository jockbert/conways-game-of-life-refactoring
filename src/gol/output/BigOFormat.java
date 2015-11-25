package gol.output;

public class BigOFormat implements OutputFormat {

	@Override
	public String cell(boolean isAlive) {
		return isAlive? "O" : "-";
	}
}
