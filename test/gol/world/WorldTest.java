package gol.world;

import static gol.Cell.cell;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import gol.Cell;
import gol.world.neo.LookupWorld;

import java.util.Iterator;

import org.junit.Test;

public abstract class WorldTest {

	abstract World getWorld();

	private World world = getWorld();

	public static class LookupWorldTest extends WorldTest {
		@Override
		World getWorld() {
			return World.withHashAndEquals(new LookupWorld());
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

	@Test
	public void testBlockGeneration() throws Exception {
		world.setAlive(1, 1);
		world.setAlive(1, 2);
		world.setAlive(2, 1);
		world.setAlive(2, 2);

		World nextWorld = world.nextWorld();

		assertEquals(cell(1, 1), nextWorld.firstAlive());
		assertEquals(cell(2, 1), nextWorld.nextAlive(cell(1, 1)));
		assertEquals(cell(1, 2), nextWorld.nextAlive(cell(2, 1)));
		assertEquals(cell(2, 2), nextWorld.nextAlive(cell(1, 2)));
		assertEquals(null, nextWorld.nextAlive(cell(2, 2)));
	}

	@Test
	public void testGlider() throws Exception {
		world.setAlive(0, 0);
		world.setAlive(1, 0);

		world.setAlive(0, 1);
		world.setAlive(2, 1);

		world.setAlive(0, 2);

		// has period of 4
		World world1 = world.nextWorld();
		World world2 = world1.nextWorld();
		World world3 = world2.nextWorld();
		World world4 = world3.nextWorld();

		assertEquals(cell(-1, -1), world4.firstAlive());
		assertEquals(cell(0, -1), world4.nextAlive(cell(-1, -1)));
		assertEquals(cell(-1, 0), world4.nextAlive(cell(0, -1)));
		assertEquals(cell(1, 0), world4.nextAlive(cell(-1, 0)));
		assertEquals(cell(-1, 1), world4.nextAlive(cell(1, 0)));
		assertEquals(null, world4.nextAlive(cell(-1, 1)));

	}

	@Test
	public void testSetMixedLines() throws Exception {

		world.setAlive(0, 0);
		world.setAlive(2, 2);

		assertEquals(cell(0, 0), world.firstAlive());
		assertEquals(cell(2, 2), world.nextAlive(cell(0, 0)));

		world.toString();

	}

}
