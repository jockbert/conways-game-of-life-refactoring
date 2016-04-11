package gol.world;

import static gol.Cell.cell;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import gol.Cell;

import java.util.Iterator;

import org.junit.Test;

public class BitSetWorldTest {

	@Test
	public void testEmptyWorld() throws Exception {
		World world = new BitSetWorld();

		assertNull(world.firstAlive());

	}

	@Test
	public void testOneWorld() throws Exception {
		World world = new BitSetWorld();
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
		World world = new BitSetWorld();
		world.setAlive(-4, -2);

		assertEquals(cell(-4, -2), world.firstAlive());
		assertEquals(null, world.nextAlive(cell(-4, -2)));
	}

	@Test
	public void testTrickyStart() throws Exception {

		World world = new BitSetWorld();
		world.setAlive(-4, -2);

		assertEquals(cell(-4, -2), world.nextAlive(cell(-40000, -2)));
	}
}
