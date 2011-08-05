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
import org.eclipse.ui.part.EditorPart;
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
public class PasteObjectsActionTest {

	Mockery mockery = new JUnit4Mockery();
	IWorkbenchPart mockPart =mockery.mock(IWorkbenchPart.class);
	private PasteAction action;
	private PasteActionStub actionTSS;
	GElementEditPart ep ;
	GElementEditPart ep2 ;
	
	class PasteActionStub extends PasteAction{

		public PasteActionStub(IWorkbenchPart part) {
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
			// override to bypass eclipse-dependent command stack
				public void  execute (Command c) {
					 
					 c.execute();
					
				 }
			void	setIcons(){isIconSet=true;}
	}
	@Before
	public void setUp() throws Exception {
		actionTSS=new PasteActionStub(mockPart);
		action=actionTSS;
		 ep = new GElementEditPart();
		 ep2 = new GElementEditPart();
	}

	@After
	public void tearDown() throws Exception {
		Clipboard.getDefault().setContents(new Object());
	}

	
	
	@Test
	public void testIDIsStandardID(){
		assertEquals(action.getId(),ActionFactory.PASTE.getId()); 
		assertTrue(actionTSS.isIconSet);// to hook into menus
	}

	@Test
	public final void testCalculateEnabledFalseForEmptyClipboard() {
		actionTSS.setSelectedObjects(Collections.EMPTY_LIST);
		assertFalse(action.isEnabled());
	}
	
	@Test
	public final void testCalculateEnabledFalseIfItemSelected() {
		GModel model = TestUtils.createValidGModel("id");
		List<GElement> el = Arrays.asList(new GElement[]{model});
		Clipboard.getDefault().setContents(el);
		actionTSS.setSelectedObjects(Arrays.asList(new GElementEditPart[]{ep}));
		assertFalse(action.isEnabled());
	}
	
	@Test
	public final void testCalculateEnabledForGElementt() {
		GModel model = TestUtils.createValidGModel("id");
		List<GElement> el = Arrays.asList(new GElement[]{model});
		Clipboard.getDefault().setContents(el);
		actionTSS.setSelectedObjects(Collections.EMPTY_LIST);
		assertTrue(action.isEnabled());
		Clipboard.getDefault().setContents(Collections.EMPTY_LIST);
		assertFalse(action.isEnabled());
	}
	
	@Test
	public final void testCalculateEnabledFalseForWrongObject() {
		actionTSS.setSelectedObjects(Arrays.asList(new ConnectionEditPart []{new ConnectionEditPart()}));
		assertFalse(action.isEnabled());
	}

	


}
