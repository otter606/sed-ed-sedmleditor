package org.sedml.jlibsedml.editor.actions;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;
import org.sedml.jlibsedml.editor.GElementEditPart;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.GSedML;
import org.sedml.jlibsedml.editor.gmodel.GVariable;
import org.sedml.jlibsedml.editor.gmodel.Location;
import org.sedml.jlibsedml.editor.gmodelcommands.ConnectionCreateCommand;
import org.sedml.jlibsedml.editor.gmodelcommands.ShapeCreateCommand;

public  class AddVariableAction extends AddChildElementAction{
	
	
	public static final String ADD_VARIABLE_ID = "AddVariableID";

	public AddVariableAction(IWorkbenchPart part) {
		super(part);
		
		
	}
	
	protected void init (){
		setId(ADD_VARIABLE_ID);
		setActionDefinitionId("Add Variable");
		setText("Add Variable");
		setToolTipText("Adds a Variable element to this element");
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
		return new GVariable();
	}
	
	public void run() {
		EditPart selected = (EditPart) getSelectedObjects().get(0);
		Location currLoc = getModel().getLocation();
		CompoundCommand cc = new CompoundCommand("Add Variable");
		GVariable gvar = new GVariable();
		ShapeCreateCommand scc = new ShapeCreateCommand(gvar, (GSedML)selected.getParent().getModel(),
				  new Rectangle(currLoc.getX()+40, currLoc.getY()+40, GElement.DEFAULT_SIZE.getWidth(), 
						    GElement.DEFAULT_SIZE.getHeight()),false);
		ConnectionCreateCommand ccc = new ConnectionCreateCommand(getModel(), SWT.LINE_SOLID);
		ccc.setTarget(gvar);
		
		cc.add(scc);
		cc.add(ccc);
		execute(cc);
		
	}
}
