package gol.world.neo;

import java.util.ArrayList;

public class BasicFragments implements Fragments {

	static class ByteStorage {
		ArrayList<Byte> list = new ArrayList<Byte>();

		byte get(int index) {
			if (index < 0 || index >= list.size())
				return 0;
			return list.get(index);
		}

		void set(int index, byte value) {

			if (list.size() <= index) {
				fillUpToIndex(index);
				list.add(value);
			} else {
				list.set(index, value);
			}
		}

		private void fillUpToIndex(int index) {
			list.ensureCapacity(index);
			while (list.size() < index)
				list.add((byte) 0);
		}

		int size() {
			return list.size();
		}
	}

	private int fragSize;

	private ByteStorage storage = null;

	private int indexOffset;

	public BasicFragments(int fragSize) {
		this.fragSize = fragSize;
	}

	@Override
	public int minIndex() {
		if (storage == null)
			return Integer.MAX_VALUE;
		else if (get(indexOffset - 1) != 0)
			return indexOffset - 1;
		else
			return indexOffset;
	}

	@Override
	public int maxIndex() {
		if (storage == null)
			return Integer.MIN_VALUE;
		else if (get(indexOffset + storage.size()) != 0)
			return indexOffset + storage.size();
		else
			return indexOffset + storage.size() - 1;
	}

	@Override
	public Integer get(int index) {
		if (storage == null)
			return 0;

		int storageIndex = index - indexOffset;
		int value = storage.get(storageIndex);

		byte nextValue = storage.get(storageIndex + 1);
		if ((nextValue & (1 << fragSize)) != 0) {
			int mask = 1;
			value |= mask;
		}

		byte prevValue = storage.get(storageIndex - 1);
		if (((prevValue & 2) != 0)) {
			int mask = 1 << (fragSize + 1);
			value |= mask;
		}

		return value;
	}

	@Override
	public void set(int index, Integer value) {
		if (value == 0) {
			return;
		}

		if (storage == null) {
			storage = new ByteStorage();
			indexOffset = index;
		}
		int storageIndex = index - indexOffset;
		storage.set(storageIndex, value.byteValue());
	}

	@Override
	public int fragSize() {
		return fragSize;
	}
}
