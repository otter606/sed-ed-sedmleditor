package org.sedml.jlibsedml.editor.actions;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.sedml.jlibsedml.editor.GElementEditPart;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodelcommands.GElementDeleteCommand;

public class CutObjectsAction extends SelectionAction {

	public CutObjectsAction(IWorkbenchPart part) {
		super(part);
		// TODO Auto-generated constructor stub
	}
	
	protected void init (){
		setId(ActionFactory.CUT.getId());
		setActionDefinitionId("Cut");
		setText("Cut");
		setToolTipText("Cut");
		setEnabled(true);
		setIcons();
	}
	
	 void setIcons() {
			ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
			setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
			setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT_DISABLED));
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
		// copy, then delete.
		CopyAndCutActionHelper helper= new CopyAndCutActionHelper();
		List<GElement> copied = helper.getCopiesOfSelection(getSelectedObjects());

		Clipboard.getDefault().setContents(copied);
		CompoundCommand cc = new CompoundCommand("Cut");
		for (Iterator it = getSelectedObjects().iterator(); it.hasNext();) {
			Object o = it.next();
			if(o instanceof GElementEditPart){
				GElement el = (GElement) ((GElementEditPart)o).getModel();
				if (el.getParent()!=null){
					GElementDeleteCommand comm = new GElementDeleteCommand(el.getParent(), el);
					cc.add(comm);
				}
			}
		}
		execute(cc);
		
	}

}
