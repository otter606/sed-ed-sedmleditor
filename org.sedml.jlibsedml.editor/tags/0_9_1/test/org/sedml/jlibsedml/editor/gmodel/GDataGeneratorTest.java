package org.sedml.jlibsedml.editor.gmodel;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sedml.DataGenerator;
import org.sedml.Libsedml;

public class GDataGeneratorTest {
 GDataGenerator gdg;
	@Before
	public void setUp() throws Exception {
		gdg=new GDataGenerator();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testCanGetSedML() {
		assertFalse(gdg.canGetSedML());
		GDataGenerator gdg2 = TestUtils.createValidDGWithVars("");
		assertTrue(gdg2.canGetSedML());
	}

	@Test
	public final void testGDataGeneratorDataGenerator() {
		DataGenerator dg = new DataGenerator("id", "name", Libsedml.parseFormulaString(" 3+ 8"));
		GDataGenerator gdg2 = new GDataGenerator(dg);
		assertTrue(gdg2.canGetSedML());
		assertEquals(11,gdg2.getMath().evaluate().getValue());
	}

	@Test
	public final void testGDataGeneratorGetCopy() {
		GDataGenerator orig = TestUtils.createValidDGWithVars("orig");
		assertTrue(orig.getVars().size()>0);
		
		GDataGenerator copy = orig.getCopy();
		// vars not copied
		assertFalse(copy.getVars().size()>0);
		assertTrue(orig.getVars().size()>0);
		
		copy.getMath().removeChildAtIndex(0);
		
		//orig unaffected
		assertEquals(TestUtils.EXPECTED_DG_MATH, orig.getMath().evaluate().getValue());
		
	}

}
