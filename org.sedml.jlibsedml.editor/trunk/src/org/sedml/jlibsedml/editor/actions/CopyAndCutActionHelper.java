package org.sedml.jlibsedml.editor.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.sedml.jlibsedml.editor.GElementEditPart;
import org.sedml.jlibsedml.editor.gmodel.Connection;
import org.sedml.jlibsedml.editor.gmodel.GElement;

/**
 * Helper for Cut and Copy Actions that makes a copy of all selected objects
 * @author radams
 *
 */
class CopyAndCutActionHelper {
	
	/**
	 * This method is intended as a helper to the Cut / Copy actions and
	 *  therefore assumes that enablement calculations have been performed 
	 *  correctly.
	 * @param selectedObjects The selected editparts
	 * @return A <code>List</code> of copied elements
	 */
	List<GElement> getCopiesOfSelection(List selectedObjects) {
		List<GElement> toCopy = new ArrayList<GElement>();
		for (Iterator it = selectedObjects.iterator(); it.hasNext();) {
			Object o = it.next();
			if(o instanceof GElementEditPart){
				toCopy.add(((GElementEditPart)o).getCastedModel());
			}
		}
		
		
	
		Map<GElement, GElement> orig2Copy = new HashMap<GElement, GElement>();
		List<GElement> copied = new ArrayList<GElement>();
		for (GElement el: toCopy){
			GElement copy = el.getCopy();
			copied.add(copy);
			orig2Copy.put(el, copy);
		}
		
		for (GElement el: toCopy){
			List<Connection> srces = el.getSrcConnections();
			for (Connection c: srces){
				if(toCopy.contains(c.getTarget())){
					new Connection(orig2Copy.get(c.getSource()),orig2Copy.get(c.getTarget()) );	
				}
			}
		}
		return copied;
	}

}
