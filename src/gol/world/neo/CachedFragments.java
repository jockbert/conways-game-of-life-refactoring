package gol.world.neo;

public class CachedFragments implements Fragments {
	private int[] arr;
	private int minIndex;
	private int fragSize;

	CachedFragments(Fragments frags) {
		minIndex = frags.minIndex();
		arr = new int[frags.maxIndex() - minIndex + 1];

		for (int i = 0; i < arr.length; i++) {
			arr[i] = frags.get(minIndex + i);
		}

		fragSize = frags.fragSize();
	}

	@Override
	public int minIndex() {
		return minIndex;
	}

	@Override
	public int maxIndex() {
		return minIndex + arr.length - 1;
	}

	@Override
	public Integer get(int index) {
		int arrIndex = index - minIndex;
		if (arrIndex < 0 || arrIndex >= arr.length)
			return 0;
		return arr[arrIndex];
	}

	@Override
	public void set(int index, Integer value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int fragSize() {
		return fragSize;
	}

}
