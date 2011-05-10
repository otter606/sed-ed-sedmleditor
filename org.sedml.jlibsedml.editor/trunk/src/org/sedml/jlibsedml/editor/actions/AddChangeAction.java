package org.sedml.jlibsedml.editor.actions;

import org.eclipse.ui.IWorkbenchPart;
import org.sedml.jlibsedml.editor.GElementEditPart;
import org.sedml.jlibsedml.editor.gmodel.GChange;
import org.sedml.jlibsedml.editor.gmodel.GElement;

public  class AddChangeAction extends AddChildElementAction{
	
	
	public static final String ADD_CHANGE_ID = "AddChangeID";

	public AddChangeAction(IWorkbenchPart part) {
		super(part);
		
		
	}
	
	protected void init (){
		setId(ADD_CHANGE_ID);
		setActionDefinitionId("Add Change");
		setText("Add Change");
		setToolTipText("Adds a Change element");
		setEnabled(true);
	}
	
	

	
	GElement getModel(){
		GElement gel =((GElementEditPart)getSelectedObjects().get(0)).getCastedModel();
		 if (gel.isModel()){
			 return gel;
		 }else {
			 return null;
		 }
	}

	@Override
	GElement createModelElement() {
		return new GChange();
	}
	

	
}
