package org.sedml.jlibsedml.editor.handlers;

import org.eclipse.ui.IEditorInput;
import org.sedml.jlibsedml.editor.gmodel.GSedML;

public interface ISEDMLProvider {
	
	GSedML getModel ();
	
	IEditorInput getEditorInput();

}
