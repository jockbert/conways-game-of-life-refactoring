package gol.world.neo;

import java.util.BitSet;

public final class BasicLine implements Line {

	boolean isEmpty = true;
	int offset = 0;
	BitSet bs = null;

	@Override
	public void set(int index) {
		if (isEmpty) {
			offset = index;
			bs = new BitSet();
			bs.set(0);
			isEmpty = false;
		} else if (index < offset) {
			throw new LowerThanFirstException("unexpected index " + index
					+ " is lower than first set index " + offset);
		} else {
			bs.set(index - offset);
		}
	}

	@Override
	public boolean isSet(int index) {
		if (isEmpty) {
			return false;
		} else {
			int bitSetIndex = index - offset;
			return bitSetIndex >= 0 && bs.get(bitSetIndex);
		}
	}

	@Override
	public int minSetBit() {
		if (isEmpty) {
			return Integer.MAX_VALUE;
		} else {
			return offset;
		}
	}

	@Override
	public int maxSetBit() {
		if (isEmpty) {
			return Integer.MIN_VALUE;
		} else {
			return offset + bs.length() - 1;
		}
	}

	@Override
	public boolean isEmpty() {
		return isEmpty;
	}

	@Override
	public Integer nextAliveInclusive(int fromX) {
		fromX = fromX < minSetBit() ? minSetBit() : fromX;

		int result = bs.nextSetBit(fromX - offset);
		return result == -1 ? null : result + offset;
	}
}
