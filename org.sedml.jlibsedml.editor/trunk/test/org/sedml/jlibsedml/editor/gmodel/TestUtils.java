package org.sedml.jlibsedml.editor.gmodel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.sedml.Algorithm;
import org.sedml.Libsedml;
import org.sedml.NewXML;
import org.sedml.Notes;
import org.sedml.SEDMLTags;
import org.sedml.XPathTarget;
import org.sedml.jlibsedml.xmlutils.XMLUtils;
import org.sedml.modelsupport.KisaoOntology;
import org.sedml.modelsupport.SUPPORTED_LANGUAGE;

public class TestUtils {
	
public static final double EXPECTED_DG_MATH = 26;

public static Marshaller createMarshaller(Class clazz) throws JAXBException{
	
	JAXBContext jc = JAXBContext.newInstance(clazz);
	 
    Marshaller marshaller = jc.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    return marshaller;
	
    
}

public static Unmarshaller createUnmarshaller(Class clazz) throws JAXBException{
	
	JAXBContext jc = JAXBContext.newInstance(clazz);
	 
	   return jc.createUnmarshaller();
      
	
}

public static GModel createValidGModel(String id) {
	GModel gmod = new GModel();
	gmod.setId(id);
	gmod.setLanguage(SUPPORTED_LANGUAGE.SBML_GENERIC.getURN());
	gmod.setSource("TestData/BIOMD0000000028.xml");
	return gmod;
}

public static GSimulation createValidGSim(String id) {
	GSimulation gsim = new GSimulation();
	gsim.setId(id);
	gsim.setAlgorithm(createAlgorithm());
	return gsim;
}

 public static GSimulation createInValidGSim(String id) {
	GSimulation gsim = createValidGSim(id);
	gsim.setAlgorithm(null);
	return gsim;
}

public static  Algorithm createAlgorithm() {
	return new Algorithm(KisaoOntology.ALGORITHM_WITH_DETERMINISTIC_RULES.getId());
}


public static GPlot2D createValidPlot2d(String id){
	GPlot2D rc = new GPlot2D();
	rc.setId(id);
	GCurve gc1 = new GCurve();
	gc1.setId("any");
	gc1.setX(createValidDGWithVars("x"));
	gc1.setY(createValidDGWithVars("y"));
	rc.addCurve(gc1);
	return rc;
}

 public static GModel createInValidGModel(String id) {
	GModel gmod = createValidGModel(id);
	gmod.setSource(null);
	return gmod;
}
 
 /**
  * MAths evaluates to 26
  * @param id
  * @return
  */
 public static GDataGenerator createValidDGWithVars(String id){
	 
	 GDataGenerator gdg = new GDataGenerator();
	 gdg.setId(id);
	 gdg.setMath(Libsedml.parseFormulaString("9 + 17"));
	 GVariable gv = new GVariable();
	 gv.setId("var1");
	 gv.setTask(TestUtils.createInValidGTask("t"));
	 gv.setTargetXPath("anyPath/[@id='x'");
	 gdg.addVar(gv);
	 return gdg;
	 
 }

 public static GTask createInValidGTask(String id) {
	GTask gmod = createValidGTask(id);
	gmod.setModel(null);
	return gmod;
}

 public static GTask createValidGTask(String id) {
	GModel mod=createValidGModel("m1");
	GSimulation sim = createValidGSim("s1");
	GTask gt = new GTask();
	gt.setId(id);
	gt.setModel(mod);
	gt.setSim(sim);
	return gt;
}

 public static GReport createValidReport(String id) {
	 GReport rc = new GReport();
	rc.setId(id);
	GDataset gc1 = new GDataset();
	gc1.setId("any");
	gc1.setDg(createValidDGWithVars("x"));
	
	rc.addDataset(gc1);
	return rc;
}

 public static GModel createValidGModelWithChanges(String string) {
	GModel m = createValidGModel("m1");
	
	GChange gc = new GChange();
	gc.setChType(SEDMLTags.ADD_XML_KIND);
	gc.setTarget(new XPathTarget("/sbml:sbml/sbml:listOfParameters/sbml:parameter[id='a']"));
	gc.setNewxml(new NewXML(Collections.EMPTY_LIST));
	m.addChange(gc);
	return m;
}

public static Notes createNote() throws JDOMException, IOException {
	String html = "<div> <p> Hello </p> </div>";
	Document d = new XMLUtils().readDoc(new ByteArrayInputStream(html.getBytes()));
	return new Notes(d.detachRootElement());
}

}
