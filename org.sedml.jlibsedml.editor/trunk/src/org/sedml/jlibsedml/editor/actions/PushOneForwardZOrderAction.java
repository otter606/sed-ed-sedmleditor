package org.sedml.jlibsedml.editor.actions;

import org.eclipse.ui.IWorkbenchPart;
import org.sedml.jlibsedml.editor.gmodelcommands.ZOrderCommand;
import org.sedml.jlibsedml.editor.gmodelcommands.ZOrderCommand.OrderType;


public class PushOneForwardZOrderAction extends ZOrderAction {
	

	public static final String PUSH_FORWARD_Z_ORDERID = "oneForwardZOrderID";
	public static final String DISPLAY_TEXT="Move One Forward";

	public PushOneForwardZOrderAction(IWorkbenchPart part) {
		super(part);
	}

	@Override
	protected String getZDisplayText() {
		return DISPLAY_TEXT;
	}

	@Override
	protected String getZId() {
		return PUSH_FORWARD_Z_ORDERID;
	}

	@Override
	protected OrderType getZOrderType() {
		return ZOrderCommand.OrderType.FORWARD;
	}
}
