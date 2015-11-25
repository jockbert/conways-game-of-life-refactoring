package gol.output;

public class DefaultHashDashFormat implements OutputFormat {

	@Override
	public String cell(boolean isAlive) {
		return isAlive? "#": "-";
	}
}
