package org.sedml.jlibsedml.editor;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.sedml.jlibsedml.editor.gmodel.Connection;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.GSedML;


public class SEDMLEditPartFactory implements EditPartFactory {

	/**
	 * FRamework edit part creation factory. WRaps model objects in a  graphics conversion
	 * wrapper
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		
		// we start off with a BaseEditPart as we need to add IGraphics functionality
		// later on.
		EditPart rc = null;
		if (model instanceof GSedML) {
			rc =  new MapEditPart();
	
			
		} else if ( model instanceof GElement){
			rc = new GElementEditPart();
		}else if (model instanceof Connection) {
			rc= new ConnectionEditPart();
		}
		if(rc!=null)
			rc.setModel(model);
		return rc;
	}

}
