package org.sedml.jlibsedml.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.swt.SWT;
import org.sedml.jlibsedml.editor.configdialogs.BaseConfigDialog;
import org.sedml.jlibsedml.editor.gmodel.Connection;
import org.sedml.jlibsedml.editor.gmodel.ConnectionManager;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.PropertyChangeNames;
import org.sedml.jlibsedml.editor.gmodelcommands.ConnectionCreateCommand;
import org.sedml.jlibsedml.editor.gmodelcommands.ConnectionReconnectCommand;

public class GElementEditPart extends AbstractGraphicalEditPart implements
		NodeEditPart, PropertyChangeListener {
	private ConnectionAnchor anchor;
	private GElementFigure fig;
	private ConnectionManager connManager=new ConnectionManager();



	/**
	 * Upon activation, attach to the model element as a property change
	 * listener.
	 */
	public void activate() {
		if (!isActive()) {
			super.activate();
			((GElement) getModel()).addPropertyChangeListener(this);
		}
	}

	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			((GElement) getModel()).removePropertyChangeListener(this);
		}
	}

	@Override
	protected IFigure createFigure() {
		fig = new GElementFigure(this);
		return fig;
	}

	@Override
	protected void createEditPolicies() {
		
		// allow removal of the associated model element
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new ShapeComponentEditPolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new GraphicalNodeEditPolicy() {
					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#
					 * getConnectionCompleteCommand
					 * (org.eclipse.gef.requests.CreateConnectionRequest)
					 */
					protected Command getConnectionCompleteCommand(
							CreateConnectionRequest request) {
						ConnectionCreateCommand cmd = (ConnectionCreateCommand) request
								.getStartCommand();
						GElement src = (GElement) request.getSourceEditPart()
								.getModel();
						GElement targ = (GElement) getHost().getModel();
						if(connManager.canConnect(src, targ,false,false)){
							cmd.setTarget(targ);
							return cmd;
						}

						return null;
					}

					

					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#
					 * getConnectionCreateCommand
					 * (org.eclipse.gef.requests.CreateConnectionRequest)
					 */
					protected Command getConnectionCreateCommand(
							CreateConnectionRequest request) {
						GElement source = (GElement) getHost().getModel();

						ConnectionCreateCommand cmd = new ConnectionCreateCommand(
								source, SWT.LINE_SOLID);
						request.setStartCommand(cmd);
						return cmd;
					}

					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#
					 * getReconnectSourceCommand
					 * (org.eclipse.gef.requests.ReconnectRequest)
					 */
					protected Command getReconnectSourceCommand(
							ReconnectRequest request) {
						Connection conn = (Connection) request
								.getConnectionEditPart().getModel();
						GElement newSource = (GElement) getHost().getModel();
						ConnectionReconnectCommand cmd = new ConnectionReconnectCommand(
								conn);
						cmd.setNewSource(newSource);
						
						if(connManager.canConnect(newSource, conn.getTarget(),true,false)){
							return cmd;
						}
						return null;
						
					}

					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#
					 * getReconnectTargetCommand
					 * (org.eclipse.gef.requests.ReconnectRequest)
					 */
					protected Command getReconnectTargetCommand(
							ReconnectRequest request) {
						Connection conn = (Connection) request
								.getConnectionEditPart().getModel();
						GElement newTarget = (GElement) getHost().getModel();
						ConnectionReconnectCommand cmd = new ConnectionReconnectCommand(
								conn);
						cmd.setNewTarget(newTarget);
						if(connManager.canConnect(conn.getSource(), newTarget,false,true)){
							return cmd;
						}
						return null;
					}
				});

	}

	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if (PropertyChangeNames.SIZE_EVENT.equals(prop)
				|| PropertyChangeNames.LOCATION_EVENT.equals(prop)) {
			refreshVisuals();
		} else if (PropertyChangeNames.SOURCE_CONNECTIONS_PROP.equals(prop)) {
			refreshSourceConnections();
		} else if (PropertyChangeNames.TARGET_CONNECTIONS_PROP.equals(prop)) {
			refreshTargetConnections();
		} else if (PropertyChangeNames.PROPERTY_EVENT.equals(prop)) {
			refreshVisuals();
		}else if (PropertyChangeNames.HIDE_ELEMENT_EVENT.equals(prop)) {
			getFigure().setVisible(false);
			
		}else if (PropertyChangeNames.SHOW_ELEMENT_EVENT.equals(prop)) {
			getFigure().setVisible(true);
		}

	}

	private void refreshFigureLabels() {
		fig.setDisplay1(getCastedModel().getDisplay1());
		fig.setDisplay2(getCastedModel().getDisplay2());
		fig.updateColour();
		
	}

	public void performRequest(Request req) {
		SelectionRequest sr = null;
		if (req.getType().equals(RequestConstants.REQ_OPEN)) {
			sr = (SelectionRequest) req;
			if (sr != null) {
				BaseConfigDialog bcd = ConfigDialogFactory.createDialog(getCastedModel());
				if(bcd!=null){
					bcd.open();
				}
			}

		}

	}

	protected ConnectionAnchor getConnectionAnchor() {
		if (anchor == null) {
			if (getModel() instanceof GElement)
				anchor = new ChopboxAnchor(getFigure());

			else
				// if Shapes gets extended the conditions above must be updated
				throw new IllegalArgumentException("unexpected model");
		}
		return anchor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections
	 * ()
	 */
	protected List getModelSourceConnections() {
		return getCastedModel().getSrcConnections();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections
	 * ()
	 */
	protected List getModelTargetConnections() {
		return getCastedModel().getTargetConnections();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef
	 * .ConnectionEditPart)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connection) {
		return getConnectionAnchor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef
	 * .Request)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return getConnectionAnchor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef
	 * .ConnectionEditPart)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connection) {
		return getConnectionAnchor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef
	 * .Request)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return getConnectionAnchor();
	}

	protected void refreshVisuals() {
		// notify parent container of changed position & location
		// if this line is removed, the XYLayoutManager used by the parent
		// container
		// (the Figure of the ShapesDiagramEditPart), will not know the bounds
		// of this figure
		// and will not draw it correctly.
		Rectangle bounds = new Rectangle(getCastedModel().getLocation().getX(),
				getCastedModel().getLocation().getY(), getCastedModel()
						.getSize().getWidth(), getCastedModel().getSize()
						.getHeight());
		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), bounds);
		refreshFigureLabels();
		
	}

	public GElement getCastedModel() {
		// TODO Auto-generated method stub
		return (GElement) getModel();
	}

}
