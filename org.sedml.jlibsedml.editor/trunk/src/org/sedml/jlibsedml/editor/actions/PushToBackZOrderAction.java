package org.sedml.jlibsedml.editor.actions;

import org.eclipse.ui.IWorkbenchPart;
import org.sedml.jlibsedml.editor.gmodelcommands.ZOrderCommand;
import org.sedml.jlibsedml.editor.gmodelcommands.ZOrderCommand.OrderType;

public class PushToBackZOrderAction extends ZOrderAction {

	public static final String PUSH_TO_BACK_Z_ORDERID = "toBackOrderID";
	public final static String DISPLAY_TEXT="Move to Back";

	public PushToBackZOrderAction(IWorkbenchPart part) {
		super(part);
	}	

	@Override
	protected String getZDisplayText() {
		return DISPLAY_TEXT;
	}

	@Override
	protected String getZId() {
		return PUSH_TO_BACK_Z_ORDERID;
	}

	@Override
	protected OrderType getZOrderType() {
		return ZOrderCommand.OrderType.TO_BACK;
	}
}
