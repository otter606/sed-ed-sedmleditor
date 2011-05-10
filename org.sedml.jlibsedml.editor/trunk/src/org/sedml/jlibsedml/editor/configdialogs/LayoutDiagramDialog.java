package org.sedml.jlibsedml.editor.configdialogs;



import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.sedml.jlibsedml.editor.gmodel.GSedML;

/**
 * Dialog for user to choose layout options when laying out a diagram for the first time.
 * @author radams
 *
 */
public class LayoutDiagramDialog extends TrayDialog {
	private GSedML gsedml;
	public LayoutDiagramDialog(Shell shell, GSedML gsedml) {
		super(shell);
		this.gsedml=gsedml;
		// TODO Auto-generated constructor stub
	}
	
	private boolean isLayoutAll, isLayoutSome, isLayoutNone;
	
	
	protected Control createDialogArea(Composite parent) {
		Composite child =(Composite)super.createDialogArea(parent);
		getShell().setText("Layout diagram");
		GridLayout la = new GridLayout();
		la.verticalSpacing=15;
		child.setLayout(la);
		Label l =  new Label(child, SWT.NONE);
		l.setText(" Some or all of the SED-ML elements in this file lack graphical information. \n" +
				 " Please choose a layout option below:");
		
		Button nolayoutButt = new Button(child, SWT.RADIO);
		nolayoutButt.setText("Don't do any auto-layout - manually layout the elements yourself");
		nolayoutButt.addSelectionListener(new SelectionListener() {	
			public void widgetSelected(SelectionEvent e) {
				isLayoutNone=true;
				isLayoutAll=false;
				isLayoutSome=false;
				}	
			public void widgetDefaultSelected(SelectionEvent e) {	}
		});
		
		Button layoutAllButt = new Button(child, SWT.RADIO);
		layoutAllButt.setText("Full auto-layout, overriding existing graphical information");
		layoutAllButt.addSelectionListener(new SelectionListener() {	
			public void widgetSelected(SelectionEvent e) {
				isLayoutNone=false;
				isLayoutAll=true;
				isLayoutSome=false;
				}	
			public void widgetDefaultSelected(SelectionEvent e) {	}
		});
		// default is layout all
		layoutAllButt.setSelection(true);
		isLayoutAll=true;
		
		Button layoutSomeButt = new Button(child, SWT.RADIO);
		layoutSomeButt.setText("Just layout elements that lack graphical information");
		layoutSomeButt.addSelectionListener(new SelectionListener() {	
			public void widgetSelected(SelectionEvent e) {
				isLayoutNone=false;
				isLayoutAll=false;
				isLayoutSome=true;
				}	
			public void widgetDefaultSelected(SelectionEvent e) {	}
		});
		setHelp(parent);
		setHelpAvailable(true);
		
		return child;
		
	}
	
	private void setHelp(Composite parent) {
		//setHelpAvailable(true);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,
				BaseConfigDialog.PLUGINID + ".layout");

	}
	
	public boolean  isLayoutSome(){
		return isLayoutSome;
	}
	public boolean  isLayoutAll(){
		return isLayoutAll;
	}
	boolean  isLayoutNone(){
		return isLayoutNone;
	}

}
