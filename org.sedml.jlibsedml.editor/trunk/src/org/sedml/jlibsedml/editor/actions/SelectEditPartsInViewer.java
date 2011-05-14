package org.sedml.jlibsedml.editor.actions;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;

/**
 * Helper class for actions that involve altering the selection of map objects
 * in the viewer.
 * 
 * @author Richard Adams
 * @see PasteAction
 * @see SelectAllOfSameObjectTypeAction
 */
class SelectObjectsInViewerHelper  <N> {
	private EditPart selectedItem ;
	
	/**
	 * Takes an {@link EditPart} from the current selection.
	 * @param selectedItem Cannot be <code>null</code>
	 */
	public SelectObjectsInViewerHelper(EditPart selectedItem) {
		super();
		if(selectedItem ==null){
			throw new IllegalArgumentException("Cannot be null");
		}
		this.selectedItem = selectedItem;
	}
	/* (non-Javadoc)
	 * @see org.pathwayeditor.editor.actions.IEditPartSelector#selectObjectsInViewer(java.util.List)
	 */
	public void selectObjectsInViewer (List<N> objectsToSelect) {
		EditPartViewer viewer = selectedItem.getViewer();
		viewer.deselectAll();
		viewer.flush();
		for (N imo: objectsToSelect) {
			EditPart editpart = (EditPart)viewer.getEditPartRegistry().get(imo);
	
				viewer.appendSelection( editpart);
				if(objectsToSelect.size() ==1){
					viewer.reveal(editpart);
				}
			
		}
		
	}

}

