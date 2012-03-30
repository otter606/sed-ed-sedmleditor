package org.sedml.jlibsedml.editor.gmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jlibsedml.Libsedml;
import org.jlibsedml.Notes;
import org.jlibsedml.SedML;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GElementBuilderTest {

	File SEDML12_RAW= new File("TestData/sedMLBIOM12.xml");
	SedML input;
	GElementBuilder builder;
	@Before
	public void setUp() throws Exception {
		input= Libsedml.readDocument(SEDML12_RAW).getSedMLModel();
		builder=new GElementBuilder(input);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGElementBuilderFromSEDMLWithNoLocationsSetsLocationsNotNull() {
		GSedML gsedml = builder.build();
		assertEquals(42,gsedml.getChildren().size());
		for (GElement child:gsedml.getChildren()){
			assertNotNull(child.getLocation());
			assertNotNull(child.getSize());
		}
	}

	@Test
	public final void testGetSetLocationSizeRoundTrip() throws JDOMException, IOException {
		GSedML gsedml = builder.build();
		// add in extra report
		createAReport(gsedml);
	
	
		
		for (int i =0;i<gsedml.getChildren().size();i++){
			GElement el = gsedml.getChildren().get(i);
			el.setLocation(new Location(i,i+1));
			el.setSize(new Size(i,i+2));
			el.setNotes(new Notes(createElementFromXMLStr("<p>" + i + "</p>")));
			assertTrue(el.canGetSedML());
		}
		
		// now write back to sedml
		SEDMLBuilder tosedml = new SEDMLBuilder();
		SedML sedml = tosedml.buildSEDML(gsedml);
		
		GElementBuilder builder2 = new GElementBuilder(sedml);
		GSedML gsedml2 = builder2.build();
		for (int i =0;i<gsedml2.getChildren().size();i++){
			GElement el = gsedml2.getChildren().get(i);
			// check loc,size and notes were all added
			System.err.println("looking at " + el.getId());
			assertEquals(new Location(i,i+1),el.getLocation());
			assertEquals(new Size(i,i+2),el.getSize());
			assertEquals(i+"",el.getNotes().getNotesElement().getText());
			assertTrue(el.canGetSedML());
		}
		
		
		
	}

	private void addAParameter(GSedML gsedml) {
		GParameter gp = new GParameter();
		gp.setId("param1");
		gp.setValue(2.4);
		gsedml.addChild(gp);
		for (GElement child:gsedml.getChildren()){
			if(child.isDataGenerator()){
				new Connection(child, gp);
				break;
			}
		}
		
		
	}

	private void createAReport(GSedML gsedml) {
		// add a report and data set to check.
		GReport grp = new GReport();
		grp.setId("TESTID");
		GDataset gds = new GDataset();
		gds.setId("TESTid2");
		grp.addDataset(gds);
		gds.setLabel("lab");
		for (GElement child:gsedml.getChildren()){
			if(child.isDataGenerator()){
				new Connection(gds, child);
				break;
			}
		}
		gsedml.addChild(grp);
		gsedml.addChild(gds);
	}
	
	
	Element createElementFromXMLStr(String s) throws JDOMException, IOException{
		 Document doc = new SAXBuilder().build(new StringReader(s) );
		 Element el = doc.detachRootElement();
		 return el;
	}

}
