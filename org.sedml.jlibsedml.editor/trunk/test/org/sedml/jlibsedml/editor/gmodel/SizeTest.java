package org.sedml.jlibsedml.editor.gmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SizeTest {
    Size s1, s2, s3;
	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	

	@Test
	public void testSizeIntInt() {
		s1 = new Size(5,10);
		assertEquals(5, s1.getWidth());
		assertEquals(10, s1.getHeight());
	}


	@Test
	public void testGetWidth() {
		s1 = new Size(5,10);
		assertEquals(5, s1.getWidth());
		
	}

	@Test
	public void testGetHeight() {
		s1 = new Size(5,10);
		assertEquals(10, s1.getHeight());
		
	}

	@Test
	public void testEqualsObject() {
		s1 = new Size(5,10);
		s2 = new Size(5,10);
		assertTrue(s1.equals(s2));
		assertTrue(s2.equals(s1));
		assertFalse(s1 == s2);
		
		s2 = new Size(8,10);
		assertFalse(s1.equals(s2));
		assertFalse(s2.equals(s1));
	}

	@Test
	public void testShrinkREturnsNewObject() {
		s1 = new Size(10,20);
		Size rc = s1.shrink(5, 5);
		assertFalse(rc == s1);
		assertEquals(10, s1.getWidth());
		assertEquals(20, s1.getHeight());
	}

	@Test
	public void testExpandREturnsNewObject() {
		s1 = new Size(10,20);
		Size rc = s1.expand(5, 5);
		assertFalse(rc == s1);
		assertEquals(10, s1.getWidth());
		assertEquals(20, s1.getHeight());
	}
	
	



}
