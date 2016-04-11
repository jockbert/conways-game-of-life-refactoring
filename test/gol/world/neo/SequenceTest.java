package gol.world.neo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;

public class SequenceTest {

	private static final String NULL_OBJECT = "the null string";

	private Sequence<String> s = new Sequence<String>(() -> NULL_OBJECT);

	@Test
	public void testIsEmpty() throws Exception {
		assertTrue(s.isEmpty());
		s.set(123, "some string");
		assertFalse(s.isEmpty());
	}

	@Test
	public void testSetPositive() throws Exception {

		s.set(123456789, "a b c d");

		assertEquals("a b c d", s.getOrMiss(123456789));

		assertEquals(123456789, s.getMin());
		assertEquals(123456789, s.getMax());
	}

	@Test
	public void testSetNegative() throws Exception {

		s.set(-1234, "a b x");

		assertEquals("a b x", s.getOrMiss(-1234));

		assertEquals(-1234, s.getMin());
		assertEquals(-1234, s.getMax());
	}

	@Test
	public void testSetWithSpacing() throws Exception {
		s.set(3, "a");
		s.set(5, "b");

		assertEquals(3, s.getMin());
		assertEquals(5, s.getMax());

		assertEquals("a", s.getOrMiss(3));
		assertEquals(NULL_OBJECT, s.getOrMiss(4));
		assertEquals("b", s.getOrMiss(5));
		assertEquals(NULL_OBJECT, s.getOrMiss(6));
		assertEquals(NULL_OBJECT, s.getOrMiss(33));

		assertEquals(3, s.getMin());
		assertEquals(5, s.getMax());
	}

	@Test(expected = LowerThanFirstException.class)
	public void testSetIndexBeforeFirst() throws Exception {
		s.set(-2, "a");
		s.set(-3, "c");
	}

	static private class Mutable {
		int n = 0;
	}

	@Test
	public void testGetOrAdd() throws Exception {
		Sequence<Mutable> mutables = new Sequence<Mutable>(() -> new Mutable());

		Mutable m = mutables.getOrAdd(4);
		assertEquals(m, mutables.getOrMiss(4));

		assertEquals(0, mutables.getOrMiss(4).n);
		m.n = 56;
		assertEquals(56, mutables.getOrMiss(4).n);

	}

	@Test
	public void testEmptyMin() throws Exception {
		assertEquals(Integer.MAX_VALUE, s.getMin());
	}

	@Test
	public void testEmptyMax() throws Exception {
		assertEquals(Integer.MIN_VALUE, s.getMax());
	}

	@Test
	public void testIterable() throws Exception {
		s.set(3, "x");
		s.set(5, "x");

		assertTrue(s instanceof Iterable<?>);

		Iterator<Integer> it = s.iterator();

		assertTrue(it.hasNext());
		assertEquals(3, (int) it.next());
		assertTrue(it.hasNext());
		assertEquals(4, (int) it.next());
		assertTrue(it.hasNext());
		assertEquals(5, (int) it.next());
		assertFalse(it.hasNext());
	}
}
