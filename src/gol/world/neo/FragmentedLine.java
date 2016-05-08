package gol.world.neo;

public class FragmentedLine implements Line {

	private Fragments fragments;
	private Integer smallestBitIndex = null;
	private Integer largestBitIndex = null;

	public FragmentedLine(Fragments fragments) {
		this.fragments = fragments;
	}

	@Override
	public void set(int bitIndex) {
		if (smallestBitIndex == null) {
			smallestBitIndex = bitIndex;
			largestBitIndex = bitIndex;
		} else if (bitIndex < smallestBitIndex) {
			throw new LowerThanFirstException("Bit index " + bitIndex
					+ " is smaller than lowest bit in line " + smallestBitIndex);
		} else if (bitIndex > largestBitIndex) {
			largestBitIndex = bitIndex;
		}

		int fragIndex = fragIndex(bitIndex);
		int value = fragments.get(fragIndex);
		int mask = 1 << bitIndexInFrag(bitIndex);
		value |= mask;
		fragments.set(fragIndex, value);
	}

	@Override
	public boolean isSet(int bitIndex) {
		int fragIndex = fragIndex(bitIndex);
		int value = fragments.get(fragIndex);
		int mask = 1 << bitIndexInFrag(bitIndex);
		return (value & mask) != 0;
	}

	@Override
	public int minSetBit() {
		if (smallestBitIndex == null)
			return Integer.MAX_VALUE;

		return smallestBitIndex;
	}

	@Override
	public int maxSetBit() {
		if (smallestBitIndex == null)
			return Integer.MIN_VALUE;

		return largestBitIndex;
	}

	@Override
	public boolean isEmpty() {
		int maxIndex = fragments.maxIndex();
		int minIndex = fragments.minIndex();
		return maxIndex < minIndex;
	}

	@Override
	public Integer nextAliveInclusive(int fromBitIndex) {
		if (largestBitIndex == null || largestBitIndex < fromBitIndex)
			return null;
		else if (fromBitIndex <= smallestBitIndex)
			return smallestBitIndex;
		else {
			for (int i = fromBitIndex; i < largestBitIndex; ++i) {
				if (isSet(i))
					return i;
			}
			return largestBitIndex;
		}
	}

	private int bitIndexInFrag(int bitIndex) {
		int fragSize = fragments.fragSize();
		int modulo = bitIndex % fragSize;
		modulo = modulo < 0 ? modulo + fragSize : modulo;
		int bitIndexInFrag = fragSize - modulo;
		return bitIndexInFrag;
	}

	private int fragIndex(int bitIndex) {
		int result = bitIndex / fragments.fragSize();
		return result + (bitIndex < 0 ? -1 : 0);
	}

}
