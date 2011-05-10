package org.sedml.jlibsedml.editor.gmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BoundsTest {
    Bounds toTest1;
    Bounds toTest2;
	@Before
	public void setUp() throws Exception {
		toTest1 = new Bounds(new Location(5,10), new Size(50,20));
		toTest2 = new Bounds(new Location(5,10), new Size(50,20));
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testBoundsTranslate() {
		Bounds old = toTest1.getCopy();
		Bounds tr = toTest1.translate(new Location(10,20));
		assertEquals(tr, new Bounds(15,30,50,20));
		assertEquals(old, toTest1);
	}
	

	

	@Test
	public void testBoundsILocationISize() {
		Bounds toTest3 = new Bounds(new Location(5,5), new Size(50,50));
		assertNotNull(toTest3);
	}

	@Test
	public void testBoundsBounds() {
		Bounds copy = new Bounds(toTest1);
		assertEquals(toTest1, copy);
	}

	@Test
	public void testGetLocation() {
		assertEquals(5, toTest1.getLocation().getX());

	}
	
	@Test 
	public void testGetUnion() {
		Bounds a = new Bounds (0,0,50,50);
		Bounds B = new Bounds (50,0,50,50);
		
		Bounds expected = new Bounds (0,0,100,50);
		assertEquals(expected, a.getCopy().union(B));
		
		B = new Bounds(25,25,50,50);
		Bounds expected2 = new Bounds (0,0,75,75);
		assertEquals(expected2, a.getCopy().union(B));
		
		Bounds onTop = new Bounds (30,20,10,40);
		System.out.println(B.union(onTop));
		
	
		
	
	}

	@Test
	public void testGetSize() {
		assertEquals(20, toTest1.getSize().getHeight());
	}

	@Test
	public void testShrink() {
		Bounds shrunk = toTest1.shrink(10, 10);
		assertEquals(new Location(15,20), shrunk.getLocation());
		assertEquals(new Size(30,0), shrunk.getSize());
	}

	@Test
	public void testExpand() {
		Bounds expanded= toTest1.expand(10, 10);
		assertEquals(new Location(-5,0), expanded.getLocation());
		assertEquals(new Size(70,40), expanded.getSize());
	}

	@Test
	public void testEqualsObject() {
		assertEquals(toTest1, toTest2);
	}

	@Test
	public void testToString() {
		System.out.println(toTest1.toString());
	}

	@Test
	public void testGetWidth() {
		assertEquals(50, toTest1.getWidth());
	}

	@Test
	public void testGetHeight() {
		assertEquals(20, toTest1.getHeight());
	}

	@Test
	public void testGetX() {
		assertEquals(5, toTest1.getX());
	}

	@Test
	public void testGetY() {
		assertEquals(10, toTest1.getY());
	}
	
	@Test
	public void testCopy () {
		Bounds copy = toTest1.getCopy();
		assertEquals(copy, toTest1.getCopy());
		assertFalse(copy == toTest1);
		
	}
	
	@Test
	public void getCentretest() {
		Bounds b = new Bounds(0,0,100,100);
		assertEquals(new Location(50,50), b.getCentre());
		Bounds b2 = new Bounds(0,0,99,99); // handles int conversion
		assertEquals(new Location(49,49), b2.getCentre());
	}
	
	@Test
	public void testGetArea () {
		Bounds b = new Bounds(0,0,10,20);
		assertEquals(200d, b.getArea());
	}
	
	@Test
	public void testSORT () {
		Bounds SMALL = new Bounds(0,0,10,20);
		Bounds MEDIUM = new Bounds(0,0,15,20);
		Bounds LARGE = new Bounds(0,0,15,25);
		List<Bounds>toSort = Arrays.asList(new Bounds[]{MEDIUM, LARGE, SMALL});//unsorted
		Collections.sort(toSort);
		assertEquals(SMALL, toSort.get(0));
		assertEquals(MEDIUM, toSort.get(1));
		assertEquals(LARGE, toSort.get(2));
	}
	
	@Test
	public void testContains () {
		Bounds inner = new Bounds (5,6,20,30);
		Bounds outer = new Bounds(4,5,25,35);
		assertTrue(outer.contains(inner));
	}
	
	@Test
	public void testContainsFalseForPartialOverlap () {
		Bounds inner = new Bounds (220,220,31,31);
		Bounds outer = new Bounds(0,0,250,250);
		assertFalse(outer.contains(inner));
	}
	
	@Test
	public void testContainsFalseForDisjoint () {
		Bounds inner = new Bounds (270,500,31,31);
		Bounds outer = new Bounds(0,0,250,250);
		assertFalse(outer.contains(inner));
	}
	
	@Test
	public void testContainsFalseForEqual () {
		Bounds inner = new Bounds (270,500,31,31);
		assertFalse(inner.contains(inner));
	}

}
