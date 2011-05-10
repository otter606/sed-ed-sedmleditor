package org.sedml.jlibsedml.editor.configdialogs;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sedml.jlibsedml.editor.configdialogs.IsUniqueIDValidator;
import org.sedml.jlibsedml.editor.gmodel.ElementStub;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.GSedML;

public class IsUniqueIDValidatorTest {
	IsUniqueIDValidator val;
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testValidate() {
		GElement g = new ElementStub();
		g.setId("a");
		GElement g2 = new ElementStub();
		g2.setId("a2");
		GSedML root = new GSedML();
		root.addChild(g);
		root.addChild(g2);
		val = new IsUniqueIDValidator(g);
		assertFalse(val.validate(null));
		assertFalse(val.validate(""));
		assertTrue(val.validate("a")); // this is id of this element so is ok
		assertFalse(val.validate("a2")); // this is ID of new element
		assertTrue(val.validate("new"));
	}

}
