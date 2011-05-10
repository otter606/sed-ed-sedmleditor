package org.sedml.jlibsedml.editor.gmodel;


import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GVarTest {

	GTask task;
	GModel model;
	GSimulation sim;
	@Before
	public void setUp() throws Exception {
		sim = TestUtils.createValidGSim("w");
		model = TestUtils.createValidGModel("m");
		task = TestUtils.createValidGTask("task");
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testRefsSetUpWithConnection(){
		Connection c = new Connection(task, sim);
		assertEquals(sim,task.getSim());
		Connection c2 = new Connection(task, model);
		assertEquals(model,task.getModel());
		
		c2.disconnect();
		assertNull(task.getModel());
		c.disconnect();
		assertNull(task.getSim());
	}

}
