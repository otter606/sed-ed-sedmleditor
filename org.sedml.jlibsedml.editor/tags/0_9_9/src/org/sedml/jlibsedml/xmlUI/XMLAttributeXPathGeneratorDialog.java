package org.sedml.jlibsedml.xmlUI;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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
import org.jdom.Attribute;
import org.jdom.Document;
import org.sedml.jlibsedml.xmlutils.AttributeIdentifiedBySelectedAttributePolicy;

public class XMLAttributeXPathGeneratorDialog extends BaseXMLDialog {

	public XMLAttributeXPathGeneratorDialog(Shell shell, Document XML) {
		super(shell, XML);
		// TODO Auto-generated constructor stub
	}

	class GetAttributeXPathListener implements ISelectionChangedListener {
		String currXPath;
		private AttributeIdentifiedBySelectedAttributePolicy policy;

		public void selectionChanged(SelectionChangedEvent event) {
			IStructuredSelection sel = (IStructuredSelection) event
					.getSelection();
			if (sel.isEmpty()) {
				return;
			}
			Object ob = sel.getFirstElement();
			if (ob instanceof Attribute) {
				policy = new AttributeIdentifiedBySelectedAttributePolicy(xmlDoc,(Attribute)ob);
				if(sel.toList().size()==2 && sel.toList().get(1) instanceof Attribute){
					currXPath = policy.getXPathForAttribute((Attribute)sel.toList().get(1));
				}
				
				System.err.println(currXPath);
			}

		}

		String getCurrXPath() {
			return currXPath;
		}

	}

	protected Control createDialogArea(Composite parent) {

		final XMLViewer viewer = new XMLViewer();
	
		final GetAttributeXPathListener listener = new GetAttributeXPathListener();
		getShell().setText("XML element chooser");
		Composite child = (Composite) super.createDialogArea(parent);
		child.setLayout(new GridLayout());
		Tree tree = createTree(child,SWT.READ_ONLY | SWT.MULTI);
		createDescriptionLabel(new AttributeIdentifiedBySelectedAttributePolicy(null,null), child);
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
	
	

	

}
