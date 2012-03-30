package org.sedml.jlibsedml.editor.gmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GDatasetTest {

	GDataset orig, copy;
	@Before
	public void setUp() throws Exception {
		orig = new GDataset();
	}

	@After
	public void tearDown() throws Exception {
	}



	@Test
	public final void testGetCopy() {
		orig.setLabel("label");
		orig.setSize(new Size(12,23));
		orig.setName("ds1");
		GDataset copy = orig.getCopy();
		assertEquals("label", copy.getLabel());
		assertFalse(orig == copy);
		assertEquals("ds1",copy.getName());
		assertEquals(new Size(12,23), copy.getSize());
	}

}
