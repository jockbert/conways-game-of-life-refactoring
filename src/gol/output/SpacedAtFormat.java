package gol.output;

public class SpacedAtFormat implements OutputFormat {

	@Override
	public String cell(boolean isAlive) {
		return isAlive? "@ ": ". ";
	}
}
