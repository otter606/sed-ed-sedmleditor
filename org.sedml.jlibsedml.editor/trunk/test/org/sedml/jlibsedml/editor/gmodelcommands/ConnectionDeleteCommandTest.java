package org.sedml.jlibsedml.editor.gmodelcommands;


import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sedml.jlibsedml.editor.gmodel.Connection;
import org.sedml.jlibsedml.editor.gmodel.GTask;
import org.sedml.jlibsedml.editor.gmodel.GVariable;
import org.sedml.jlibsedml.editor.gmodel.TestUtils;

public class ConnectionDeleteCommandTest {

	ConnectionDeleteCommand command;
	
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testDeleteCommand(){
		GTask gt = TestUtils.createInValidGTask("t1");
		GVariable gvar = new GVariable();
		Connection conn = new Connection(gt,gvar
				   );
		command = new ConnectionDeleteCommand(conn);
		
		assertTrue(gvar.getTargetConnections().size()==1);
		assertTrue(gt.getSrcConnections().size()==1);
		command.execute();
		assertTrue(gvar.getTargetConnections().size()==0);
		assertTrue(gt.getSrcConnections().size()==0);
		
		command.undo();
		assertTrue(gvar.getTargetConnections().size()==1);
		assertTrue(gt.getSrcConnections().size()==1);
		
		command.redo();
		assertTrue(gvar.getTargetConnections().size()==0);
		assertTrue(gt.getSrcConnections().size()==0);
		
		
	}

}
