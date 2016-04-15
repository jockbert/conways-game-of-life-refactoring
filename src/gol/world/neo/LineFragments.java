package gol.world.neo;

public class LineFragments implements Fragments {

	private int fragSize;
	private Line line;

	public LineFragments(int fragSize, Line line) {
		this.fragSize = fragSize;
		this.line = line;
	}

	@Override
	public int minIndex() {
		if (line.isEmpty())
			return Integer.MAX_VALUE;

		int minSetBit = line.minSetBit();
		int minMainFrag = minSetBit / fragSize;

		boolean hasOverlapToNextFrag = minSetBit % fragSize == 0;

		return minMainFrag + (hasOverlapToNextFrag ? -1 : 0);
	}

	@Override
	public int maxIndex() {
		if (line.isEmpty())
			return Integer.MIN_VALUE;

		int maxSetBit = line.maxSetBit();
		int maxMainFrag = maxSetBit / fragSize;

		boolean hasOverlapToNextFrag = maxSetBit % fragSize == fragSize - 1;

		return maxMainFrag + (hasOverlapToNextFrag ? 1 : 0);
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
