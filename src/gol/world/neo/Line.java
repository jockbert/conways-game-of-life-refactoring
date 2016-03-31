package gol.world.neo;

public interface Line {

	void set(int i);

	boolean isSet(int i);

	int minSetBit();

	int maxSetBit();
}
