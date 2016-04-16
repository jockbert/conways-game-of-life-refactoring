package gol.world.neo;

public final class LineFragments implements Fragments {

	private final int fragSize;
	private final Line line;

	public LineFragments(int fragSize, Line line) {
		this.fragSize = fragSize;
		this.line = line;
	}

	@Override
	public int minIndex() {
		if (line.isEmpty())
			return Integer.MAX_VALUE;

		int minSetBit = line.minSetBit();
		int minMainFrag = minSetBit / fragSize + (minSetBit < 0 ? -1 : 0);

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
	public Integer get(int index) {
		int startIndex = fragStartIndex(index);
		int result = 0;

		for (int i = 0; i < fragLength(); i++) {
			result <<= 1;
			result += line.isSet(startIndex + i) ? 1 : 0;
		}

		return result;
	}

	private int fragStartIndex(int index) {
		return index * fragSize - 1;
	}

	private final int fragLength() {
		return fragSize + 2;
	}

	private int reverse(int value) {
		int result = 0;

		for (int i = 0; i < fragLength(); i++) {
			result <<= 1;
			result |= value & 1;
			value >>= 1;
		}

		return result;
	}

	@Override
	public int fragSize() {
		return fragSize;
	}

	@Override
	public void set(int index, Integer value) {
		int startIndex = fragStartIndex(index);
		int eulav = reverse(value);

		for (int i = 0; i < fragLength(); i++) {
			if ((eulav & 1) != 0)
				line.set(startIndex + i);
			eulav >>= 1;
		}
	}

}
