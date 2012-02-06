package org.sedml.jlibsedml.editor.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.sedml.jlibsedml.editor.GElementEditPart;
import org.sedml.jlibsedml.editor.gmodel.Connection;
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
		setIcons();
	}
	
	 void setIcons() {
			ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
			setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
			setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
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
		CopyAndCutActionHelper helper= new CopyAndCutActionHelper();
		List<GElement> copied = helper.getCopiesOfSelection(getSelectedObjects());

		copyToClipboard(copied);
	}

	 void copyToClipboard(Object copied) {
		Clipboard.getDefault().setContents(copied);	
	}

	

}
