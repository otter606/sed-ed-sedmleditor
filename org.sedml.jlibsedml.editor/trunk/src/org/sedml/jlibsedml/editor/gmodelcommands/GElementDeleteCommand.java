package org.sedml.jlibsedml.editor.gmodelcommands;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.sedml.jlibsedml.editor.gmodel.Connection;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.GSedML;
import org.sedml.jlibsedml.editor.gmodel.PropertyChangeNames;

public class GElementDeleteCommand extends Command {

	private GSedML parent;
	private  GElement child;
	private boolean wasRemoved =false;
	/** Holds a copy of the outgoing connections of child. */
	private List sourceConnections;
	/** Holds a copy of the incoming connections of child. */
	private List targetConnections;
	public GElementDeleteCommand(GSedML parent, GElement child) {
		this.parent=parent;
		this.child=child;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		// store a copy of incoming & outgoing connections before proceeding
		sourceConnections = child.getSrcConnections();
		targetConnections = child.getTargetConnections();
	
		doRemove();
		parent.firePropertyChange(PropertyChangeNames.CHILD_REMOVE_EVENT, null, child);
	}
	
	/**
	 * Reconnects a List of Connections with their previous endpoints.
	 * 
	 * @param connections
	 *            a non-null List of connections
	 */
	private void addConnections(List connections) {
		for (Iterator iter = connections.iterator(); iter.hasNext();) {
			Connection conn = (Connection) iter.next();
			conn.reconnect();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	public void redo() {
		doRemove();
		parent.firePropertyChange(PropertyChangeNames.CHILD_REMOVE_EVENT, null, null);

	}

	private void doRemove() {
		wasRemoved = parent.removeChild(child);
	
		if (wasRemoved) {
			removeConnections(sourceConnections);
			removeConnections(targetConnections);
		}
	}
	
	/**
	 * Disconnects a List of Connections from their endpoints.
	 * 
	 * @param connections
	 *            a non-null List of connections
	 */
	private void removeConnections(List connections) {
		for (Iterator iter = connections.iterator(); iter.hasNext();) {
			Connection conn = (Connection) iter.next();
			conn.disconnect();
		}
	}

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		if(parent.addChild(child)){
			parent.firePropertyChange(PropertyChangeNames.CHILD_ADD_EVENT, null, null);
			addConnections(sourceConnections);
			addConnections(targetConnections);
		}
		
	}

}
