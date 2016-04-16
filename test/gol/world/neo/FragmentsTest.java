package gol.world.neo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FragmentsTest {

	@Test
	public void testEmpty() throws Exception {
		Line l = new BasicLine();
		Fragments f = new LineFragments(33, l);

		assertEquals(Integer.MAX_VALUE, f.minIndex());
		assertEquals(Integer.MIN_VALUE, f.maxIndex());

		assertEquals(0, (int) f.get(-2));
		assertEquals(0, (int) f.get(-1));
		assertEquals(0, (int) f.get(0));
		assertEquals(0, (int) f.get(1));
		assertEquals(0, (int) f.get(2));

		assertEquals(33, f.fragSize());
	}

	@Test
	public void testOneSetBitFragSize1() throws Exception {
		Line l = new BasicLine();
		l.set(0);

		Fragments f = new LineFragments(1, l);

		assertEquals(-1, f.minIndex());
		assertEquals(1, f.maxIndex());

		assertEquals(0b000, (int) f.get(-2));
		assertEquals(0b001, (int) f.get(-1));
		assertEquals(0b010, (int) f.get(0));
		assertEquals(0b100, (int) f.get(1));
		assertEquals(0b000, (int) f.get(2));

		assertEquals(1, f.fragSize());
	}

	@Test
	public void testOneSetBitFragSize2() throws Exception {
		Line l = new BasicLine();
		l.set(0);

		Fragments f = new LineFragments(2, l);

		assertEquals(-1, f.minIndex());
		assertEquals(0, f.maxIndex());

		assertEquals(0b0000, (int) f.get(-2));
		assertEquals(0b0001, (int) f.get(-1));
		assertEquals(0b0100, (int) f.get(0));
		assertEquals(0b0000, (int) f.get(1));

		assertEquals(2, f.fragSize());
	}

	@Test
	public void testOneSetBitFragSize3() throws Exception {
		Line l = new BasicLine();
		l.set(1);

		Fragments f = new LineFragments(3, l);

		assertEquals(0, f.minIndex());
		assertEquals(0, f.maxIndex());

		assertEquals(0b00000, (int) f.get(-1));
		assertEquals(0b00100, (int) f.get(0));
		assertEquals(0b00000, (int) f.get(1));

		assertEquals(3, f.fragSize());
	}

	@Test
	public void testOneSetBitFragSize2WithOffset() throws Exception {
		Line l = new BasicLine();
		l.set(3);

		Fragments f = new LineFragments(2, l);

		assertEquals(1, f.minIndex());
		assertEquals(2, f.maxIndex());

		assertEquals(0b0000, (int) f.get(0));
		assertEquals(0b0010, (int) f.get(1));
		assertEquals(0b1000, (int) f.get(2));
		assertEquals(0b0000, (int) f.get(3));

		assertEquals(2, f.fragSize());
	}

	@Test
	public void testSetFragment() throws Exception {
		Line l = new BasicLine();
		Fragments f = new LineFragments(2, l);

		f.set(1, 0b0010);

		assertEquals(1, f.minIndex());
		assertEquals(2, f.maxIndex());

		assertEquals(0b0000, (int) f.get(0));
		assertEquals(0b0010, (int) f.get(1));
		assertEquals(0b1000, (int) f.get(2));
		assertEquals(0b0000, (int) f.get(3));

		assertEquals(2, f.fragSize());
	}
}
