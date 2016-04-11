package gol.world;

import static gol.Cell.cell;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import gol.Cell;

import java.util.Iterator;

import org.junit.Test;

public abstract class WorldTest {

	abstract World getWorld();

	private World world = getWorld();

	static class BitSetWorldTest extends WorldTest {
		@Override
		World getWorld() {
			return new BitSetWorld();
		}
	}

	@Test
	public void testEmptyWorld() throws Exception {
		assertNull(world.firstAlive());
	}

	@Test
	public void testOneWorld() throws Exception {
		world.setAlive(4, 2);

		assertEquals(cell(4, 2), world.firstAlive());
		assertEquals(null, world.nextAlive(cell(4, 2)));

		Iterator<Cell> it = World.allAlive(world).iterator();
		assertTrue(it.hasNext());
		assertEquals(it.next(), cell(4, 2));
		assertFalse(it.hasNext());
	}

	@Test
	public void testNegativeWorld() throws Exception {
		world.setAlive(-4, -2);

		assertEquals(cell(-4, -2), world.firstAlive());
		assertEquals(null, world.nextAlive(cell(-4, -2)));
	}

	@Test
	public void testTrickyStart() throws Exception {
		world.setAlive(-4, -2);

		assertEquals(cell(-4, -2), world.nextAlive(cell(-40000, -2)));
	}
}
