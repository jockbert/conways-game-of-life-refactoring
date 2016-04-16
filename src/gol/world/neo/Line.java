package gol.world.neo;

public interface Line {

	void set(int i);

	boolean isSet(int i);

	int minSetBit();

	int maxSetBit();

	boolean isEmpty();

	Integer nextAlive(int fromX);

	static Line defaultLine() {
		return new BasicLine();
	}
}
