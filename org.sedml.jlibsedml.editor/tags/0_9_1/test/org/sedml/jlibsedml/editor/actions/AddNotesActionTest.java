package org.sedml.jlibsedml.editor.actions;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.eclipse.gef.GraphicalEditPart;
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
import org.sedml.jlibsedml.editor.gmodel.ElementStub;
import org.sedml.jlibsedml.editor.gmodel.GElement;

@RunWith(JMock.class)
public class AddNotesActionTest {
	Mockery mockery = new JUnit4Mockery();
	IWorkbenchPart mockPart =mockery.mock(IWorkbenchPart.class);
	private AddNotesAction action;
	private AdNotesStub actionTSS;
	GElementEditPart ep = new GElementEditPart();
	GElementEditPart ep2 = new GElementEditPart();
	
	class AdNotesStub extends AddNotesAction{

		public AdNotesStub(IWorkbenchPart part) {
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
	}
		
	
	@Before
	public void setUp() throws Exception {
		actionTSS = new AdNotesStub(mockPart);
		action=actionTSS;
		 ep.setModel(new ElementStub());
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public final void testCalculateEnabled() {
		actionTSS.setSelectedObjects(Arrays.asList(new GraphicalEditPart[]{ep}));
		assertTrue(action.isEnabled());
	}
	
	@Test
	public final void testCalculateEnabledFalseIfNotSelected() {
		actionTSS.setSelectedObjects(Arrays.asList(new GElementEditPart[]{}));
		assertFalse(action.isEnabled());
	}
	
	@Test
	public final void testCalculateEnabledFalseIfMoreThan1EPSelected() {
		actionTSS.setSelectedObjects(Arrays.asList(new GElementEditPart[]{ep,ep2}));
		assertFalse(action.isEnabled());
	}
	
	@Test
	public final void testCalculateEnabledFalseIfEPNotGraphicalEP() {
		GraphicalEditPart gep = new MapEditPart();
		actionTSS.setSelectedObjects(Arrays.asList(new GraphicalEditPart[]{gep}));
		assertFalse(action.isEnabled());
	}
		
	

}
