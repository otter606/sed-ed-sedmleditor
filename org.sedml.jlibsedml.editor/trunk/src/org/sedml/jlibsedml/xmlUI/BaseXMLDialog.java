package org.sedml.jlibsedml.xmlUI;

import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.jdom.Document;
import org.sedml.jlibsedml.xmlutils.ISelectionChangedPolicy;

public abstract class BaseXMLDialog extends TrayDialog {
	
	protected Document xmlDoc;

	protected String selectedXPathFromViewer = "";

	
	public  BaseXMLDialog(Shell shell, Document XML) {
		super(shell);
		this.xmlDoc = XML;

		setShellStyle(SWT.CLOSE | SWT.MAX | SWT.TITLE | SWT.BORDER
				| SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());

		// TODO Auto-generated constructor stub
	}
	
	 void createDescriptionLabel(ISelectionChangedPolicy policy,
			Composite child) {
		final Label l = new Label(child, SWT.BORDER | SWT.READ_ONLY);
		l.setText(policy.getDescription());
		GridData gd3 = new GridData(SWT.FILL, SWT.FILL, true, false);
		l.setLayoutData(gd3);
	}
	 
		 Tree createTree(Composite child, int style) {
			Tree tree = new Tree(child, style);
			GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
			tree.setLayoutData(gd);
			return tree;
		}

	
	public String getSelectedXPathFromViewer() {
		return selectedXPathFromViewer;
	}
	
	/**
	 * Subclasses can optionally implement. This default implementation returns <code>null</code>.
	 * @return
	 */
	 ISelectionChangedPolicy createPolicy (){ return null;}

}
