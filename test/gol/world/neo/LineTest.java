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

	@Test
	public void testToLargeIndexInNextWithSet() throws Exception {
		l.set(1);
		assertNull(l.nextAliveInclusive(Integer.MAX_VALUE));
	}

	@Test
	public void testToLargeIndexInNextWithOutSet() throws Exception {
		assertNull(l.nextAliveInclusive(Integer.MAX_VALUE));
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
	public void testUninitialized() throws Exception {
		assertFalse(l.isSet(0));
		assertEquals(Integer.MAX_VALUE, l.minSetBit());
		assertEquals(Integer.MIN_VALUE, l.maxSetBit());
	}

	@Test
	public void testIsEmpty() throws Exception {
		assertTrue(l.isEmpty());
		l.set(12345);
		assertFalse(l.isEmpty());
	}
}
