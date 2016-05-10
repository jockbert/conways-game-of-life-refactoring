package gol.world.neo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public abstract class FragmentsTest {

	protected Line l;
	protected Fragments f;

	abstract void setUp(int fragSize);

	public static class LineFragmentsTest extends FragmentsTest {
		@Override
		void setUp(int fragSize) {
			l = new BasicLine();
			f = new LineFragments(fragSize, l);
		}
	}

	public static class BasicFragmentsTest extends FragmentsTest {
		@Override
		void setUp(int fragSize) {
			f = new BasicFragments(fragSize);
			l = new FragmentedLine(f);
		}
	}

	@Test
	public void testEmpty() throws Exception {
		setUp(5);

		assertEquals(Integer.MAX_VALUE, f.minIndex());
		assertEquals(Integer.MIN_VALUE, f.maxIndex());

		assertEquals(0, (int) f.get(-2));
		assertEquals(0, (int) f.get(-1));
		assertEquals(0, (int) f.get(0));
		assertEquals(0, (int) f.get(1));
		assertEquals(0, (int) f.get(2));

		assertEquals(5, f.fragSize());
	}

	@Test
	public void testOneSetBitFragSize1() throws Exception {
		setUp(1);

		l.set(0);

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
		setUp(2);

		l.set(0);

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
		setUp(3);

		l.set(1);

		assertEquals(0, f.minIndex());
		assertEquals(0, f.maxIndex());

		assertEquals(0b00000, (int) f.get(-1));
		assertEquals(0b00100, (int) f.get(0));
		assertEquals(0b00000, (int) f.get(1));

		assertEquals(3, f.fragSize());
	}

	@Test
	public void testOneSetBitFragSize2WithOffset() throws Exception {
		setUp(2);

		l.set(3);

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
		setUp(2);

		f.set(1, 0b0010);

		assertEquals(1, f.minIndex());
		assertEquals(2, f.maxIndex());

		assertEquals(0b0000, (int) f.get(0));
		assertEquals(0b0010, (int) f.get(1));
		assertEquals(0b1000, (int) f.get(2));
		assertEquals(0b0000, (int) f.get(3));

		assertEquals(2, f.fragSize());
	}

	@Test
	public void testOverlapping5() throws Exception {
		setUp(5);

		l.set(-1);
		l.set(0);
		l.set(1);

		assertEquals(-1, f.minIndex());
		assertEquals(0, f.maxIndex());

		assertEquals(0b0000000, (int) f.get(-2));
		assertEquals(0b0000011, (int) f.get(-1));
		assertEquals(0b1110000, (int) f.get(0));
		assertEquals(0b0000000, (int) f.get(1));

		assertEquals(5, f.fragSize());
	}
}
