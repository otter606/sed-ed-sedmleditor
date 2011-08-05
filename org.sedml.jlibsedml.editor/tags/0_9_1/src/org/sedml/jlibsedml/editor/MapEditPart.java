package org.sedml.jlibsedml.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.swt.SWT;
import org.eclipse.ui.views.properties.IPropertySource;
import org.sedml.jlibsedml.editor.gmodel.GSedML;
import org.sedml.jlibsedml.editor.gmodel.PropertyChangeNames;

public class MapEditPart extends AbstractGraphicalEditPart implements EditPart,PropertyChangeListener {

	/**
	 * Upon activation, attach to the model element as a property change
	 * listener.
	 */
	public void activate() {
		if (!isActive()) {
			super.activate();
			 ((GSedML) getModel()).addPropertyChangeListener(this);
		}
	}

	public Object getAdapter(Class key) {
		if (key == IPropertySource.class) {
			// return new MapDiagramPropertySource(getCastedModel());
		}
		return super.getAdapter(key);
	}

	public boolean isContainer() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		// disallows the removal of this edit part from its parent
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new RootComponentEditPolicy());
		
		// handles constraint changes (e.g. moving and/or resizing) of model
		// elements
		// and creation of new model elements
		installEditPolicy(EditPolicy.LAYOUT_ROLE,
				new GSedmlXYLayoutEditPolicy());
	}
	
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {

		Figure f = new FreeformLayer();
		f.setBorder(new MarginBorder(3));
		f.setLayoutManager(new FreeformLayout());
		f.setBackgroundColor(ColorConstants.white);
		f.setOpaque(false);

		// ////////////////

		// //////////////////
		// set the links antialiasing

		// FreeformLayer ffLayer =
		// (FreeformLayer)getLayer(LayerConstants.PRIMARY_LAYER);

		// Create the default router for the connection layer

		ConnectionLayer connLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);

		connLayer.setAntialias(SWT.ON);

		FreeformLayer primaryLayer = (FreeformLayer) getLayer(LayerConstants.PRIMARY_LAYER);

		// /////////////

		connLayer.setConnectionRouter(new ShortestPathConnectionRouter(f));

		return f;

	}

	protected IFigure getLayer(Object key) {
		return super.getLayer(key);
	}

	/**
	 * Upon deactivation, detach from the model element as a property change
	 * listener.
	 */
	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			((GSedML) getModel()).removePropertyChangeListener(this);
		}
	}

	private GSedML getCastedModel() {
		return (GSedML) getModel();
	}

	/**
	 * Labels and children are considered as child edit parts
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List getModelChildren() {
		
		return getCastedModel().getChildren();
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.beans.PropertyChangeListener#propertyChange(PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();

		if ((PropertyChangeNames.CHILD_ADD_EVENT.equals(prop) ||
				PropertyChangeNames.CHILD_PASTE_EVENT.equals(prop) |
				PropertyChangeNames.CHILD_REMOVE_EVENT.equals(prop))) {
			refreshChildren();
		}
	//	} 
	}


	

//	/**
//	 * Gets a list of all links contained in the diagram and top level items.
//	 * Used for image export.
//	 * 
//	 * @return A <code>List</code> of {@link AbstractEditPart}s
//	 */
//	public List<GraphicalEditPart> getPrimaryEditParts() {
//
//		List<GraphicalEditPart> toReturn = new ArrayList<GraphicalEditPart>();
//		toReturn.addAll(getChildren());
//
//		List<ILink> links = getCastedModel().getLinks();
//		for (ILink l : links) {
//			toReturn.add((GraphicalEditPart) getViewer().getEditPartRegistry()
//					.get(l));
//		}
//		return toReturn;
//	}



	

	

	

	public boolean isRoot() {
		return true;
	}

	

}
