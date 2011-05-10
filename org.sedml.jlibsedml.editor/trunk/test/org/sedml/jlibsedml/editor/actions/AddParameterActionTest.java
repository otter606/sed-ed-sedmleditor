package org.sedml.jlibsedml.editor.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IWorkbenchPart;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sedml.SEDMLTags;
import org.sedml.jlibsedml.editor.GElementEditPart;
import org.sedml.jlibsedml.editor.MapEditPart;
import org.sedml.jlibsedml.editor.gmodel.GChange;
import org.sedml.jlibsedml.editor.gmodel.GDataGenerator;
import org.sedml.jlibsedml.editor.gmodel.GSedML;
import org.sedml.jlibsedml.editor.gmodel.GTask;
import org.sedml.jlibsedml.editor.gmodel.Location;
@RunWith(JMock.class)
public class AddParameterActionTest {
	Mockery mockery = new JUnit4Mockery();
	IWorkbenchPart mockPart =mockery.mock(IWorkbenchPart.class);
	class AddParameterActionTSS extends AddParameterAction{
		boolean executedStarted=false;
		 public AddParameterActionTSS(IWorkbenchPart part) {
			super(part);
			// TODO Auto-generated constructor stub
		}
		 private List selectedObjects;
		 /*
		  * Package scoped for testing only
		  */
		  void setSelectedObjects (List selectedObjects) {
			   this.selectedObjects=selectedObjects;
			  }
			  protected List getSelectedObjects (){
			   return selectedObjects;
			  }

		// override to bypass eclipse-dependent command stack
		public void  execute (Command c) {
			 executedStarted = true;
			 c.execute();
			
		 }
		
		
	}
	private AddParameterActionTSS stub  = new AddParameterActionTSS(mockPart);
	private AddParameterAction api= stub;
	GElementEditPart ep = new GElementEditPart();
	MapEditPart mep = new MapEditPart();
	
	private GDataGenerator gdg = createGDG();
	@Before
	public void setUp() throws Exception {
		ep.setModel(gdg);
		ep.setParent(mep);
		mep.setModel(new GSedML());
	}

	private GDataGenerator createGDG() {
		GDataGenerator dg =new GDataGenerator();
		dg.setLocation(Location.ORIGIN);
		return dg;
	}
	
	private GTask createWrongElement() {
		GTask dg =new GTask();
		dg.setLocation(Location.ORIGIN);
		return dg;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testRunWithModelIsDG() {
		ep.setModel(gdg);
		stub.setSelectedObjects(Arrays.asList(new GraphicalEditPart[]{ep}));
		api.run();
		assertTrue(stub.executedStarted);
		assertEquals(1,gdg.getParams().size());
		assertTrue(gdg.getSrcConnections().get(0).getTarget().isParameter());
		
	}
	
	@Test
	public final void testRunWithModelIsComputeChange() {
		GChange ch = new GChange();
		ch.setChType(SEDMLTags.COMPUTE_CHANGE_KIND);
		ch.setLocation(Location.ORIGIN);
		
		ep.setModel(ch);
		stub.setSelectedObjects(Arrays.asList(new GraphicalEditPart[]{ep}));
		api.run();
		assertTrue(stub.executedStarted);
		assertEquals(1,ch.getParams().size());
		assertTrue(ch.getSrcConnections().get(0).getTarget().isParameter());
		
	}
	
	@Test
	public final void testRunWithModelIsNotChange() {
		GChange ch = new GChange();
		ch.setChType(SEDMLTags.CHANGE_XML_KIND);
		ch.setLocation(Location.ORIGIN);
		ep.setModel(ch);
		stub.setSelectedObjects(Arrays.asList(new GraphicalEditPart[]{ep}));
		assertFalse(api.calculateEnabled());
		assertFalse(stub.executedStarted);
	
		
	}
	
	@Test
	public final void testRunWithModelIsNotGDGDoesNotExecute() {
		ep.setModel(createWrongElement());
		stub.setSelectedObjects(Arrays.asList(new GraphicalEditPart[]{ep}));
		assertFalse(api.calculateEnabled());
		assertFalse(stub.executedStarted);
	
	}

	

}
