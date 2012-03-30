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
import org.sedml.jlibsedml.editor.GElementEditPart;
import org.sedml.jlibsedml.editor.MapEditPart;
import org.sedml.jlibsedml.editor.gmodel.GModel;
import org.sedml.jlibsedml.editor.gmodel.GSedML;
import org.sedml.jlibsedml.editor.gmodel.GTask;
import org.sedml.jlibsedml.editor.gmodel.Location;
@RunWith(JMock.class)
public class AddChangeTest {
	Mockery mockery = new JUnit4Mockery();
	IWorkbenchPart mockPart =mockery.mock(IWorkbenchPart.class);
	class AddChangeActionTSS extends AddChangeAction{
		boolean executedStarted=false;
		 public AddChangeActionTSS(IWorkbenchPart part) {
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
	private AddChangeActionTSS stub  = new AddChangeActionTSS(mockPart);
	private AddChangeAction api= stub;
	GElementEditPart ep = new GElementEditPart();
	MapEditPart mep = new MapEditPart();
	
	private GModel gmodel = createModel();
	@Before
	public void setUp() throws Exception {
		ep.setModel(gmodel);
		ep.setParent(mep);
		mep.setModel(new GSedML());
	}

	private GModel createModel() {
		GModel dg =new GModel();
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
		ep.setModel(gmodel);
		stub.setSelectedObjects(Arrays.asList(new GraphicalEditPart[]{ep}));
		api.run();
		assertTrue(stub.executedStarted);
		assertEquals(1,gmodel.getChanges().size());
		assertTrue(gmodel.getSrcConnections().get(0).getTarget().isChange());
		
	}
	
	
	
	@Test
	public final void testRunWithModelIsNotGDGDoesNotExecute() {
		ep.setModel(createWrongElement());
		stub.setSelectedObjects(Arrays.asList(new GraphicalEditPart[]{ep}));
		assertFalse(api.calculateEnabled());
		assertFalse(stub.executedStarted);
	
	}

	

}
