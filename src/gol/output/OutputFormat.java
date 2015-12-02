package gol.output;

public interface OutputFormat {
	String cell(boolean isAlive);

	static OutputFormat defaultHashDash() {
		return new OutputFormat() {
			@Override
			public String cell(boolean isAlive) {
				return isAlive? "#": "-";
			}
		};
	}

	static OutputFormat spacedAt() {
		return new OutputFormat() {
			@Override
			public String cell(boolean isAlive) {
				return isAlive? "@ ": ". ";
			}
		};
	}

	static OutputFormat bigO() {
		return new OutputFormat() {
			@Override
			public String cell(boolean isAlive) {
				return isAlive? "O" : "-";
			}
		};
	}
}
