package org.sedml.jlibsedml.editor;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.ui.PlatformUI;
import org.sedml.jlibsedml.editor.configdialogs.ICommand;
import org.sedml.jlibsedml.editor.configdialogs.ICommandExecutor;
import org.sedml.jlibsedml.editor.gmodelcommands.PropertyCommand;

/**
 * Performs execution of an {@link ICommand} on the GEF command stack
 * @author radams
 *
 */
public class WorkbenchPartExecutor implements ICommandExecutor {

	/**
	 * @
	 */
	public void executeOnCommandStack(ICommand command) {
		CommandStack stack = (CommandStack) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
		                      .getPartService().getActivePart().getAdapter(CommandStack.class);

		stack.execute(new PropertyCommand(command));
	}

}
