package org.sedml.jlibsedml.editor.gmodel;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sedml.ArchiveComponents;
import org.sedml.Libsedml;
import org.sedml.SEDMLDocument;
import org.sedml.XMLException;

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



}
