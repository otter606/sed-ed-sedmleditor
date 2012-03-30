package org.sedml.jlibsedml.editor.configdialogs;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MathValidator {
    IValidator val;
	@Before
	public void setUp() throws Exception {
		val = new MathsValidator();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetValidationFailureMessage() {
		assertNull(val.getValidationFailureMessage());
	}

	@Test
	public final void testValidate() {
		assertTrue(val.validate("x +2"));
		assertFalse(val.validate(null));
		assertFalse(val.validate(""));
		assertFalse(val.validate("3 + ((x)"));//unbalanced bracks
		assertFalse(val.validate(".??"));//gibberish
	}

}
