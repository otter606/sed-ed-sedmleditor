package org.sedml.jlibsedml.editor.configdialogs;

import java.util.Collections;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.sedml.jlibsedml.editor.gmodel.GTask;

/**
 * Dialog for editing properties of a SEDML model element.
 * @author radams
 *
 */
public class TaskConfigDialog extends BaseConfigDialog {
	private GTask t;
	private String oldID, oldName;
	public TaskConfigDialog(Shell shell, GTask task) {
		super(shell);
		this.t=task;
		this.oldID=task.getId();
		this.oldName=task.getName();
		
		// TODO Auto-generated constructor stub
	}
	protected Control createDialogArea(Composite parent) {
		Composite child =(Composite)super.createDialogArea(parent);
		getShell().setText("Add properties to task");
		child.setLayout(createGridLayout(3));
		new IDNameGroup(child, t);
		setHelp(parent);
		setHelpAvailable(true);
		return child;
		
	}
	
	private void setHelp(Composite parent) {
		//setHelpAvailable(true);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,
				PLUGINID + ".task");

	}
	
	
	 void resetOldValues() {
		t.setId(oldID);
		t.setName(oldName);	
	}
	@Override
	List<VerifyObject> createVerifyObjects() {
		return Collections.EMPTY_LIST;
	}
	
	
		
		

	
	

}
