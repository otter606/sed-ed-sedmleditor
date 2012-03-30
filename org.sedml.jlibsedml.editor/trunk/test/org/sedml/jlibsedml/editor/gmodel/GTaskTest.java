package org.sedml.jlibsedml.editor.gmodel;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GTaskTest {

	GTask task;
	GModel model;
	GVariable var;
	@Before
	public void setUp() throws Exception {
		
		model = TestUtils.createValidGModel("m");
		task = TestUtils.createValidGTask("task");
		var = new  GVariable();
		var.setId("var");
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testRefsSetUpWithConnection(){
		Connection c = new Connection(var, task);
		assertEquals(task,var.getTask());
		Connection c2 = new Connection(var, model);
		assertEquals(model,var.getModel());
		
		c2.disconnect();
		assertNull(var.getModel());
		c.disconnect();
		assertNull(var.getTask());
	}

}
