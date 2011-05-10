package org.sedml.jlibsedml.editor.actions;

import org.eclipse.ui.IWorkbenchPart;
import org.sedml.jlibsedml.editor.GElementEditPart;
import org.sedml.jlibsedml.editor.gmodel.GDataset;
import org.sedml.jlibsedml.editor.gmodel.GElement;

public  class AddDatasetAction extends AddChildElementAction{
	
	
	public static final String ADD_Dataset_ID = "AddDataset";

	public AddDatasetAction(IWorkbenchPart part) {
		super(part);
		
		
	}
	
	protected void init (){
		setId(ADD_Dataset_ID);
		setActionDefinitionId("Add Dataset");
		setText("Add Dataset");
		setToolTipText("Adds a Dataset element");
		setEnabled(true);
	}
	
	

	
	GElement getModel(){
		GElement gel =((GElementEditPart)getSelectedObjects().get(0)).getCastedModel();
		 if (gel.isReport()){
			 return gel;
		 }else {
			 return null;
		 }
	}

	@Override
	GElement createModelElement() {
		return new GDataset();
	}
	

	
}
