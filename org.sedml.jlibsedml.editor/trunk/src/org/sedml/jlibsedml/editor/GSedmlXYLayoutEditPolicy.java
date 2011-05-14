package org.sedml.jlibsedml.editor;

import javax.xml.bind.annotation.XmlElement;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.GSedML;
import org.sedml.jlibsedml.editor.gmodelcommands.ShapeCreateCommand;
import org.sedml.jlibsedml.editor.gmodelcommands.ShapeSetConstraintCommand;

/**
 * Layout policy for top-level parent, i.e., creation and XY layout positioning.
 * @author radams
 *
 */
public class GSedmlXYLayoutEditPolicy extends XYLayoutEditPolicy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see ConstrainedLayoutEditPolicy#createChangeConstraintCommand(
	 * ChangeBoundsRequest, EditPart, Object)
	 */
	protected Command createChangeConstraintCommand(
			ChangeBoundsRequest request, EditPart child, Object constraint) {
		if (child instanceof GElementEditPart
				&& constraint instanceof Rectangle) {
			// return a command that can move and/or resize a Shape
			return new ShapeSetConstraintCommand((GElement) child.getModel(),
					request, (Rectangle) constraint);
		}
		return super.createChangeConstraintCommand(request, child,
				constraint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ConstrainedLayoutEditPolicy#createChangeConstraintCommand(EditPart,
	 * Object)
	 */
	protected Command createChangeConstraintCommand(EditPart child,
			Object constraint) {
		// not used in this example
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see LayoutEditPolicy#getCreateCommand(CreateRequest)
	 */
	@XmlElement
	protected Command getCreateCommand(CreateRequest request) {
		//this returns a class object from a GEF simple factory
		Object childClass = request.getNewObjectType();
		if (isTopLevelChildElement(childClass)) {
			// return a command that can add a Shape to a ShapesDiagram
			return new ShapeCreateCommand((GElement) request.getNewObject(),
					(GSedML) getHost().getModel(),
					(Rectangle) getConstraintFor(request),false);
		}
		return null;
	}

	private boolean isTopLevelChildElement(Object childClass) {
		return GElement.class.isAssignableFrom((Class)childClass);
	}

}
