package org.sedml.jlibsedml.xmlutils;


import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.filter.ElementFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class XPathGeneratorTest {

	File SBMLFileWithNoPrefixes= new File("TestData/BIOMD0000000028.xml");
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testMakePrefixes() throws JDOMException, IOException{
		XMLUtils utils= new XMLUtils();
		Document  doc = utils.readDoc(new FileInputStream(SBMLFileWithNoPrefixes));
		Map<Namespace,String>ns2Prefix = utils.getNamespacesWithPRefixes(doc);
		for (Namespace ns:ns2Prefix.keySet()){
			assertTrue(ns2Prefix.get(ns).length()>0);
		}
	}
	
	@Test
	public void testMakeXPath() throws JDOMException, IOException{
		XMLUtils utils= new XMLUtils();
	
		FileInputStream fis = new FileInputStream(SBMLFileWithNoPrefixes);
		
		Document doc = utils.readDoc(fis);
		
		Iterator it = doc.getDescendants(new ElementFilter());
		while (it.hasNext()) {
			Element el = (Element) it.next();
			String XPATH = utils.getXPathForElementIdentifiedByAttribute(el, doc, new Attribute("initialAmount","1"));
			System.err.println(XPATH);
		}
	}

	
	
	
	


}
