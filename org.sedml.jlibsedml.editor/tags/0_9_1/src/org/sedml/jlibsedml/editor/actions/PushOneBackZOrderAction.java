package org.sedml.jlibsedml.editor.actions;

import org.eclipse.ui.IWorkbenchPart;
import org.sedml.jlibsedml.editor.gmodelcommands.ZOrderCommand;
import org.sedml.jlibsedml.editor.gmodelcommands.ZOrderCommand.OrderType;

public class PushOneBackZOrderAction extends ZOrderAction {

	public static final String PUSH_BACK_Z_ORDERID = "oneBackZOrderID";
	public static final String DISPLAY_TEXT="Move One Back";

	public PushOneBackZOrderAction(IWorkbenchPart part) {
		super(part);
	}

	@Override
	protected String getZDisplayText() {
		return DISPLAY_TEXT;
	}

	@Override
	protected String getZId() {
		return PUSH_BACK_Z_ORDERID;
	}

	@Override
	protected OrderType getZOrderType() {
		return ZOrderCommand.OrderType.BACKWARD;
	}

	

	
}
