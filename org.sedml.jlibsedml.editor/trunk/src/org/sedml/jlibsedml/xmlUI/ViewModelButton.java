package org.sedml.jlibsedml.xmlUI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.sedml.jlibsedml.editor.gmodel.GElement;

/**
 * Button is enabled if the {@link GElement} passed into constructor can access the XML model 
 * referenced by SEDML Model element.
 * @author radams
 *
 */
public class ViewModelButton {
	
	public ViewModelButton(GElement gEl) {
		super();
		this.gelement = gEl;
	}

	private GElement gelement;
	Button ViewModelButton;
	
	/**
	 * Adds button whic hopens XML selection dialo
	 * @param parent The parent composite
	 * @param displayTextWdgt A {@link Text} widget in the same thread in which to display chosen XPath
	 */
	public void create(Composite parent, final Text displayTextWdgt){
	ViewModelButton = new Button(parent, SWT.PUSH);
	ViewModelButton.setText("Get XPath from Model");
	ViewModelButton.setEnabled(gelement.canGetModel());
	
	}
	
	public void addSelectionListener (SelectionListener selListener){
		ViewModelButton.addSelectionListener(selListener);
	}
	
	public void setEnabled (boolean enabled){
		ViewModelButton.setEnabled(enabled);
	}

}
