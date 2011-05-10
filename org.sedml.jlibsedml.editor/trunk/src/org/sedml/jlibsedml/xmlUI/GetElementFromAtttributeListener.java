package org.sedml.jlibsedml.xmlUI;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.jdom.Attribute;
import org.jdom.Element;
import org.sedml.jlibsedml.xmlutils.ISelectionChangedPolicy;

class GetElementFromAttributeListListener implements  ISelectionChangedListener {

	GetElementFromAttributeListListener(ISelectionChangedPolicy policy) {
		super();
		this.policy = policy;
	}

	String currXPath;
	private ISelectionChangedPolicy policy;
	
	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection sel = (IStructuredSelection) event
				.getSelection();
		if (sel.isEmpty()) {
			return;
		}
		Object ob = sel.getFirstElement();
		if (ob instanceof Attribute) {
			currXPath=policy.getXPathForAttribute((Attribute)ob);
				System.err.println(currXPath);
				}
			
		 else if (ob instanceof Element) {
			Element el = (Element)ob;
	        currXPath=policy.getXPathForElement(el);
			System.err.println(currXPath);
			
		}

	}
	
	String getCurrXPath(){
		return currXPath;
	}
}
