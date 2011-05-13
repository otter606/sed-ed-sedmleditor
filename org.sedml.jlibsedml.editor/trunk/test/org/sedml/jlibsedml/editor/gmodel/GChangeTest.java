package org.sedml.jlibsedml.editor.gmodel;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sedml.NewXML;
import org.sedml.SEDMLTags;
import org.sedml.XPathTarget;

public class GChangeTest {

	private GChange change;
	Element el = new Element("el1");
	Element el2 = new Element("el2");
	
	NewXML newxml = new NewXML(Arrays.asList(new Element[]{el}));
	@Before
	public void setUp() throws Exception {
		el.addContent(el2);
		change = new GChange() ;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testCanGetSedMLFalseAfterCreation() {
		assertFalse(change.canGetSedML());
	}
	
	@Test
	public final void testCanGetSedMLForAttr() {
		change.setChType(SEDMLTags.CHANGE_ATTRIBUTE_KIND);
		change.setNewValue("2.3");
		change.setTarget(new XPathTarget("/sbml:sbml/sbml:sometag[id='d']"));
		assertTrue(change.canGetSedML());
	}
	
	@Test
	public final void testCanGetSedMLForNewXML() {
		change.setChType(SEDMLTags.ADD_XML_KIND);
		change.setNewxml(newxml);
		change.setTarget(new XPathTarget("/sbml:sbml/sbml:sometag[id='d']"));
		assertTrue(change.canGetSedML());
	}
	
	@Test
	public final void testCanGetSedMLForChangeXML() {
		change.setChType(SEDMLTags.CHANGE_XML_KIND);
		change.setNewxml(newxml);
		change.setChType(SEDMLTags.CHANGE_XML_KIND);
		assertFalse(change.canGetSedML());
		change.setTarget(new XPathTarget("/sbml:sbml/sbml:sometag[id='d']"));
		assertTrue(change.canGetSedML());
	}
	
	@Test
	public final void testCanGetSedMLForRemoveXML() {
		change.setChType(SEDMLTags.REMOVE_XML_KIND);
		change.setTarget(new XPathTarget("/sbml:sbml/sbml:sometag[id='d']"));
		assertTrue(change.canGetSedML());
	}

	@Test
	(expected=AssertionError.class)
	public final void testGetSEDMLObject() {
		change.getSEDMLObject();
	}
	
	@Test
	public final void testCanGetModelFalseByDefault() {
		assertFalse(change.canGetModel());
	}
	
	@Test
	public final void testCanGetModelIfIsAddedToModelWhichHasAccessibleSrc() {
		GModel gm = TestUtils.createValidGModel("id");
		gm.addChange(change);
		GSedML sedml = new GSedML();
		sedml.addChild(gm);
		assertTrue(change.canGetModel());
		assertNotNull(change.getModelDocument());
	}
	
	@Test
	public final void testGetType() {
	 assertEquals(GChange.CHANGE, change.getType());	
	}
	
	@Test
	(expected= AssertionError.class)
	public final void testCanGetSEMLThrowsAssertErrIfunknowntype() {
	 change.setChType("UNKNOWN_TYPE");
	 change.setNewValue("3");
	 change.getSEDMLObject();
	}
	
	@Test
	public void testGetCopy () throws JDOMException, IOException{
		change.setChType(SEDMLTags.CHANGE_XML_KIND);
		change.setNewxml(newxml);
		change.setNotes(TestUtils.createNote());
		
		GChange copy = change.getCopy();
		assertTrue(copy.isModifyXMLChange());
		assertTrue(copy.getChType().equals(SEDMLTags.CHANGE_XML_KIND));
		assertNotNull(copy.getNewxml());
		NewXML copiedXML = copy.getNewxml();
		// modify copy
		copiedXML.getXml().get(0).setName("DIFERENT");
		
		// check original still the same
		assertFalse(change.getNewxml().getXml().get(0).getName().equals("DIFFERENT"));
		assertNotNull(copy.getNotes());
	}
	

}
