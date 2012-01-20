package org.sedml.jlibsedml.editor.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.sedml.jlibsedml.editor.ConnectionEditPart;
import org.sedml.jlibsedml.editor.GElementEditPart;
import org.sedml.jlibsedml.editor.MapEditPart;
import org.sedml.jlibsedml.editor.gmodel.Connection;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.GSedML;
import org.sedml.jlibsedml.editor.gmodelcommands.ConnectionCreateCommand;
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
		setIcons();
		setEnabled(false);
	}

	 void setIcons() {
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
	}

	Object getClipboard(){
		return Clipboard.getDefault().getContents();
	}
	@Override
	protected boolean calculateEnabled() {
		if ( getClipboard() == null)
			return false;
		
		if(getSelectedObjects().size() > 0) {
			for (Iterator it = getSelectedObjects().iterator(); it.hasNext();){
				Object o = it.next();
				if (o instanceof EditPart) {
					EditPart ep = (EditPart)o;
					if(ep instanceof ConnectionEditPart || ep instanceof GElementEditPart){
						return false;
					}
				}
			}
		}
		Object  contents =  getClipboard();
		if(contents instanceof List &&!((List)contents).isEmpty() && ((List)contents).get(0) instanceof GElement ) {
			return true;
		}
		return false;
	}
	
	public void run() {
		MapEditPart selected = ((MapEditPart) getSelectedObjects().get(0));
		GSedML root =  (GSedML)(selected.getModel());
		List<GElement> toAdd = new ArrayList<GElement>();
		List<GElement>  clipboard =(List)  getClipboard();
		Map<GElement, GElement> clipBrd2Copy = new HashMap<GElement, GElement>();
		for (GElement cpbord: clipboard) {
			GElement gep = cpbord.getCopy();
			toAdd.add(gep);
			clipBrd2Copy.put(cpbord, gep);
			}
		
		CompoundCommand cc = new CompoundCommand();
	 	SelectObjectsInViewerHelper <GElement>helper = new SelectObjectsInViewerHelper<GElement>(selected);
		for (GElement ge:toAdd){
			ShapeCreateCommand scc = new ShapeCreateCommand(ge, root, new Rectangle(ge.getLocation().getX(), 
					ge.getLocation().getY(), ge.getSize().getWidth(), ge.getSize().getHeight()),true);
			cc.add(scc);
		}
		for (GElement el: clipboard){
			List<Connection> srces = el.getSrcConnections();
			for (Connection c: srces){
				if(clipboard.contains(c.getTarget())){
					ConnectionCreateCommand ccc= new ConnectionCreateCommand(clipBrd2Copy.get(c.getSource()), SWT.LINE_SOLID);
					ccc.setTarget(clipBrd2Copy.get(c.getTarget()) );
					cc.add(ccc);
				}
			}
		}
		execute(cc);
		helper.selectObjectsInViewer(toAdd);
	}

}
