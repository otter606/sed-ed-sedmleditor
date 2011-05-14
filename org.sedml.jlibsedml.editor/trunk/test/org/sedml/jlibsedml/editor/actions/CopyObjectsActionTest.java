package org.sedml.jlibsedml.editor.actions;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sedml.jlibsedml.editor.ConnectionEditPart;
import org.sedml.jlibsedml.editor.GElementEditPart;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.GModel;
import org.sedml.jlibsedml.editor.gmodel.TestUtils;

@RunWith(JMock.class)
public class CopyObjectsActionTest {

	Mockery mockery = new JUnit4Mockery();
	IWorkbenchPart mockPart =mockery.mock(IWorkbenchPart.class);
	private CopyObjectsAction action;
	private CopyObjectsActionStub actionTSS;
	GElementEditPart ep ;
	GElementEditPart ep2 ;
	
	class CopyObjectsActionStub extends CopyObjectsAction{

		public CopyObjectsActionStub(IWorkbenchPart part) {
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
					 
					 c.execute();
					
				 }
	}
	@Before
	public void setUp() throws Exception {
		actionTSS=new CopyObjectsActionStub(mockPart);
		action=actionTSS;
		 ep = new GElementEditPart();
		 ep2 = new GElementEditPart();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testRun() {
		assertNull(Clipboard.getDefault().getContents());
		GModel gm = TestUtils.createValidGModel("id");
		ep.setModel(gm);
		actionTSS.setSelectedObjects(Arrays.asList(new GElementEditPart []{ep}));
		actionTSS.run();
		List<GElement> cp = (List<GElement>)Clipboard.getDefault().getContents();
		assertNotNull(cp);
		assertEquals(1,cp.size());
		assertEquals(gm.getName(), cp.get(0).getName());
		
		//check is copy
		gm.setName("new name");
		assertFalse(gm.getName().equals(cp.get(0).getName()));
		
		
		
	}
	
	@Test
	public void testIDIsStandardID(){
		assertEquals(action.getId(),ActionFactory.COPY.getId()); // to hook into menus
	}

	@Test
	public final void testCalculateEnabledFalseForEmptyList() {
		actionTSS.setSelectedObjects(Collections.EMPTY_LIST);
		assertFalse(action.isEnabled());
	}
	
	@Test
	public final void testCalculateEnabledForGElementt() {
		actionTSS.setSelectedObjects(Arrays.asList(new GElementEditPart []{ep}));
		assertTrue(action.isEnabled());
	}
	
	@Test
	public final void testCalculateEnabledFalseForConnection() {
		actionTSS.setSelectedObjects(Arrays.asList(new ConnectionEditPart []{new ConnectionEditPart()}));
		assertFalse(action.isEnabled());
	}

	


}
