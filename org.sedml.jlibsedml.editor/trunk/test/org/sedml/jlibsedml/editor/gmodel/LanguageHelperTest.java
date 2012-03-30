package org.sedml.jlibsedml.editor.gmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jlibsedml.modelsupport.SUPPORTED_LANGUAGE;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LanguageHelperTest {
	LanguageHelper helper;
	@Before
	public void setUp() throws Exception {
		helper=new LanguageHelper();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetURNForLangReturnsNameIfNotFound() {
		final String RANDOM_NAME="fadfdf";
		assertEquals(RANDOM_NAME,helper.getURNForLang(RANDOM_NAME));
	}
	
	@Test
	public final void testGetURNForLang() {
		assertEquals(SUPPORTED_LANGUAGE.CELLML_1_0.getURN(),helper.getURNForLang(SUPPORTED_LANGUAGE.CELLML_1_0.getLanguage()));
	}
	
	@Test
	public final void testGetURNForEmptyStringReturnsEmpty() {
		assertEquals("",helper.getURNForLang(""));
	}
	
	@Test
	public final void testGetURNForNullStringReturnsEmpty() {
		assertEquals(null,helper.getURNForLang(null));
	}
	
	@Test
	public final void testGetLanguageStringsHasNoNullEntries() {
		String [] langs=helper.getLanguagesAsStrings();
		for (String lang:langs){
			assertNotNull(lang);
		}
	}
	
	@Test
	public final void testGetIndexForSBML() {
		final int EXPECTED_INDEX=7;
		assertEquals(EXPECTED_INDEX,helper.getIndexFor(SUPPORTED_LANGUAGE.SBML_GENERIC));
	}
	
	@Test
	public final void testGetIndexForURN() {
		final int EXPECTED_INDEX=7;
		assertEquals(EXPECTED_INDEX,helper.getIndexForURN(SUPPORTED_LANGUAGE.SBML_GENERIC.getURN()));
	}
	
	@Test
	public final void testGetIndexForURNReturnsMinus1IfNotFound() {
		final int EXPECTED_INDEX=-1;
		assertEquals(EXPECTED_INDEX,helper.getIndexForURN("ANY_NON_URN"));
	}

}
