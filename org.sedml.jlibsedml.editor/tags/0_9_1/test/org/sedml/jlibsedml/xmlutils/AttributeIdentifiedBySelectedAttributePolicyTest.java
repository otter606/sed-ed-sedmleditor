package org.sedml.jlibsedml.xmlutils;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.filter.ElementFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AttributeIdentifiedBySelectedAttributePolicyTest {

	ISelectionChangedPolicy policy;
	Document doc;
	
	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetDescription() {
		policy = new AttributeIdentifiedBySelectedAttributePolicy(null,null);
		assertTrue(policy.getDescription().length()>1);
	}

	@Test
	public final void testGetXPathForAttribute() throws JDOMException, IOException {
		String xmltoTest="<a><b name='c' id='2'/></a>";
		doc = getDoc(xmltoTest);
		List els = doc.getRootElement().getContent(new ElementFilter());
		Attribute wanted = ((Element)els.get(0)).getAttribute("name");
		Attribute elID = ((Element)els.get(0)).getAttribute("id");
		
		policy = new AttributeIdentifiedBySelectedAttributePolicy(doc,elID);
		
		assertEquals("/a:a/a:b[@id='2']/@name",policy.getXPathForAttribute(wanted));
	}

	Document getDoc(String in) throws JDOMException, IOException {
		return new XMLUtils().readDoc(new ByteArrayInputStream(in.getBytes()));
	}
	@Test
	public final void testGetXPathForElement() throws JDOMException, IOException {
		String xmltoTest="<a><b name='c'/></a>";
		doc = getDoc(xmltoTest);
		policy = new ElementIdentifiedBySelectedAttributePolicy(doc);
		assertEquals("/a:a",policy.getXPathForElement(doc.getRootElement()));
	}

}
