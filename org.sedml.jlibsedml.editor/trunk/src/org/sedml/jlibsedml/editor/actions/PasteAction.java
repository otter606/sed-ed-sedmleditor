package org.sedml.jlibsedml.editor.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.sedml.jlibsedml.editor.MapEditPart;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.GSedML;
import org.sedml.jlibsedml.editor.gmodelcommands.ShapeCreateCommand;

public class PasteAction extends SelectionAction {

	public PasteAction(IWorkbenchPart part) {
		super(part);
		// TODO Auto-generated constructor stub
	}
	
	protected void init() {
		setId(ActionFactory.PASTE.getId());
		setText("Paste");
		setToolTipText("Paste");
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
		setEnabled(false);
	}

	@Override
	protected boolean calculateEnabled() {
		if (Clipboard.getDefault().getContents() == null)
			return false;
		Object  contents = Clipboard.getDefault().getContents();
		if(contents instanceof List && ((List)contents).get(0) instanceof GElement ) {
			return true;
		}
		return false;
	}
	
	public void run() {
		GSedML root =  (GSedML)((MapEditPart) getSelectedObjects().get(0)).getModel();
		List<GElement> toAdd = new ArrayList<GElement>();
		List<GElement>  clipboard =(List) Clipboard.getDefault().getContents();
		for (GElement cpbord: clipboard) {
			toAdd.add(cpbord.getCopy());
			}
		CompoundCommand cc = new CompoundCommand();
		
		for (GElement ge:toAdd){
			ShapeCreateCommand scc = new ShapeCreateCommand(ge, root, new Rectangle(ge.getLocation().getX(), 
					ge.getLocation().getY(), ge.getSize().getWidth(), ge.getSize().getHeight()));
			cc.add(scc);
		}
		execute(cc);
	}

}
