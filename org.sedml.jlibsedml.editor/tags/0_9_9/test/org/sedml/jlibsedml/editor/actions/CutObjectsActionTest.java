package org.sedml.jlibsedml.editor.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
import org.sedml.jlibsedml.editor.MapEditPart;
import org.sedml.jlibsedml.editor.gmodel.Connection;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.GModel;
import org.sedml.jlibsedml.editor.gmodel.GSedML;
import org.sedml.jlibsedml.editor.gmodel.GTask;
import org.sedml.jlibsedml.editor.gmodel.TestUtils;

@RunWith(JMock.class)
public class CutObjectsActionTest {

	Mockery mockery = new JUnit4Mockery();
	IWorkbenchPart mockPart =mockery.mock(IWorkbenchPart.class);
	private CutObjectsAction action;
	private CutObjectsActionStub actionTSS;
	GElementEditPart ep ;
	GElementEditPart ep2 ;
	MapEditPart mep;
	
	class CutObjectsActionStub extends CutObjectsAction{

		public CutObjectsActionStub(IWorkbenchPart part) {
			super(part);
			// TODO Auto-generated constructor stub
		}

	
		private List selectedObjects;
		boolean isIconSet;
		Object clipboard;
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
					 clipboard = copied;	
				}
	}
	@Before
	public void setUp() throws Exception {
		actionTSS=new CutObjectsActionStub(mockPart);
		action=actionTSS;
		 ep = new GElementEditPart();
		 ep2 = new GElementEditPart();
		 mep = new MapEditPart();
		 
	}

	

	@Test
	public final void testRun() {
		GSedML parent = new GSedML();
		
		GModel gm = TestUtils.createValidGModel("id");
		ep.setModel(gm);
		GTask gt = TestUtils.createValidGTask("tid");
		ep2.setModel(gt);
		parent.addChild(gm);
		parent.addChild(gt);
		new Connection(gm,gt);
		actionTSS.setSelectedObjects(Arrays.asList(new GElementEditPart []{ep,ep2}));
		
		// b4 assertions
		assertEquals(2,parent.getChildren().size());
		assertEquals(1, gm.getSrcConnections().size());
		actionTSS.run();
		List<GElement> cp = (List<GElement>)actionTSS.clipboard;
		assertNotNull(cp);
		assertEquals(2,cp.size());
		assertEquals(1, cp.get(0).getSrcConnections().size());
		assertEquals(1, cp.get(1).getTargetConnections().size());
		assertEquals(0,parent.getChildren().size()); // deleted
		assertEquals(0, gm.getSrcConnections().size()); // conns deleted too
		

		
	}
	
	@Test
	public final void testRunWithConnections() {
		
		GModel gm = TestUtils.createValidGModel("id");
		ep.setModel(gm);
		actionTSS.setSelectedObjects(Arrays.asList(new GElementEditPart []{ep}));
		actionTSS.run();
		List<GElement> cp = (List<GElement>)actionTSS.clipboard;
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
		assertEquals(action.getId(),ActionFactory.CUT.getId()); // to hook into menus
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
