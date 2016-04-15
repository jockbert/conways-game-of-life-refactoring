package gol.world.neo;

public interface Indexed<T> {

	int minIndex();

	int maxIndex();

	T get(int index);

	public static int min(Indexed<?>... is) {
		int result = Integer.MAX_VALUE;
		for (Indexed<?> i : is) {
			int index = i.minIndex();
			result = index < result ? index : result;
		}
		return result;
	}

	public static int max(Indexed<?>... is) {
		int result = Integer.MIN_VALUE;
		for (Indexed<?> i : is) {
			int index = i.minIndex();
			result = index > result ? index : result;
		}
		return result;
	}
}
