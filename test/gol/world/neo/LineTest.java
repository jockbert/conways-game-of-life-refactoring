package gol.world.neo;

import static org.junit.Assert.assertFalse;
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

}
