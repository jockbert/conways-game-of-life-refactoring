package gol.world.neo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public abstract class LineTest {

	@Test
	public void testSetAndGetValue() throws Exception {
		Line l = line();

		l.set(1);
		l.set(3);
		l.set(-7);

		assertFalse(l.get(-8));
		assertTrue(l.get(-7));
		assertFalse(l.get(-6));

		assertFalse(l.get(0));
		assertTrue(l.get(1));
		assertFalse(l.get(2));
		assertTrue(l.get(3));
		assertFalse(l.get(4));
	}

	protected abstract Line line();

	public static class ByteArrLineTest extends LineTest {
		@Override
		protected ByteArrLine line() {
			return new ByteArrLine();
		}

		@Test
		public void testByteIndexOf() throws Exception {
			ByteArrLine l = line();

			assertEquals(-2, l.byteIndex(-7));
			assertEquals(-1, l.byteIndex(-6));
			assertEquals(-1, l.byteIndex(-1));
			assertEquals(0, l.byteIndex(0));
			assertEquals(0, l.byteIndex(5));
			assertEquals(1, l.byteIndex(6));
			assertEquals(2, l.byteIndex(13));
		}

		@Test
		public void testBitIndexOf() throws Exception {
			ByteArrLine l = line();

			assertEquals(6, l.bitIndexOf(-7));
			assertEquals(1, l.bitIndexOf(-6));
			assertEquals(2, l.bitIndexOf(-5));
			assertEquals(3, l.bitIndexOf(-4));
			assertEquals(4, l.bitIndexOf(-3));
			assertEquals(5, l.bitIndexOf(-2));
			assertEquals(6, l.bitIndexOf(-1));
			assertEquals(1, l.bitIndexOf(0));
			assertEquals(6, l.bitIndexOf(5));
			assertEquals(1, l.bitIndexOf(6));
		}

		@Test
		public void testFirstBit() throws Exception {
			ByteArrLine l = line();

			l.set(13);

			assertEquals(2, l.arrOffset);
			assertEquals(0b0000100, l.arr[0]);
		}

		@Test
		public void testExtendPositive() throws Exception {
			ByteArrLine l = line();
			l.arr = new byte[] { 4 };
			l.arrOffset = 0;

			l.set(7);

			assertTrue(l.arr.length >= 2);
			assertEquals(0, l.arrOffset);
			assertEquals(4, l.arr[0]);
			assertEquals(4, l.arr[1]);

		}

		@Test
		public void testExtendNegative() throws Exception {
			ByteArrLine l = line();
			l.arr = new byte[] { 4 };
			l.arrOffset = 0;

			l.set(-6);

			assertTrue(l.arr.length >= 2);
			assertEquals(-1, l.arrOffset);
			assertEquals(2, l.arr[0]);
			assertEquals(4, l.arr[1]);
		}

		@Test
		public void testMargin() throws Exception {
			ByteArrLine l = line();
			l.arr = new byte[] { 0b00000010 };
			l.arrOffset = 0;

			l.set(-1);

			assertTrue(l.arr.length >= 2);
			assertEquals(-1, l.arrOffset);
			assertEquals((byte) 0b11000000, l.arr[0]);
			assertEquals(0b00000011, l.arr[1]);
		}
	}
}
