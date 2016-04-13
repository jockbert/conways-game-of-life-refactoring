package gol.world.neo;

public class LineFragmenter implements Fragmenter {

	private int fragSize;
	private Line line;

	public LineFragmenter(int fragSize, Line line) {
		this.fragSize = fragSize;
		this.line = line;
	}

	@Override
	public int min() {
		if (line.isEmpty())
			return Integer.MAX_VALUE;

		return line.minSetBit() / fragSize - 1;
	}

	@Override
	public int max() {
		if (line.isEmpty())
			return Integer.MIN_VALUE;

		return line.minSetBit() / fragSize + 1;
	}

	@Override
	public int get(int index) {
		int length = fragSize + 2;
		int startIndex = index * fragSize - 1;
		int result = 0;

		for (int i = 0; i < length; i++) {
			result <<= 1;
			result += line.isSet(startIndex + i) ? 1 : 0;
		}

		return result;
	}

	@Override
	public int fragSize() {
		return fragSize;
	}

}
