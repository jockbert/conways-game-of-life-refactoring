package gol.world.neo;

import static org.junit.Assert.*;

import org.junit.Test;

public class SegmentTest {

	@Test
	public void testGetSegmentsWithNoValue() throws Exception {
		Segmented s = new ByteArrLine();

		assertFalse(s.hasBytes());
		assertEquals(0, s.minIndex());
		assertEquals(0, s.maxIndex());
	}

	@Test
	public void testGetSegmentsWithOneValue() throws Exception {
		ByteArrLine l = new ByteArrLine();

		l.set(0);
		l.set(5);


		assertTrue(l.hasBytes());
		assertEquals(0, l.minIndex());
		assertEquals(0, l.maxIndex());

		assertEquals(0, l.getByte(-1));
		assertEquals(2 + 64, l.getByte(0));
		assertEquals(0, l.getByte(1));
	}
	
	@Test
	public void testGetSegmentsWithSeveralValues() throws Exception {
		ByteArrLine l = new ByteArrLine();
		
		l.set(-5);
		l.set(13);
		
		assertTrue(l.hasBytes());
		assertEquals(-1, l.minIndex());
		assertEquals(2, l.maxIndex());
	}

}
