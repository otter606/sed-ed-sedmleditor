package org.sedml.jlibsedml.xmlUI;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Tree;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Text;
import org.jdom.filter.ElementFilter;
import org.sedml.jlibsedml.xmlutils.XMLUtils;

public class XMLViewer {
	


	private Tree tree;
	private TreeViewer viewer;
	
	
	
	public void createViewer (Tree tree, final Document root) {
	this.tree=tree;
	this.viewer = new TreeViewer(tree);
	final XMLUtils utils = new XMLUtils();

	
	
	viewer.setContentProvider(new ITreeContentProvider() {

		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof Element) {
				List els = ((Element) parentElement)
						.getContent(new ElementFilter());
				List rc = new ArrayList(els);
				rc.addAll(((Element) parentElement).getAttributes());
				
				return rc.toArray();
			}else   {
				return new Object[0];
			}

		}

		public Object getParent(Object element) {
			if (element instanceof Element) {
				return ((Element) element).getParentElement();
			} else if (element instanceof Attribute) {
				return ((Attribute) element).getParent();
			}  else {
				return null;
			}

		}

		public boolean hasChildren(Object element) {
			if (element instanceof Element) {
				Element el = (Element) element;
				return el.getContent(new ElementFilter()).size() > 0
						|| el.getAttributes().size() > 0;
			} else {
				return false;
			}

		}

		public Object[] getElements(Object inputElement) {
			return ((Element) inputElement).getContent(new ElementFilter())
					.toArray();
		}

		public void dispose() {
			// TODO Auto-generated method stub

		}

		public void inputChanged(Viewer viewer, Object oldInput,
				Object newInput) {
		}

	});
	viewer.setInput(root.getRootElement());
	viewer.setLabelProvider(new LabelProvider() {
		public String getText(Object element) {
			if (element instanceof Element)
				return ((Element) element).getName() + getAttributes((Element) element) + getTextStr((Element) element);
			else if (element instanceof Attribute) {
				return ((Attribute) element).getName() + "="
						+ ((Attribute) element).getValue();
			}else if (element instanceof Text) {
				return ((Text) element).getValue();
	}else {
				return element.toString();
			}
		}

		private String getTextStr(Element element) {
			if(element.getValue()!=null 
					&& element.getValue().length()>0
					&& !element.getValue().contains("\n")){
				String rc = element.getValue();
				if(rc.length()>15) {
					rc=rc.substring(0,15);
				}
				return "[" + rc.trim()+"]";
			} else return "";
		}

		private String getAttributes(Element element) {
			return utils.getAttributesAsString(element);
			
		}
	});
	}

	
	public void addXMLSelectionListener(SelectionListener sl) {
		tree.addSelectionListener(sl);
		
	}
	
	public void addSelectionChangedListener(ISelectionChangedListener sl) {
		viewer.addSelectionChangedListener(sl);
		
	}

}
