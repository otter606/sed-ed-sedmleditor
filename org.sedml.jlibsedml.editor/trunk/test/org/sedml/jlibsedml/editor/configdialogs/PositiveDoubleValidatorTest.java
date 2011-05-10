package org.sedml.jlibsedml.editor.configdialogs;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sedml.jlibsedml.editor.configdialogs.IValidator;
import org.sedml.jlibsedml.editor.configdialogs.PositiveDoubleValidator;

public class PositiveDoubleValidatorTest {

	private static final String VALID_DOUBLE = "23.0d";
	private static final String NEG_VALID_DOUBLE = "-23.0d";
	private static final String INVALID_STR = "abc";
	IValidator val = new PositiveDoubleValidator();
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetValidationFailureMessageNullIfNotValidated() {
		assertNull(val.getValidationFailureMessage());
	}
	
	@Test
	public final void testGetValidationFailureMessageNullIfValid() {
		val.validate(VALID_DOUBLE);
		assertNull(val.getValidationFailureMessage());
	}

	@Test
	public final void testValidate() {
		assertTrue(val.validate(VALID_DOUBLE));
		assertFalse(val.validate(NEG_VALID_DOUBLE));
		assertFalse(val.validate(INVALID_STR));
	}

}
