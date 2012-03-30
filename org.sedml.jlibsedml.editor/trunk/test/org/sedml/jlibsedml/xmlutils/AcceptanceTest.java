package org.sedml.jlibsedml.xmlutils;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.filter.ElementFilter;
import org.jdom.output.XMLOutputter;
import org.jlibsedml.NamespaceContextHelper;
import org.jlibsedml.XPathEvaluationException;
import org.jlibsedml.XPathTarget;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AcceptanceTest {
	File SBMLMODEL_DIR=new File("TestData/weekly_30January2012_sbmls/curated");
	XMLUtils utils = new XMLUtils();
	XPathFactory xpf = XPathFactory.newInstance();
	
	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}
	@Ignore
	@Test
	public final void test() throws JDOMException, IOException, XPathEvaluationException, ParserConfigurationException, SAXException {
		File [] models = SBMLMODEL_DIR.listFiles();

		for (File f : models) {
			if(!f.getName().startsWith("gold")){
				continue;
			}
			System.err.println("Reading "+ f.getName());
			FileInputStream fis = new FileInputStream(f);
			Document doc = utils.readDoc(fis);
			NamespaceContextHelper nc= new NamespaceContextHelper(doc);
			String xmlstr = fromDoc(doc);
			org.w3c.dom.Document w3cdoc=getXMLDocumentFromModelString(xmlstr);
			fis.close();
			Element el = doc.getRootElement();
			examineXML(el,w3cdoc,doc, f.getName(),nc);
			
		}
	}
	String fromDoc (Document xmlDoc){
	XMLOutputter xmlOut = new XMLOutputter();
	
	return xmlOut.outputString(xmlDoc);	
}
	static org.w3c.dom.Document getXMLDocumentFromModelString(final String originalModel)
			throws ParserConfigurationException, UnsupportedEncodingException,
			IOException, SAXException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true); // never forget this!
		DocumentBuilder builder = factory.newDocumentBuilder();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Writer out = new OutputStreamWriter(bos, "UTF8");
		out.write(originalModel);
		out.close();
		bos.close();

		org.w3c.dom.Document doc = builder
				.parse(new ByteArrayInputStream(bos.toByteArray()));
		return doc;
	}

	private void examineXML(Element element, org.w3c.dom.Document w3cdoc, Document doc, String string, NamespaceContextHelper nc) throws XPathEvaluationException {
		List<Element> els = element.getContent(new ElementFilter());
		for (Element child: els ){
			
			String xpathStr  = "";
					
			if (child.getAttributes().size() >0 && child.getAttribute("id")!=null){
				Attribute id =child.getAttribute("id");
				xpathStr = utils.getXPathForElementIdentifiedByAttribute(child, doc, id);
			} else {
				xpathStr = utils.getXPathFor(child, doc);
			}
			System.err.println("EXamining: " +xpathStr + " in " +string );
			
			XPath xpath = xpf.newXPath();
			
			
			nc.process(new XPathTarget(xpathStr));
			if(!nc.isAllXPathPrefixesMapped(new XPathTarget(xpathStr))){
				System.err.println("Not all prefixes mapped, skipping..");
				//return;
			}
			xpath.setNamespaceContext(nc);
			Object rc = applyXpath( xpathStr,
					 xpath,  w3cdoc);
			NodeList nodes = (NodeList) rc;
			assertTrue(" Not unique! " + nodes.getLength(), 1 == nodes.getLength());
			examineXML(child,w3cdoc, doc, string,nc);
			
		}
		
		
	}
	// returned object is a nodeList
		private static Object applyXpath(String xPath,
				XPath xpath, org.w3c.dom.Document w3cdoc)
				throws XPathEvaluationException {
			
			XPathExpression expr;
			Object result;
			try {
				expr = xpath.compile(xPath);

				result = expr.evaluate(w3cdoc, XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new XPathEvaluationException("Could not evaluate XPath:" + xPath, e);
			}
			return result;
		}

}
