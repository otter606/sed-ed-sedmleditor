package org.sedml.jlibsedml.editor.configdialogs;

import java.util.Collections;
import java.util.List;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.sedml.jlibsedml.editor.gmodel.GOutput;

/**
 * Dialog for editing properties of a SEDML model element.
 * @author radams
 *
 */
public class OutputConfigDialog extends BaseConfigDialog {
	private GOutput outPut;
	private String oldID, oldName;

	public OutputConfigDialog(Shell shell, GOutput gOut) {
		super(shell);
		
		this.outPut=gOut;
		this.oldID=gOut.getId();
		this.oldName=gOut.getName();
		
		// TODO Auto-generated constructor stub
	}
	protected Control createDialogArea(Composite parent) {
		Composite child =(Composite)super.createDialogArea(parent);
		getShell().setText("Add properties to the output");
		//Composite child = new Composite(parent,SWT.NULL);
		GridLayout gl = createGridLayout(3);
		child.setLayout(gl);
		new IDNameGroup(child, outPut);
		setHelp(parent);
		setHelpAvailable(true);
		return child;
		
	}
	
	private void setHelp(Composite parent) {
		//setHelpAvailable(true);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,
				PLUGINID + ".output");

	}
	
	
	  GridLayout createGridLayout(int i) {
		 GridLayout gl=new GridLayout();
			gl.numColumns=i;
			gl.verticalSpacing=15;
			return gl;
	}
	void resetOldValues() {
		 outPut.setId(oldID);
		 outPut.setName(oldName);	
	}
	@Override
	List<VerifyObject> createVerifyObjects() {
		return Collections.EMPTY_LIST;
	}
	
	
		
		

	
	

}
