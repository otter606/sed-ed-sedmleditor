package org.sedml.jlibsedml.editor.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.sedml.jlibsedml.editor.GElementEditPart;
import org.sedml.jlibsedml.editor.gmodel.GElement;

public class CopyObjectsAction extends SelectionAction {

	public CopyObjectsAction(IWorkbenchPart part) {
		super(part);
		// TODO Auto-generated constructor stub
	}
	
	protected void init (){
		setId(ActionFactory.COPY.getId());
		setActionDefinitionId("Copy");
		setText("Copy");
		setToolTipText("Copy");
		setEnabled(true);
	}

	/**
	 * Is enabled if >= 1 GElementEditPart is selected. 
	 */
	@Override
	protected boolean calculateEnabled() {
		for (Iterator it = getSelectedObjects().iterator(); it.hasNext();) {
			Object o = it.next();
			if(o instanceof GElementEditPart){
				return true;
			}
		}
		return false;
	}
	
	public void run(){
		List<GElement> toCopy = new ArrayList<GElement>();
		for (Iterator it = getSelectedObjects().iterator(); it.hasNext();) {
			Object o = it.next();
			if(o instanceof GElementEditPart){
				toCopy.add(((GElementEditPart)o).getCastedModel());
			}
		}
	

		List<GElement> copied = new ArrayList<GElement>();
		for (GElement el: toCopy){
			copied.add(el.getCopy());
		}
		

		Clipboard.getDefault().setContents(copied);
	}

}
