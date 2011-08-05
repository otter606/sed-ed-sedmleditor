package org.sedml.jlibsedml.editor;

import org.eclipse.gef.GraphicalViewer;
import org.sedml.jlibsedml.editor.gmodel.GSedML;

/**
 * Convenience interface for susbset of editor functionality, to enable testing.
 * @author radams
 *
 */
public interface IViewChanger {
	
	/**
	* Flush current graphical information to the UI.
	*/
	public void flushViewer();
	
	public GraphicalViewer getGraphicalViewer();

	public GSedML getModel();

}
