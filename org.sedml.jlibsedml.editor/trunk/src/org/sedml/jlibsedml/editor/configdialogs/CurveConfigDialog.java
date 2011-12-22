package org.sedml.jlibsedml.editor.configdialogs;

import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.sedml.jlibsedml.editor.gmodel.GCurve;

public class CurveConfigDialog extends BaseConfigDialog {
	
	private GCurve gc;
	
	private String oldName;
	private boolean oldLogx, oldLogy;
	private Button xbutt, ybutt;

	public CurveConfigDialog(Shell shell, GCurve gc) {
		super(shell);
		this.gc=gc;
	    this.oldName=gc.getName();
	    this.oldLogx=gc.isLogx();
	    this.oldLogy=gc.isLogy();
	   
		
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite child =(Composite)super.createDialogArea(parent);
		getShell().setText("Add properties to Curve");
		//Composite child = new Composite(parent,SWT.NULL);
	
		child.setLayout(createGridLayout(3));
		
		new IDNameGroup(child, gc);
		
		createStatus(child, 3);
		createLogs(child);
		setHelpAvailable(true);
		setInitialisationComplete(true);
		
		return child;
	}
	

	

	private void createLogs(Composite child) {
		xbutt = new Button(child,SWT.CHECK);
		xbutt.setText("log X");
		xbutt.setSelection(gc.isLogx());
		
		ybutt = new Button(child,SWT.CHECK);
		ybutt.setText("log Y");
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		ybutt.setLayoutData(gd);
		ybutt.setSelection(gc.isLogy());
		
	}

	@Override
	List<VerifyObject> createVerifyObjects() {
	
			return Collections.EMPTY_LIST;
		
	}
	
	protected void okPressed() {
		final boolean logXsel = xbutt.getSelection();
		final boolean logYsel = ybutt.getSelection();
		final String name = gc.getName();

		if(logXsel!=oldLogx || logYsel!=oldLogy || !(name.equals(oldName) )) {
			execute ( new ICommand() {
				
				public void undo() {
					resetOldValues();		
				}
				
				public void redo() {
					gc.setLogx(logXsel);
					gc.setLogy(logYsel);
					gc.setName(name);
				}
				
				public void execute() {
					redo();
					
				}

				public String getLabel() {
					return "Edit curve";
				}
			});
		}
	
		super.okPressed();
		
	}

	@Override
	void resetOldValues() {
		gc.setName(oldName);
		gc.setLogx(oldLogx);
		gc.setLogy(oldLogy);
		

	}

}
