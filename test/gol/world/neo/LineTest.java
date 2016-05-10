package gol.world.neo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public abstract class LineTest {

	Line l = line();

	protected abstract Line line();

	public static class BasicLineTest extends LineTest {

		@Override
		protected Line line() {
			return new BasicLine();
		}
	}

	public static class FragmentedLineNestedTest extends LineTest {

		@Override
		protected Line line() {
			return new FragmentedLine(new LineFragments(3, new BasicLine()));
		}
	}

	public static class FragmentedLineBareTest extends LineTest {

		@Override
		protected Line line() {
			return new FragmentedLine(new BasicFragments(3));
		}
	}

	@Test
	public void testToLargeIndexInNextWithSet() throws Exception {
		l.set(42);
		assertNull(l.nextAliveInclusive(Integer.MAX_VALUE));
	}

	public void testIsEmptyAtFirst() {
		assertTrue(l.isEmpty());
		l.set(10);
		assertFalse(l.isEmpty());
	}

	@Test
	public void testToLargeIndexInNextWithOutSet() throws Exception {
		assertNull(l.nextAliveInclusive(Integer.MAX_VALUE));
	}

	@Test
	public void testNextAliveFromMin() {
		l.set(42);
		l.set(43);
		assertEquals(42, (int) l.nextAliveInclusive(Integer.MIN_VALUE));
	}

	@Test
	public void testNextAliveInclusive() {
		l.set(42);
		l.set(43);
		assertEquals(42, (int) l.nextAliveInclusive(42));
	}

	@Test
	public void testNextAlivePastFirstSetIndex() {
		l.set(42);
		l.set(43);
		l.set(44);
		assertEquals(43, (int) l.nextAliveInclusive(43));
	}

	@Test
	public void testSetAndGetValue() throws Exception {

		l.set(-7);
		l.set(1);
		l.set(3);

		assertFalse(l.isSet(-8));
		assertTrue(l.isSet(-7));
		assertFalse(l.isSet(-6));

		assertFalse(l.isSet(0));
		assertTrue(l.isSet(1));
		assertFalse(l.isSet(2));
		assertTrue(l.isSet(3));
		assertFalse(l.isSet(4));
	}

	@Test(expected = LowerThanFirstException.class)
	public void testNotSmallestFirstThrows() throws Exception {

		l.set(4);
		l.set(3);
	}

	@Test
	public void testLargestAndSmallesBits() throws Exception {
		l.set(3);

		assertEquals(3, l.minSetBit());
		assertEquals(3, l.maxSetBit());

		l.set(5);

		assertEquals(3, l.minSetBit());
		assertEquals(5, l.maxSetBit());
	}

	@Test
	public void testUninitialized() throws Exception {
		assertFalse(l.isSet(0));
		assertEquals(Integer.MAX_VALUE, l.minSetBit());
		assertEquals(Integer.MIN_VALUE, l.maxSetBit());
	}

	@Test
	public void testIsEmpty() throws Exception {
		assertTrue("Should be empty at first", l.isEmpty());
		l.set(12345);
		assertFalse("Should be non-empty after some set bit", l.isEmpty());
	}
}
