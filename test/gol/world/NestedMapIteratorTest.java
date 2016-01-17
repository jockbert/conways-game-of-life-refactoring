package gol.world;

import static gol.Cell.cell;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gol.Cell;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class NestedMapIteratorTest {

	List<Integer> emptyList = Arrays.asList();
	List<Integer> oneElemList = Arrays.asList(10);
	List<Integer> twoElemList = Arrays.asList(21, 22);

	@Test
	public void testEmpty() {
		Map<Integer, List<Integer>> map = new HashMap<>();
		map.put(1, emptyList);
		map.put(2, emptyList);
		map.put(3, emptyList);

		Iterator<Cell> it = new NestedMapIterator<>(map,
				list -> list.iterator());

		assertFalse(it.hasNext());
	}

	@Test
	public void testOne() {
		Map<Integer, List<Integer>> map = new HashMap<>();
		map.put(1, emptyList);
		map.put(2, oneElemList);
		map.put(3, emptyList);

		Iterator<Cell> it = new NestedMapIterator<>(map,
				list -> list.iterator());

		assertTrue(it.hasNext());
		assertTrue(it.hasNext());
		assertEquals(cell(10, 2), it.next());
		assertFalse(it.hasNext());
		assertFalse(it.hasNext());
	}

	@Test
	public void testTwo() {
		Map<Integer, List<Integer>> map = new HashMap<>();
		map.put(1, emptyList);
		map.put(6, twoElemList);

		Iterator<Cell> it = new NestedMapIterator<>(map,
				list -> list.iterator());

		assertTrue(it.hasNext());
		assertEquals(cell(21, 6), it.next());
		assertTrue(it.hasNext());
		assertEquals(cell(22, 6), it.next());
		assertFalse(it.hasNext());
	}

}
