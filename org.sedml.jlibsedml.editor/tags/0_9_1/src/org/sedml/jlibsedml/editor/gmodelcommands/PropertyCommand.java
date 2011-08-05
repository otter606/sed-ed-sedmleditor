package org.sedml.jlibsedml.editor.gmodelcommands;

import org.eclipse.gef.commands.Command;
import org.sedml.jlibsedml.editor.configdialogs.ICommand;


public class PropertyCommand extends Command {
	
	public PropertyCommand(ICommand comm) {
		super();
		this.comm = comm;
		setLabel(comm.getLabel());
	}

	private ICommand comm;
	public void execute (){
		comm.execute();
	}
	public void undo (){
		comm.undo();
	}
	
	public void redo (){
		comm.redo();
	}
	
	

}
