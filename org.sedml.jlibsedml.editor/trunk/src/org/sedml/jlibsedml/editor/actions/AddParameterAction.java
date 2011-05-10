package org.sedml.jlibsedml.editor.actions;

import org.eclipse.ui.IWorkbenchPart;
import org.sedml.jlibsedml.editor.GElementEditPart;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.GParameter;

public  class AddParameterAction extends AddChildElementAction{
	
	
	public static final String ADD_PARAMETER_ID = "AddParameterID";

	public AddParameterAction(IWorkbenchPart part) {
		super(part);
		
		
	}
	
	protected void init (){
		setId(ADD_PARAMETER_ID);
		setActionDefinitionId("Add Parameter");
		setText("Add Parameter");
		setToolTipText("Adds a Parameter element");
		setEnabled(true);
	}
	
	

	
	GElement getModel(){
		GElement gel =((GElementEditPart)getSelectedObjects().get(0)).getCastedModel();
		 if (gel.isDataGenerator()){
			 return gel;
		 }else if (gel.isComputeChange()){
			 return gel;
		 }else {
			 return null;
		 }
	}

	@Override
	GElement createModelElement() {
		return new GParameter();
	}
	

	
}
