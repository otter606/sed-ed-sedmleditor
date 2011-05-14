package org.sedml.jlibsedml.editor.product;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * Handler for the 'Switch workspace' file menu item.
 * @author Richard Adams
 *
 */
public class SwitchWorkspaceHandler extends AbstractHandler implements IHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
	
		        SwitchWorkspaceDialog pwd = new SwitchWorkspaceDialog(true, null); 
		
		        int pick = pwd.open(); 
		        if (pick == Dialog.CANCEL) 
		        	return null; 
		        MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Switch Workspace", 
		        		                              "SBSI Visual will now restart with the new workspace"); 
		        
		       PlatformUI.getWorkbench().restart();  
		       return null;
	}

}
