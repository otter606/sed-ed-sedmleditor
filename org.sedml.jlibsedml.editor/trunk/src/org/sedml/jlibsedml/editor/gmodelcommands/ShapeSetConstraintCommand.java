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
package org.sedml.jlibsedml.editor.gmodelcommands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.Location;
import org.sedml.jlibsedml.editor.gmodel.PropertyChangeNames;
import org.sedml.jlibsedml.editor.gmodel.Size;

/**
 * A command to resize and/or move a shape. The command can be undone or redone.
 * 
 * @author Elias Volanakis
 */
public class ShapeSetConstraintCommand extends Command {
	/** Stores the new size and location. */
	private final Rectangle newBounds;
	/** Stores the old size and location. */
	private Rectangle oldBounds;
	/** A request to move/resize an edit part. */
	private final ChangeBoundsRequest request;

	/** Shape to manipulate. */
	private final GElement shape;

	/**
	 * Create a command that can resize and/or move a shape.
	 * 
	 * @param shape
	 *            the shape to manipulate
	 * @param req
	 *            the move and resize request
	 * @param newBounds
	 *            the new size and location
	 * @throws IllegalArgumentException
	 *             if any of the parameters is null
	 */
	public ShapeSetConstraintCommand(GElement shape, ChangeBoundsRequest req,
			Rectangle newBounds) {
		if (shape == null || req == null || newBounds == null) {
			throw new IllegalArgumentException();
		}
		this.shape = shape;
		this.request = req;
		this.newBounds = newBounds.getCopy();
		setLabel("move / resize");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		Object type = request.getType();
		// make sure the Request is of a type we support:
		return (RequestConstants.REQ_MOVE.equals(type)
				|| RequestConstants.REQ_MOVE_CHILDREN.equals(type)
				|| RequestConstants.REQ_RESIZE.equals(type)
				|| RequestConstants.REQ_RESIZE_CHILDREN.equals(type)
				||RequestConstants.REQ_ALIGN_CHILDREN.equals(type));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		oldBounds = new Rectangle(shape.getLocation().getX(), shape.getLocation().getY(),
				shape.getSize().getWidth(),shape.getSize().getHeight());
		redo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	public void redo() {
		shape.setSize( new Size(newBounds.getSize().width,newBounds.getSize().height));
		shape.setLocation(new Location(newBounds.getLocation().x, newBounds.getLocation().y));
		shape.firePropertyChange(PropertyChangeNames.LOCATION_EVENT, null, shape);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		shape.setSize( new Size(oldBounds.getSize().width,oldBounds.getSize().height));
		shape.setLocation(new Location(oldBounds.getLocation().x, oldBounds.getLocation().y));
		shape.firePropertyChange(PropertyChangeNames.LOCATION_EVENT, null, shape);
		
	}
}
