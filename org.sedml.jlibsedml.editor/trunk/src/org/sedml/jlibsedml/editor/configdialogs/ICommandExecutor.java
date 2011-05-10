package org.sedml.jlibsedml.editor.configdialogs;

public interface ICommandExecutor {
	
	/**
	 * Executes a command on a command stack, which is application specific.
	 * @param command A non-null {@link ICommand}
	 */
	void executeOnCommandStack (ICommand command);

}
