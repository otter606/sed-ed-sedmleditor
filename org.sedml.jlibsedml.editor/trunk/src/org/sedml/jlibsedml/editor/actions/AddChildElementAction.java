package org.sedml.jlibsedml.editor.actions;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;
import org.sedml.jlibsedml.editor.GElementEditPart;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.GSedML;
import org.sedml.jlibsedml.editor.gmodel.Location;
import org.sedml.jlibsedml.editor.gmodel.PropertyChangeNames;
import org.sedml.jlibsedml.editor.gmodelcommands.ConnectionCreateCommand;
import org.sedml.jlibsedml.editor.gmodelcommands.ShapeCreateCommand;

/**
 * Base class for adding 'child' elements to SEDML  top-level objects.
 *  In practice all elements are added to the canvas (MapEditPArt) directly.
 * @author radams
 *
 */
public abstract class AddChildElementAction extends SelectionAction {

	public AddChildElementAction(IWorkbenchPart part) {
		super(part);
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * @return true if only one item selected (for the time being).
	 */
	@Override
	protected boolean calculateEnabled() {
		if (getSelectedObjects().size() == 1 && (getSelectedObjects().get(0) instanceof GElementEditPart) &&
				getModel()!=null)
			return true;
		return false;
	}
	
	abstract GElement getModel();
	
	public void run() {
		EditPart selected = (EditPart) getSelectedObjects().get(0);
		Location currLoc = getModel().getLocation();
		CompoundCommand cc = new CompoundCommand("Add Parameter");
		GElement gvar = createModelElement();
		ShapeCreateCommand scc = new ShapeCreateCommand(gvar, (GSedML)selected.getParent().getModel(),
				  new Rectangle(currLoc.getX()+60, currLoc.getY()+60, 60, 30),false);
		ConnectionCreateCommand ccc = new ConnectionCreateCommand(getModel(), SWT.LINE_SOLID);
		ccc.setTarget(gvar);
		cc.add(ccc);
		cc.add(scc);
		
		execute(cc);
		gvar.firePropertyChange(PropertyChangeNames.PROPERTY_EVENT, null, gvar);
		
		
	}

	abstract GElement createModelElement() ;



}
