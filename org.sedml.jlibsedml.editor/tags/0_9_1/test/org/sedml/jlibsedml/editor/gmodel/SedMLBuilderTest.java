package org.sedml.jlibsedml.editor.gmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sedml.SEDMLTags;
import org.sedml.SedML;

public class SedMLBuilderTest {

	SEDMLBuilder builder;
	@Before
	public void setUp() throws Exception {
		builder = new SEDMLBuilder();
	}
	

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testBuildSEDMLWithSingleModel() {
	
			GSedML gsedml = new GSedML();
			GModel gmod = TestUtils.createValidGModel("m1");
			gsedml.addChild(gmod);
			
			SedML sed = builder.buildSEDML(gsedml);
			assertNotNull(sed);
			assertEquals(0,builder.gelsWithErrors.size());
	}
	
	@Test
	public final void testBuildSEDMLWithSingleModelWithChanges() {
		
		GSedML gsedml = new GSedML();
		GModel gmod = TestUtils.createValidGModelWithChanges("m1");
		gsedml.addChild(gmod);
		
		SedML sed = builder.buildSEDML(gsedml);
		assertNotNull(sed);
		assertEquals(0,builder.gelsWithErrors.size());
		assertEquals(SEDMLTags.ADD_XML_KIND, sed.getModels().get(0).getListOfChanges().get(0).getChangeKind());
}
	
	@Test
	public final void testBuildSEDMLWithSingleSim() {
	
			GSedML gsedml = new GSedML();
			GSimulation sim = TestUtils.createValidGSim("sim1");
			sim.setSize(new Size(5,10));
			sim.setLocation(Location.ORIGIN);
			gsedml.addChild(sim);
			SedML sed = builder.buildSEDML(gsedml);
			assertNotNull(sed);
			assertEquals(0,builder.gelsWithErrors.size());
			assertEquals(1,sed.getSimulations().get(0).getAnnotation().size());
	}
	
	@Test
	public final void testBuildSEDMLWithInvalidSim() {
	
			GSedML gsedml = new GSedML();
	
			GSimulation sim = TestUtils.createInValidGSim("anyid");;
			gsedml.addChild(sim);
			SedML sed = builder.buildSEDML(gsedml);
			assertNotNull(sed);
			assertEquals(sim,builder.gelsWithErrors.get(0));
	}
	
	@Test
	public final void testBuildSEDMLWithInvalidModel() {	
		GSedML gsedml = new GSedML();
		GModel gmod = TestUtils.createInValidGModel("m1");
		gsedml.addChild(gmod);
		
		SedML sed = builder.buildSEDML(gsedml);
		assertNotNull(sed);
		assertEquals(1,builder.gelsWithErrors.size());
		assertEquals(gmod,builder.gelsWithErrors.get(0));
}
	
	
	@Test
	public final void testBuildSEDMLWithValidTask() {	
		GSedML gsedml = new GSedML();
		GTask gt = TestUtils.createValidGTask("t1");
		gsedml.addChild(gt);
		
		SedML sed = builder.buildSEDML(gsedml);
		assertNotNull(sed);
		assertEquals(0,builder.gelsWithErrors.size());
		
}
	@Test
	public final void testBuildSEDMLWithInValidTask() {	
		GSedML gsedml = new GSedML();
		GTask gt = TestUtils.createInValidGTask("t1");
		gsedml.addChild(gt);
		SedML sed = builder.buildSEDML(gsedml);
		assertNotNull(sed);
		assertEquals(gt,builder.gelsWithErrors.get(0));
		
}
	
	@Test
	public final void testBuildSEDMLWithValidDG() {	
		GSedML gsedml = new GSedML();
		GDataGenerator gdg = TestUtils.createValidDGWithVars("dg1");
		gsedml.addChild(gdg);
		SedML sed = builder.buildSEDML(gsedml);
		assertNotNull(sed);
		assertEquals(0,builder.gelsWithErrors.size());
		
}
	
	@Test
	public final void testBuildSEDMLWithValidPlot2d() {	
		GSedML gsedml = new GSedML();
		GPlot2D gp = TestUtils.createValidPlot2d("plot1");
		gsedml.addChild(gp);
		SedML sed = builder.buildSEDML(gsedml);
		assertNotNull(sed);
		assertEquals(0,builder.gelsWithErrors.size());
		
}
	@Test
	public final void testBuildSEDMLWithValidReport() {	
		GSedML gsedml = new GSedML();
		GReport gp = TestUtils.createValidReport("ds1");
		gsedml.addChild(gp);
		SedML sed = builder.buildSEDML(gsedml);
		assertNotNull(sed);
		assertEquals(0,builder.gelsWithErrors.size());
		
}
	
	
		
	

	
}
