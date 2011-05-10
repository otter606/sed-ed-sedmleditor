package org.sedml.jlibsedml.editor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jdom.Element;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sedml.jlibsedml.xmlutils.XMLHandler;

public class XMLValidatorTest {
	XMLHandler val;
	@Before
	public void setUp() throws Exception {
		val = new XMLHandler();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testValidateSingleEls() {
		assertTrue(val.validate("<a/>"));
		assertTrue(val.validate("<a>content</a>"));
		assertTrue(val.validate("  <a>content</a>  ")); // whitespace
		assertFalse(val.validate("  <a>c</a  ")); 
		assertTrue(val.validate("  <a>content</a><b>content</b> ")); //elemetn list with no root
		assertTrue(val.validate("  <a name=\"f\"/><b v='5'/>  <c/>")); //elemetn list with no root
		assertFalse(val.validate("  <a name=\"f\"/><b v='5'/>  <c/")); //elemetn list with no root
		assertFalse(val.validate("  a name=\"f\"/><b v='5'/>  <c/>")); //elemetn list with no root
	}
	
	@Test
	public final void testValidateNestedEls() {
		assertTrue(val.validate("<a><b>blah</b></a>"));
		
	}
	
	@Test
	public final void testValidateSingleTag() {
		assertTrue(val.validate("<div><br/></div>"));
		
	}
	
	@Test
	public final void testGetELs() {
		assertEquals(1,val.getXMLElements("<a>content56</a>").size());
		assertEquals(3,val.getXMLElements("<a name=\"f\"/><b v='5'/>  <c/>").size());
	}
	
	@Test
	public final void testGetRepeatedELs() {
		assertEquals(3,val.getXMLElements("<a/><a/><a/>").size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void testGetELsThrowsIAEIfStringNotValidXML() {
		assertTrue(val.validate("  <a>content</a>  "));
		assertEquals(1,val.getXMLElements("<a>content</a>").size());
		assertFalse(val.validate("  <a name=\"f\"/><b v='5'/>  <c/")); 
		val.getXMLElements("<a name=\"f\"/><b v='5'/>  <c/");
	}
	
	@Test
	public final void testGetELsAsString() {
		String EXPECTED_ROUND_TRIP="<a>content</a>";
		assertTrue(val.validate(EXPECTED_ROUND_TRIP));
		List<Element> els = val.getXMLElements(EXPECTED_ROUND_TRIP);
		assertEquals(EXPECTED_ROUND_TRIP+"\n",val.getElementsAsString(els)); 
	
	}

}
