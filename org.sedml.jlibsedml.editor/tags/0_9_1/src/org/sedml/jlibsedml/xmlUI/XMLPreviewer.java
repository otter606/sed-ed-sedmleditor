package org.sedml.jlibsedml.xmlUI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.jdom.Document;

public class XMLPreviewer extends BaseXMLDialog {

	public XMLPreviewer(Shell parent, Document XML) {
		super(parent, XML);
	
	}
	
	protected Control createDialogArea (Composite parent) {
		final XMLViewer viewer = new XMLViewer();
		getShell().setText("XML element chooser");
		Composite child = (Composite) super.createDialogArea(parent);
		child.setLayout(new GridLayout());
		Tree tree = createTree(child,SWT.READ_ONLY | SWT.MULTI);
		
		viewer.createViewer(tree, xmlDoc);
		
		return child;
	}

}
