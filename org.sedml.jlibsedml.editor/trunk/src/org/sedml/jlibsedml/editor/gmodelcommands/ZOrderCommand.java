package org.sedml.jlibsedml.editor.gmodelcommands;

import org.eclipse.gef.commands.Command;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.GSedML;
import org.sedml.jlibsedml.editor.gmodel.PropertyChangeNames;

/**
 * Reorders the z-axis of elements so that an element will be added to the top of the figure stack
 * @author Richard Adams
 *
 */
public class ZOrderCommand extends Command {
	
	public enum OrderType{TO_FRONT, TO_BACK, FORWARD, BACKWARD};
	
	GSedML parent;
	GElement toOrder;
	OrderType type;
	
	
	int oldIndex;
	
	/**
	 * 
	 * @param toOrder
	 * @param type
	 */
	public ZOrderCommand(GElement toOrder, GSedML root, OrderType type) {
		this.toOrder = toOrder;
		this.type = type;
		this.parent=root;
	}
	
	public boolean canExecute () {
		return parent != null;
	}
	
	
	public void execute () {
		redo();
	}

	public void redo() {
	
		if(parentHasOnlyOneChild()) {
			return;
		}
		oldIndex = parent.getChildren().indexOf(toOrder);
		
		if (type.equals(OrderType.TO_FRONT) && toOrderIsntAlreadyAtFront()){
			parent.setChildIndex(toOrder, parent.getChildren().size() -1);
		} else if (type.equals(OrderType.TO_BACK)&& toOrderIsntAlreadyAtBack()){
			parent.setChildIndex(toOrder, 0);
		} else if(type.equals(OrderType.FORWARD) && toOrderIsntAlreadyAtFront()) {
			parent.setChildIndex(toOrder, oldIndex + 1);
		}else if(type.equals(OrderType.BACKWARD) && toOrderIsntAlreadyAtBack()) {
			parent.setChildIndex(toOrder, oldIndex - 1);
		}
		parent.firePropertyChange(PropertyChangeNames.CHILD_ADD_EVENT, null, toOrder);
		
		
	}
	
	public void undo () {
		parent.setChildIndex(toOrder, oldIndex);
		parent.firePropertyChange(PropertyChangeNames.CHILD_ADD_EVENT, null, toOrder);
	}


	private boolean toOrderIsntAlreadyAtBack() {
		return parent.getChildren().indexOf(toOrder) > 0;
	}

	
	private boolean parentHasOnlyOneChild() {
		return parent.getChildren().size() == 1;
	}
	
	private boolean toOrderIsntAlreadyAtFront() {
		return parent.getChildren().indexOf(toOrder) < parent.getChildren().size() -1;
	}
}
