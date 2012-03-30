package org.sedml.jlibsedml.editor.gmodel;


import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jlibsedml.Annotation;
import org.jlibsedml.SEDBase;
import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.SedML;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LocationTest {
	
	

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPersistToXML() throws JAXBException, JDOMException, IOException{
		Location LOC=new Location(3,4);
		Size s = new Size(20,40);
		Container c = new Container(LOC,s);
		
		 
        Marshaller marshaller = TestUtils.createMarshaller(Container.class);
   
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshaller.marshal(c, baos);
        
        String out=(new String (baos.toByteArray()));
        System.err.println(out);
        Unmarshaller um = TestUtils.createUnmarshaller(Container.class);
        Container l =(Container)  um.unmarshal(new StringReader(out));
        assertEquals(3,c.getLoc().getX());
      	assertEquals(4,c.getLoc().getY());
      	assertEquals(20,c.getSize().getWidth());
      	assertEquals(40,c.getSize().getHeight());
		
      	SedML sed= new SEDMLDocument().getSedMLModel();
      	Element node = new SAXBuilder().build(new StringReader(out)).detachRootElement();
      	Annotation ann = new Annotation(node);
      	sed.addAnnotation(ann );
      	System.err.println(new SEDMLDocument(sed).writeDocumentToString());
      
      	System.err.println(new SEDMLDocument(sed).writeDocumentToString());
      	
	}
	
	void replaceAnnotation(Annotation annn, SEDBase base){
		Annotation toFind=null;
      	for (Annotation annot: base.getAnnotation()){
      		if(annot.getAnnotationElement().getNamespaceURI().equals("http://www.sedml.sbsi.editor/level1")){
      			toFind=annot;
      			break;
      		}
      	}
      	base.removeAnnotation(toFind);
	}
}
