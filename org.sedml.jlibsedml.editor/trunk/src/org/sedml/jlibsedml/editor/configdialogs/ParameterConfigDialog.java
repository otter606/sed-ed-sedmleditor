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
import org.sedml.jlibsedml.editor.gmodel.GParameter;

public class ParameterConfigDialog extends BaseConfigDialog {

	private GParameter gParam;
	private String oldName;
	private double oldValue;
	private Text textValue;
	private IDNameGroup nameGroup;

	public ParameterConfigDialog(Shell shell, GParameter gParam) {
		super(shell);
		this.gParam = gParam;
		this.oldName = gParam.getName();
		this.oldValue = gParam.getValue();

	}

	protected Control createDialogArea(Composite parent) {
		Composite child = (Composite) super.createDialogArea(parent);
		getShell().setText("Add properties to Parameter");
		// Composite child = new Composite(parent,SWT.NULL);
		child.setLayout(createGridLayout(3));

		nameGroup =new IDNameGroup(child, gParam);
		nameGroup.idText.setEditable(true);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		nameGroup.idText.setLayoutData(gd);
		createValue(child);
		createStatus(child, 3);

		setHelpAvailable(true);
		setInitialisationComplete(true);

		return child;
	}

	private void createValue(Composite child) {
		new Label(child, SWT.NULL).setText("Value: ");
		textValue = new Text(child, SWT.BORDER);

		textValue.setText(Double.toString(gParam.getValue()));

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		textValue.setLayoutData(gd);
		textValue.addModifyListener(new VerifyingModifyListener());
		textValue.addFocusListener(new VerifyingFocusListener());

	}

	@Override
	List<VerifyObject> createVerifyObjects() {
		
		VerifyObject v1 = new VerifyObject(textValue.getText(),
				new DoubleValidator());
		return Arrays.asList(new VerifyObject[] { v1 });

	}

	protected void okPressed() {
		final double TOLERANCE=0.0001;
		final double d = Double.parseDouble(textValue.getText());
		final String name = gParam.getName();
		if( hasChanged(TOLERANCE, d, name)) {
			execute( new ICommand() {
				
				public void undo() {
					resetOldValues();
					}
				
				public void redo() {
					gParam.setValue(d);	
					gParam.setName(name);
					
				}
				
				public void execute() {
					redo();		
				}
				public String getLabel() {
					return "Edit Parameter";
				}
			});
		}
		gParam.setValue(Double.parseDouble(textValue.getText()));
		super.okPressed();

	}

	private boolean hasChanged(final double TOLERANCE, final double d, String name) {
		return Math.abs(d - oldValue)  >TOLERANCE || !oldName.equals(name);
	}

	@Override
	void resetOldValues() {
		gParam.setName(oldName);
		gParam.setValue(oldValue);

	}

}
