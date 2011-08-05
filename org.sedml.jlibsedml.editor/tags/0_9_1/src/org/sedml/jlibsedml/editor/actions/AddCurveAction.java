package org.sedml.jlibsedml.editor.actions;

import org.eclipse.ui.IWorkbenchPart;
import org.sedml.jlibsedml.editor.GElementEditPart;
import org.sedml.jlibsedml.editor.gmodel.GCurve;
import org.sedml.jlibsedml.editor.gmodel.GElement;

public  class AddCurveAction extends AddChildElementAction{
	
	
	public static final String ADD_CURVE_ID = "AddCurveID";

	public AddCurveAction(IWorkbenchPart part) {
		super(part);
		
		
	}
	
	protected void init (){
		setId(ADD_CURVE_ID);
		setActionDefinitionId("Add Curve");
		setText("Add Curve");
		setToolTipText("Adds a Curve element");
		setEnabled(true);
	}
	
	

	
	GElement getModel(){
		GElement gel =((GElementEditPart)getSelectedObjects().get(0)).getCastedModel();
		 if (gel.isPlot2D()){
			 return gel;
		 }else {
			 return null;
		 }
	}

	@Override
	GElement createModelElement() {
		return new GCurve();
	}
	

	
}
