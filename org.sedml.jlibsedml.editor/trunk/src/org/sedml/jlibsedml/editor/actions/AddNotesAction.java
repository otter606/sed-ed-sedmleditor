package org.sedml.jlibsedml.editor.actions;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.sedml.jlibsedml.editor.GElementEditPart;
import org.sedml.jlibsedml.editor.configdialogs.AddNoteDialog;

public class AddNotesAction extends SelectionAction {

	public static final String ADD_NOTE_ID = "add_note";

	protected void init (){
		setId(ADD_NOTE_ID);
		setActionDefinitionId("Add Note");
		setText("Add Note");
		setToolTipText("Annotate this element with HTML");
		setEnabled(true);
	}
	public AddNotesAction(IWorkbenchPart part) {
		super(part);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Enabled for any selected single GElement
	 */
	@Override
	protected boolean calculateEnabled() {
		if (getSelectedObjects().size() == 1 && (getSelectedObjects().get(0) instanceof GElementEditPart) )
			return true;
		return false;
	}
	public void run() {
		GElementEditPart selected = (GElementEditPart) getSelectedObjects().get(0);
		AddNoteDialog noteDialog = new AddNoteDialog(Display.getCurrent().getActiveShell(),
				      selected.getCastedModel() );
		noteDialog.open();
		
	}

}
