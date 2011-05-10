package org.sedml.jlibsedml.editor.gmodel;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GCurveTest {

	GCurve c1,c2;
	GDataGenerator orig_X,orig_y;
	@Before
	public void setUp() throws Exception {
		c1 = new GCurve();
		c2 = new GCurve();
		orig_X = new GDataGenerator();
		orig_y = new GDataGenerator();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testSetUpRefs() {
		c1.setUpRefs(orig_X);
		assertEquals(orig_X,c1.getX());
		assertNull(c1.getY());
		
		c1.setUpRefs(orig_y);
		assertEquals(orig_X,c1.getX());
		assertEquals(orig_y,c1.getY());
	}

	@Test
	public final void testRemoveRefs() {
		c1.setUpRefs(orig_X);
		c1.setUpRefs(orig_y);
		c1.removeRefs(orig_y);
		assertNull(c1.getY());
		assertEquals(orig_X,c1.getX());
		c1.removeRefs(orig_X);
		//both now null
		assertNull(c1.getY());
		assertNull(c1.getX());
		
		//now reset the old y it should still be y?
		c1.setUpRefs(orig_y);
		assertEquals(orig_y,c1.getY());
	}

}
