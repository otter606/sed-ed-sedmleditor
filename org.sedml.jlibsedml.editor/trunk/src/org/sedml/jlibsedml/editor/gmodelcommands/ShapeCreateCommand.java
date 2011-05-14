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

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.GSedML;
import org.sedml.jlibsedml.editor.gmodel.Location;
import org.sedml.jlibsedml.editor.gmodel.PropertyChangeNames;
import org.sedml.jlibsedml.editor.gmodel.Size;

/**
 * A command to add a Shape to a ShapeDiagram. The command can be undone or
 * redone.
 * 
 * @author Elias Volanakis
 */
public class ShapeCreateCommand extends Command {

	/** The new shape. */
	private GElement newShape;
	/** ShapeDiagram to add to. */
	private final GSedML parent;
	/** The bounds of the new Shape. */
	private Rectangle bounds;
	private boolean isPaste;

	/**
	 * Create a command that will add a new Shape to a ShapesDiagram.
	 * 
	 * @param newShape
	 *            the new Shape that is to be added
	 * @param parent
	 *            the ShapesDiagram that will hold the new element
	 * @param bounds
	 *            the bounds of the new shape; the size can be (-1, -1) if not
	 *            known
	 * @paste <code>boolean</code> if command is a paste operation rather than
	 *   a new creation operation
	 * @throws IllegalArgumentException
	 *             if any parameter is null, or the request does not provide a
	 *             new Shape instance
	 */
	public ShapeCreateCommand(GElement newShape, GSedML parent,
			Rectangle bounds, boolean isPaste) {
		this.newShape = newShape;
		this.parent = parent;
		this.bounds = bounds;
		this.isPaste=isPaste;
		setLabel("shape creation");
	}

	/**
	 * Can execute if all the necessary information has been provided.
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		return newShape != null && parent != null && bounds != null;
	}

	/*
	 * Adds child and sets location. Fires either a CHILD_PASTE_EVENT or a CHILD_ADD_EVENT
	 *  depending on the boolean passed to the constructor.
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		newShape.setLocation(new Location(bounds.getLocation().x,bounds.getLocation().y) );
		Dimension size = bounds.getSize();
		if (size.width > 0 && size.height > 0)
			newShape.setSize(new Size(size.width,size.height));
		parent.firePropertyChange(PropertyChangeNames.LOCATION_EVENT, null, size);
		parent.firePropertyChange(PropertyChangeNames.SIZE_EVENT, null, size);
		redo();
		if(!isPaste)
		 parent.firePropertyChange(PropertyChangeNames.CHILD_ADD_EVENT, null, newShape);
		else
		 parent.firePropertyChange(PropertyChangeNames.CHILD_PASTE_EVENT, null, newShape);
			
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	public void redo() {
		parent.addChild(newShape);
		newShape.setId(parent.generateIDForChildElement());
		// with null shape will not trigger dialog
		parent.firePropertyChange(PropertyChangeNames.CHILD_ADD_EVENT, null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		parent.removeChild(newShape);
		parent.firePropertyChange(PropertyChangeNames.CHILD_REMOVE_EVENT, null, newShape);
	}

}