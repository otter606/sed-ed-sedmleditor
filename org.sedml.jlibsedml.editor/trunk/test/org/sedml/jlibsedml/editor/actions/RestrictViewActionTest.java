package org.sedml.jlibsedml.editor.actions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.ui.IWorkbenchPart;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sedml.jlibsedml.editor.GElementEditPart;
import org.sedml.jlibsedml.editor.gmodel.GModel;
import org.sedml.jlibsedml.editor.gmodel.GPlot2D;

public class RestrictViewActionTest {
	
	Mockery mockery = new JUnit4Mockery();
	IWorkbenchPart mockPart =mockery.mock(IWorkbenchPart.class);
	private RestrictViewAction action;
	private RestrictViewActionTestTSS actionTSS;
	GElementEditPart ep ;
	GElementEditPart ep2 ;
	
	class RestrictViewActionTestTSS extends RestrictViewAction{

		public RestrictViewActionTestTSS(IWorkbenchPart part) {
			super(part);
			// TODO Auto-generated constructor stub
		}

	
		private List selectedObjects;
		boolean isIconSet;
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
		actionTSS=new RestrictViewActionTestTSS(mockPart);
		action=actionTSS;
		 ep = new GElementEditPart();
		 ep2 = new GElementEditPart();
	}

	@After
	public void tearDown() throws Exception {
	}

	

	@Test
	public final void testCalculateEnabledFor1output() {
		actionTSS.setSelectedObjects(Collections.EMPTY_LIST);
		assertFalse(action.calculateEnabled());
		ep.setModel(new GPlot2D());
		actionTSS.setSelectedObjects(Arrays.asList(new EditPart[]{ep}));
		assertTrue(action.calculateEnabled());
	}
	
	@Test
	public final void testCalculateNotEnabledForMoreThanOne() {
		
		ep.setModel(new GModel());
		actionTSS.setSelectedObjects(Arrays.asList(new EditPart[]{ep}));
		assertFalse(action.calculateEnabled());
	}

}
