package gol.world.neo;

public interface Segmented {
	
	int minIndex();
	int maxIndex();
	
	byte getByte(int index);
	boolean hasBytes();
	
}
