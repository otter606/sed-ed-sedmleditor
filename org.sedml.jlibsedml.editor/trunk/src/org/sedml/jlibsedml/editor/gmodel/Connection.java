/*******************************************************************************
 * Copyright (c) 2004, 2005 Elias Volanakis and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Elias Volanakis - initial API and implementation
 *******************************************************************************/
package org.sedml.jlibsedml.editor.gmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

/**
 * A connection between two distinct shapes. This is not a SEDML construct
 *  but represents a grphical connection ( a reference between two entities ).
 * 
 * @author Elias Volanakis
 */
public class Connection  {
	/**
	 * Used for indicating that a Connection with solid line style should be
	 * created.
	 * 
	 * @see org.eclipse.gef.examples.shapes.parts.ShapeEditPart#createEditPolicies()
	 */
	
	private static final IPropertyDescriptor[] descriptors = new IPropertyDescriptor[1];

	private static final long serialVersionUID = 1;

	/** True, if the connection is attached to its endpoints. */
	private boolean isConnected;

	/** Connection's source endpoint. */
	private GElement source;
	/** Connection's target endpoint. */
	private GElement target;

	
	private PropertyChangeSupport propChSupport;
	/**
	 * Create a (solid) connection between two distinct shapes.
	 * 
	 * @param source
	 *            a source endpoint for this connection (non null)
	 * @param target
	 *            a target endpoint for this connection (non null)
	 * @throws IllegalArgumentException
	 *             if any of the parameters are null or source == target
	 * @see #setLineStyle(int)
	 */
	public Connection(GElement source, GElement target) {
		if(source==null||target==null){
			throw new IllegalArgumentException("arguments null");
		}
		propChSupport = new PropertyChangeSupport(this);
		reconnect(source, target);
		
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener){
		propChSupport.addPropertyChangeListener(listener);
	}
	
	
	public void firePropertyChange(String property,Object oldValue,Object newValue){
		propChSupport.firePropertyChange(property, oldValue, newValue);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propChSupport.removePropertyChangeListener(listener);
	}

	/**
	 * Disconnect this connection from the shapes it is attached to.
	 */
	public void disconnect() {
		if (isConnected) {
			source.removeConnection(this);
			target.removeConnection(this);
			isConnected = false;
		}
	}

	

	

	

	/**
	 * Returns the source endpoint of this connection.
	 * 
	 * @return a non-null Shape instance
	 */
	public GElement getSource() {
		return source;
	}

	/**
	 * Returns the target endpoint of this connection.
	 * 
	 * @return a non-null Shape instance
	 */
	public GElement getTarget() {
		return target;
	}

	/**
	 * Reconnect this connection. The connection will reconnect with the shapes
	 * it was previously attached to.
	 */
	public void reconnect() {
		if (!isConnected) {
			source.addConnection(this);
			target.addConnection(this);
			isConnected = true;
		}
	}

	/**
	 * Reconnect to a different source and/or target shape. The connection will
	 * disconnect from its current attachments and reconnect to the new source
	 * and target.
	 * 
	 * @param newSource
	 *            a new source endpoint for this connection (non null)
	 * @param newTarget
	 *            a new target endpoint for this connection (non null)
	 * @throws IllegalArgumentException
	 *             if any of the paramers are null or newSource == newTarget
	 */
	public void reconnect(GElement newSource, GElement newTarget) {
		if (newSource == null || newTarget == null || newSource == newTarget) {
			throw new IllegalArgumentException();
		}
		disconnect();
		this.source = newSource;
		this.target = newTarget;
		reconnect();
	}

	

	

}