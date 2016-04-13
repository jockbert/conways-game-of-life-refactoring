package gol.world.neo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FragmenterTest {

	@Test
	public void testEmpty() throws Exception {
		Line l = new BasicLine();
		Fragmenter f = new LineFragmenter(33, l);

		assertEquals(Integer.MAX_VALUE, f.min());
		assertEquals(Integer.MIN_VALUE, f.max());

		assertEquals(0, f.get(-2));
		assertEquals(0, f.get(-1));
		assertEquals(0, f.get(0));
		assertEquals(0, f.get(1));
		assertEquals(0, f.get(2));

		assertEquals(33, f.fragSize());
	}

	@Test
	public void testOneSetBitFragSize1() throws Exception {
		Line l = new BasicLine();
		Fragmenter f = new LineFragmenter(1, l);
		l.set(0);

		assertEquals(-1, f.min());
		assertEquals(1, f.max());

		assertEquals(0b000, f.get(-2));
		assertEquals(0b001, f.get(-1));
		assertEquals(0b010, f.get(0));
		assertEquals(0b100, f.get(1));
		assertEquals(0b000, f.get(2));

		assertEquals(1, f.fragSize());
	}

}
