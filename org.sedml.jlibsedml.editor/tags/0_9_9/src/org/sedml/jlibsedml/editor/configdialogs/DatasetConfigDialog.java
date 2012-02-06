package org.sedml.jlibsedml.editor.configdialogs;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.sedml.jlibsedml.editor.gmodel.GDataset;

public class DatasetConfigDialog extends BaseConfigDialog {
	
	private GDataset gc;
	
	private String oldName, oldLabel, oldId;
	private Text labelText;

	public DatasetConfigDialog(Shell shell, GDataset gc) {
		super(shell);
		this.gc=gc;
	    this.oldName=gc.getName();
	    this.oldId=gc.getId();
	    this.oldLabel=gc.getLabel();
	   
	   
		
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite child =(Composite)super.createDialogArea(parent);
		getShell().setText("Add properties to Curve");
		//Composite child = new Composite(parent,SWT.NULL);
	
		child.setLayout(createGridLayout(3));
		
		new IDNameGroup(child, gc);
		
		createStatus(child, 3);
		createLabel(child);
		setHelpAvailable(true);
		setInitialisationComplete(true);
		
		return child;
	}
	

	



	private void createLabel(Composite child) {
		new Label(child, SWT.NULL).setText("Data label");
		labelText = new Text(child,SWT.BORDER);
		String existingLab="";
		if(gc.getLabel()!=null){
			existingLab =gc.getLabel();
		}
		labelText.setText(existingLab);
		labelText.addFocusListener(new VerifyingFocusListener());
		labelText.addModifyListener(new VerifyingModifyListener());
		GridData gd = new GridData(SWT.FILL, SWT.CENTER,true, false);
		gd.horizontalSpan=2;
		labelText.setLayoutData(gd);
		
	}

	@Override
	List<VerifyObject> createVerifyObjects() {
	
		VerifyObject v2 = new VerifyObject(labelText.getText(), new NonEmptyStringValidator("Label"));
		return Arrays.asList(new VerifyObject[]{v2});
		
	}
	
	protected void okPressed() {
		final String text = labelText.getText();
		final String name = gc.getName();
		final String id = gc.getId();
		if(hasChanged(text)){
			execute (new ICommand() {
				
				public void undo() {
					resetOldValues();
					
				}
				
				public void redo() {
					gc.setLabel(text);
					gc.setName(name);
					gc.setId(id);
					
				}
				
				public void execute() {
					redo();
					
				}
				public String getLabel() {
					return "Edit Dataset";
				}
			});
		}
	
		super.okPressed();
		
	}

	private boolean hasChanged(String text) {
		return ! text.equals(oldLabel) || !(gc.getName().equals(oldName));
	}

	@Override
	void resetOldValues() {
		gc.setName(oldName);
		gc.setLabel(oldLabel);
		gc.setId(oldId);
	}

}
