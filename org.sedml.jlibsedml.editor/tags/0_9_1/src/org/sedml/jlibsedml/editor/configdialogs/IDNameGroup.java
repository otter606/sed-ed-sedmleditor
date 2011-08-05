package org.sedml.jlibsedml.editor.configdialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.sedml.jlibsedml.editor.gmodel.GElement;

/**
 * Class for representing id/name identifier widgets which are aassumed
 * to fit into a parent with a 3 column grid layout.
 * This 
 * @author radams
 *
 */
public class IDNameGroup {
	
	public static final String IDLabel="ID";
	public static final String NAME_LABEL="Name";
	 Text nmText=null;
	 Text idText=null;
	
	private GElement gElement;
	public IDNameGroup (Composite parent, GElement gElement) {
		this.gElement=gElement;
		createIDRow(parent);
		createNameRow(parent);
		
	}
	
	// read only - ids are created automatically
	private void createIDRow(Composite child) {
		new Label (child,SWT.NULL).setText(IDLabel);
		idText = new Text(child,SWT.BORDER);
		idText.setEditable(false);
		idText.setText(gElement.getId());
		GridData gd = new GridData(SWT.FILL, SWT.CENTER,true, false);
		gd.minimumWidth=150;
		gd.horizontalSpan=2;

		idText.setLayoutData(gd);
	}
	
	// names are optional in SEDML
	private void createNameRow(Composite child) {
		new Label (child,SWT.NULL).setText(NAME_LABEL);
		nmText = new Text(child,SWT.BORDER);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER,true, false);
		gd.minimumWidth=150;
		gd.horizontalSpan=2;
		nmText.setLayoutData(gd);
		
		nmText.setEditable(true);
		nmText.setText(gElement.getName()==null?"":gElement.getName());
		nmText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				gElement.setName(nmText.getText());
			}
		});
	}

}
