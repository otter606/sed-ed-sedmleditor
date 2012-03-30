package org.sedml.jlibsedml.editor.gmodelcommands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sedml.jlibsedml.editor.gmodel.ElementStub;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.GSedML;


public class OrderCommandTest {
	
    ZOrderCommand zOrderCommand;
  
    GElement s1,s2,s3,s4;
 
   
    GElement [] childrenArray = new GElement[]{s1,s2,s3,s4};
    List<GElement> children = Arrays.asList(childrenArray);
     private GSedML rmo;
	@Before
	public void setUp() throws Exception {
		rmo = new GSedML();
		  s1 = new ElementStub();
		     s2 = new ElementStub();
		     s3 = new ElementStub();
		     s4 = new ElementStub();
		 
		rmo.addChild(s1);
		rmo.addChild(s2);
		rmo.addChild(s3);
		rmo.addChild(s4);
		
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void testCanExecute () {
		zOrderCommand = new ZOrderCommand(s3, rmo,ZOrderCommand.OrderType.TO_BACK);
		assertTrue (zOrderCommand.canExecute());
	}
	

	@Test
	public void testExecute() {
		zOrderCommand = new ZOrderCommand(s3,  rmo,ZOrderCommand.OrderType.TO_BACK);
		zOrderCommand.execute();
		assertEquals(0, rmo.getChildren().indexOf(s3));
		
		zOrderCommand = new ZOrderCommand(s3, rmo, ZOrderCommand.OrderType.TO_FRONT);
		zOrderCommand.execute();
		assertEquals(3, rmo.getChildren().indexOf(s3));
	}
	
	@Test
	public void testUndo() {
		zOrderCommand = new ZOrderCommand(s3,  rmo,ZOrderCommand.OrderType.TO_FRONT);
		zOrderCommand.execute();
		zOrderCommand.undo();
		assertEquals(2, rmo.getChildren().indexOf(s3));
	}

	@Test
	public void testRedo() {
		zOrderCommand = new ZOrderCommand(s3,  rmo,ZOrderCommand.OrderType.TO_BACK);
		zOrderCommand.execute();
		zOrderCommand.undo();
		zOrderCommand.redo();
		assertEquals(0, rmo.getChildren().indexOf(s3));
	}
	
	@Test
	public void testExecuteWithOnlyOneChild() {
		clearChildren();
		rmo.addChild(s4);
		zOrderCommand = new ZOrderCommand(s4,  rmo,ZOrderCommand.OrderType.TO_BACK);
		zOrderCommand.execute();
		assertEquals(0, rmo.getChildren().indexOf(s4));
		
		zOrderCommand = new ZOrderCommand(s4, rmo, ZOrderCommand.OrderType.TO_FRONT);
		zOrderCommand.execute();
		assertEquals(0, rmo.getChildren().indexOf(s4));
	}
	
	@Test
	public void testUndoWithOnlyOneChild() {
		clearChildren();
		rmo.addChild(s4);
		zOrderCommand = new ZOrderCommand(s4,  rmo,ZOrderCommand.OrderType.TO_BACK);
		zOrderCommand.execute();
		zOrderCommand.undo();
		assertEquals(0, rmo.getChildren().indexOf(s4));
		
	}
	
	@Test
	public void testExecuteForward() {
		
		zOrderCommand = new ZOrderCommand(s1, rmo, ZOrderCommand.OrderType.FORWARD);
		assertEquals(0, rmo.getChildren().indexOf(s1));
		zOrderCommand.execute();
		assertEquals(1, rmo.getChildren().indexOf(s1));
		
	}
	
	@Test
	public void testUndoForward() {
		
		zOrderCommand = new ZOrderCommand(s1, rmo, ZOrderCommand.OrderType.FORWARD);
		assertEquals(0, rmo.getChildren().indexOf(s1));
		zOrderCommand.execute();
		zOrderCommand.undo();
		assertEquals(0, rmo.getChildren().indexOf(s1));
		
	}
	
	@Test
	public void testExecuteForwardWhenAlreadyAtFront() {
		
		zOrderCommand = new ZOrderCommand(s4,  rmo,ZOrderCommand.OrderType.FORWARD);
		assertEquals(3, rmo.getChildren().indexOf(s4));
		zOrderCommand.execute();
		zOrderCommand.undo();
		assertEquals(3, rmo.getChildren().indexOf(s4));
		
	}
	
	@Test
	public void testExecuteBackward() {
		
		zOrderCommand = new ZOrderCommand(s4,  rmo,ZOrderCommand.OrderType.BACKWARD);
		assertEquals(3, rmo.getChildren().indexOf(s4));
		zOrderCommand.execute();
		assertEquals(2, rmo.getChildren().indexOf(s4));
		
	}
	
	@Test
	public void testUndoBackward() {
		
		zOrderCommand = new ZOrderCommand(s4, rmo, ZOrderCommand.OrderType.BACKWARD);
		assertEquals(3, rmo.getChildren().indexOf(s4));
		zOrderCommand.execute();
		zOrderCommand.undo();
		assertEquals(3, rmo.getChildren().indexOf(s4));
		
	}
	
	@Test
	public void testExecuteBackwardWhenAlreadyAtBack() {
		
		zOrderCommand = new ZOrderCommand(s1,  rmo,ZOrderCommand.OrderType.BACKWARD);
		assertEquals(0, rmo.getChildren().indexOf(s1));
		zOrderCommand.execute();

		assertEquals(0, rmo.getChildren().indexOf(s1));
		
	}
	
	@Test
	public void testReorderWithDelete() {
		
		zOrderCommand = new ZOrderCommand(s1, rmo, ZOrderCommand.OrderType.TO_FRONT);
		zOrderCommand.execute();
		assertEquals(4, rmo.getChildren().size());

		rmo.removeChild(s2);
		zOrderCommand = new ZOrderCommand(s1,  rmo,ZOrderCommand.OrderType.TO_BACK);
		zOrderCommand.execute();
		for(GElement imo: rmo.getChildren()){
			System.out.println(imo);
		}
		
	}

	private void clearChildren() {
		rmo.removeChild(s1);
		rmo.removeChild(s2);
		rmo.removeChild(s3);
		rmo.removeChild(s4);
		
	}

	

	

}
