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
		int bitSetIndex = index - offset;
		return bitSetIndex >= 0 && bs.get(bitSetIndex);
	}

	@Override
	public int minSetBit() {
		if (isEmpty) {
			// TODO testcase for this
		} else {
			return offset;
		}

		return 0;
	}

	@Override
	public int maxSetBit() {
		if (isEmpty) {
			// TODO testcase for this
			return 1234;
		} else {
			return offset + bs.length();
		}
	}

}
