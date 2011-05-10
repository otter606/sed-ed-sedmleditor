package org.sedml.jlibsedml.editor.actions;

import org.eclipse.ui.IWorkbenchPart;
import org.sedml.jlibsedml.editor.gmodelcommands.ZOrderCommand;
import org.sedml.jlibsedml.editor.gmodelcommands.ZOrderCommand.OrderType;

public class PushToFrontZOrderAction extends ZOrderAction {
	
	
	public static final String PUSH_TO_FRONT_Z_ORDERID = "toFrontZOrderID";
	public final static String DISPLAY_TEXT="Move to Front";

	public PushToFrontZOrderAction(IWorkbenchPart part) {
		super(part);
	}

	protected void init() {
		setId(PUSH_TO_FRONT_Z_ORDERID);
		setText("Move to Front");
		setToolTipText("Move to Front");
		// ISharedImages sharedImages =
		// PlatformUI.getWorkbench().getSharedImages();
		// setImageDescriptor(sharedImages.getImageDescriptor(
		// ISharedImages.IMG_TOOL_COPY));
		// setDisabledImageDescriptor(sharedImages.
		// getImageDescriptor(
		// ISharedImages.IMG_TOOL_COPY_DISABLED));
		setEnabled(false);
	}


	@Override
	protected String getZDisplayText() {
		return DISPLAY_TEXT;
	}

	@Override
	protected String getZId() {
		return PUSH_TO_FRONT_Z_ORDERID; 
	}

	@Override
	protected OrderType getZOrderType() {
		return ZOrderCommand.OrderType.TO_FRONT;
	}
}
