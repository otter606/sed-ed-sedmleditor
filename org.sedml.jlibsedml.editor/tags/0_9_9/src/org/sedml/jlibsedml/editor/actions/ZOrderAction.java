package org.sedml.jlibsedml.editor.actions;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.sedml.jlibsedml.editor.GElementEditPart;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.GSedML;
import org.sedml.jlibsedml.editor.gmodelcommands.ZOrderCommand;
import org.sedml.jlibsedml.editor.gmodelcommands.ZOrderCommand.OrderType;

public abstract class ZOrderAction extends SelectionAction{
	
	
	public ZOrderAction(IWorkbenchPart part) {
		super(part);

		
	}
	
	protected void init() {
		setId(getZId());
		setText(getZDisplayText());
		setToolTipText(getZDisplayText());
		setEnabled(false);
	}

	protected abstract String getZId();
	protected abstract String getZDisplayText();
	protected abstract OrderType getZOrderType();
	
	/**
	 * @return true if only one item selected (for the time being).
	 */
	@Override
	protected boolean calculateEnabled() {
		if (getSelectedObjects().size() == 1 && (getSelectedObjects().get(0) instanceof GElementEditPart))
			return true;
		return false;
	}

	public void run() {
		EditPart selected = (EditPart) getSelectedObjects().get(0);
		
		ZOrderCommand com = new ZOrderCommand( (GElement)selected.getModel(),
				(GSedML)selected.getParent().getModel(),getZOrderType());
		execute(com);
	}
}
