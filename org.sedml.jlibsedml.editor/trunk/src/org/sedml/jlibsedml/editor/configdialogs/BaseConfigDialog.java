package org.sedml.jlibsedml.editor.configdialogs;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.sedml.jlibsedml.editor.Activator;
import org.sedml.jlibsedml.editor.WorkbenchPartExecutor;

public abstract class BaseConfigDialog extends TrayDialog {
    Label status;
    boolean isInitialising=true;
    /**
     * Focus listener that verifies all text fields.
     * @author radams
     *
     */
    class VerifyingFocusListener implements FocusListener {
    	public void focusLost(FocusEvent e) {}
		public void focusGained(FocusEvent e) {
			verifyAll(createVerifyObjects());			
		}
		
    }
    
    /**
     * Modify listener that verifies all text fields after modificcation
     * @author radams
     *
     */
    class VerifyingModifyListener implements ModifyListener {
    	public void modifyText(ModifyEvent e) {
			verifyAll(createVerifyObjects());	
				
		}
    }
    
     /**
      * Call this to enable validation, during initialisation validation is not performed as not all UI
      *  fields are initialized.
      * @param b
      */
     void setInitialisationComplete(boolean b) {
		isInitialising=false;
		
	}
     
     void execute (ICommand command) {
    	 getExecutor().executeOnCommandStack(command);
     }
    
     private  ICommandExecutor getExecutor() {
		return ExecutorServiceFactory.getInstance().getCommandExecutor();
	}

	/**
      * Subclasses to provide a list (can be empty but not null) of text field to verify. 
      *  Any failing will block the dialog OK button.
      * @return
      */
    abstract  List<VerifyObject> createVerifyObjects() ;
    
    static final String PLUGINID = Activator.PLUGIN_ID;
    
    
	protected BaseConfigDialog(Shell shell) {
		super(shell);
		setShellStyle(SWT.CLOSE | SWT.MAX | SWT.TITLE | SWT.BORDER
				| SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());
	}
	
	 void createStatus(Composite child, int colNo) {
		status=new Label(child, SWT.NULL);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER,true, false);
		gd.horizontalSpan=colNo;
		status.setLayoutData(gd);
		status.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		
	}
	 
	  /**
	   * Disables OK button
	   */
	  void disableOK() {
		   if(getButton(IDialogConstants.OK_ID)!=null)
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		}
	  /**
	   * Enables OK buttong
	   */
	  void enableOK() {
			getButton(IDialogConstants.OK_ID).setEnabled(true);
		}
	 void setStatusText(String text) {
		 status.setText(text);
	 }
	 /**
	  * Removes text from status line
	  */
	 void clearStatus (){
		 setStatusText("");
	 }
	 
	 
	 /**
	  * Verifies unless isInitialising == true
	  * @param vObjects
	  */
	 void verifyAll(List<VerifyObject> vObjects) {
		 	if(isInitialising)
		 		return;
			for (VerifyObject vo:vObjects){
				if(!vo.isValid()){
					setStatusText(vo.getMessage());
					disableOK();
					return;
				}	  
			}
			clearStatus();
			enableOK();

	 }
	 
	 GridLayout createGridLayout(int i) {
		 GridLayout gl=new GridLayout();
			gl.numColumns=i;
			gl.verticalSpacing=15;
			return gl;
	}
	 /**
	  * Validates textfields here as at this point all buttons and dialog contents are
	  *  initialised
	  * @see org.eclipse.jface.dialogs.TrayDialog#createButtonBar(org.eclipse.swt.widgets.Composite)
	  */
	 protected Control createButtonBar(Composite parent) {
			Control c =super.createButtonBar(parent);
			verifyAll(createVerifyObjects());
			return c;
			
		}
	 
	 protected void cancelPressed() {
			resetOldValues();
			super.cancelPressed();
		}

	abstract void resetOldValues();
	

	

}
