package org.sedml.jlibsedml.editor.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sedml.jlibsedml.editor.ConnectionEditPart;
import org.sedml.jlibsedml.editor.GElementEditPart;
import org.sedml.jlibsedml.editor.gmodel.Connection;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.GModel;
import org.sedml.jlibsedml.editor.gmodel.GTask;
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
		boolean isIconSet;
		private Object clipboard;
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
			void	setIcons(){isIconSet=true;}
			 void copyToClipboard(Object copied) {
					clipboard=copied;	
				}
	}
	@Before
	public void setUp() throws Exception {
		actionTSS=new CopyObjectsActionStub(mockPart);
		action=actionTSS;
		 ep = new GElementEditPart();
		 ep2 = new GElementEditPart();
	}

	
	@Test
	public final void testRun() {
	
		GModel gm = TestUtils.createValidGModel("id");
		ep.setModel(gm);
		GTask gt = TestUtils.createValidGTask("tid");
		ep2.setModel(gt);
		new Connection(gm,gt);
		actionTSS.setSelectedObjects(Arrays.asList(new GElementEditPart []{ep,ep2}));
		actionTSS.run();
		List<GElement> cp = (List<GElement>)actionTSS.clipboard;
		assertNotNull(cp);
		assertEquals(2,cp.size());
		assertEquals(1, cp.get(0).getSrcConnections().size());
		assertEquals(1, cp.get(1).getTargetConnections().size());
		

		
	}
	
	@Test
	public final void testRunWithConnections() {
		
		GModel gm = TestUtils.createValidGModel("id");
		ep.setModel(gm);
		actionTSS.setSelectedObjects(Arrays.asList(new GElementEditPart []{ep}));
		actionTSS.run();
		List<GElement> cp = (List<GElement>) (List<GElement>)actionTSS.clipboard;
		assertNotNull(cp);
		assertEquals(1,cp.size());
		assertEquals(gm.getName(), cp.get(0).getName());
		
		//check is copy
		gm.setName("new name");
		assertFalse(gm.getName().equals(cp.get(0).getName()));
		assertTrue(actionTSS.isIconSet);
		
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
