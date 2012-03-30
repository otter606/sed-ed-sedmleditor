package org.sedml.jlibsedml.editor.gmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.jlibsedml.ArchiveComponents;
import org.jlibsedml.Libsedml;
import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.XMLException;
import org.jlibsedml.modelsupport.SUPPORTED_LANGUAGE;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GModelTest {

	File SEDX = new File("TestData/sedx.sedx");
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetModelDocument() throws FileNotFoundException, XMLException {
		ArchiveComponents ac = Libsedml.readSEDMLArchive(new FileInputStream(SEDX));
		SEDMLDocument doc = ac.getSedmlDocument();
		GElementBuilder sedml = new GElementBuilder(doc.getSedMLModel());
		GSedML gsedml = sedml.build();
		gsedml.setArchiveComponents(ac);
		gsedml.setIsSEDX(true);
		for (GElement el: gsedml.getChildren()){
			if(el.isModel()){
				assertTrue(( (GModel)el).canGetModel());
			}
		}
	}
	
	@Test
	public final void testGetCopy() {
		GModel orig = new GModel();
		orig.setLanguage(SUPPORTED_LANGUAGE.CELLML_1_0_ID);
		orig.setSource("a/b/c.xml");
		orig.setLocation(new Location(12,34));
		GModel copy = orig.getCopy();
		assertEquals(SUPPORTED_LANGUAGE.CELLML_1_0_ID,copy.getLanguage());
		assertEquals("a/b/c.xml",copy.getSource());
		assertEquals(new Location(12+GElement.LOCATION_OFFSET,34+GElement.LOCATION_OFFSET), copy.getLocation());
	}
 


}
