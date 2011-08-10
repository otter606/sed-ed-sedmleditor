package org.sedml.jlibsedml.xmlUI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.jdom.Document;
import org.sedml.jlibsedml.xmlutils.ElementIdentifiedBySelectedAttributePolicy;
import org.sedml.jlibsedml.xmlutils.ISelectionChangedPolicy;

public class XMLElementXPathGeneratorDialog extends BaseXMLDialog {

	public XMLElementXPathGeneratorDialog(Shell shell, Document XML) {
		super(shell, XML);

	}

	protected Control createDialogArea(Composite parent) {

		
		final XMLViewer viewer = new XMLViewer();
		ISelectionChangedPolicy policy= createPolicy();
		final GetElementFromAttributeListListener listener = new GetElementFromAttributeListListener(
				policy);
		getShell().setText("XML element chooser");
		Composite child = (Composite) super.createDialogArea(parent);
		child.setLayout(new GridLayout());
		Tree tree = createTree(child,SWT.READ_ONLY);
		createDescriptionLabel(policy, child);
		final Text t = new Text(child, SWT.BORDER | SWT.READ_ONLY);
		GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, false);

		viewer.createViewer(tree, xmlDoc,null);

		t.setLayoutData(gd2);
		SelectionListener sl = new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				selectedXPathFromViewer = listener.getCurrXPath() == null ? ""
						: listener.getCurrXPath();
				t.setText(selectedXPathFromViewer);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		};
		viewer.addSelectionChangedListener(listener);
		viewer.addXMLSelectionListener(sl);

		return child;

	}

	ISelectionChangedPolicy createPolicy (){
		ElementIdentifiedBySelectedAttributePolicy policy = new ElementIdentifiedBySelectedAttributePolicy(
				xmlDoc);
		
		return policy;
	}

}
