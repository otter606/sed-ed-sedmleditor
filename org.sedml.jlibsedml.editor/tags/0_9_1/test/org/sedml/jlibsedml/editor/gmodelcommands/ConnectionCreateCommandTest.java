package org.sedml.jlibsedml.editor.gmodelcommands;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.GVariable;
import org.sedml.jlibsedml.editor.gmodel.TestUtils;

public class ConnectionCreateCommandTest {
	
	ConnectionCreateCommand comm;
	GElement src = TestUtils.createInValidGTask("id");
	GElement targ  =new GVariable();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testCanExecuteNeedsTargetSet() {
		comm = new ConnectionCreateCommand(src, 0);
		assertFalse(comm.canExecute());
		comm.setTarget(targ);
		assertTrue(comm.canExecute());

	}

	@Test
	public final void testExecute() {
		comm = new ConnectionCreateCommand(src, 0);
		comm.setTarget(targ);
		comm.execute();
		assertTrue(src.getSrcConnections().size() >0);
		assertTrue(targ.getTargetConnections().size() >0);
		
		comm.undo();
		assertTrue(src.getSrcConnections().size() ==0);
		assertTrue(targ.getTargetConnections().size() ==0);
		
		comm.redo();
		assertTrue(src.getSrcConnections().size() >0);
		assertTrue(targ.getTargetConnections().size() >0);
	}

	

}
