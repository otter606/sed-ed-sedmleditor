package org.sedml.jlibsedml.editor.configdialogs;

public interface ICommand {
	
	public void execute();
	
	public void undo();
	
	public void redo();

	public String getLabel();

}
