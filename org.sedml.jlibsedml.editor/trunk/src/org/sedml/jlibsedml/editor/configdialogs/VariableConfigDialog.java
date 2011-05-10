package org.sedml.jlibsedml.editor.configdialogs;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.sedml.VariableSymbol;
import org.sedml.jlibsedml.editor.gmodel.GVariable;
import org.sedml.jlibsedml.xmlUI.ViewModelButton;
import org.sedml.jlibsedml.xmlUI.XMLElementXPathGeneratorDialog;



public class VariableConfigDialog extends BaseConfigDialog {
	private static final int TIME_INDEX = 0;
	final int NO_SYMBOL_INDX=1;
	private GVariable gvar;
	private VariableSymbol oldSymbol;
	private String oldName, oldtarget, oldId;
	private Combo symbols;
	private Text target;
	private ViewModelButton ViewModelButton;
	private IDNameGroup nameGroup;
	public VariableConfigDialog(Shell shell, GVariable gvar) {
		super(shell);
		this.gvar=gvar;
	    this.oldName=gvar.getName();
	    this.oldtarget=gvar.getTargetXPath();
	    this.oldSymbol = gvar.getSymbol();
	    this.oldId=gvar.getId();
		
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite child =(Composite)super.createDialogArea(parent);
		getShell().setText("Add properties to Variable");
		//Composite child = new Composite(parent,SWT.NULL);
		child.setLayout(createGridLayout(3));
		
		nameGroup=new IDNameGroup(child, gvar);
		nameGroup.nmText.addModifyListener(new VerifyingModifyListener());
		nameGroup.idText.setEditable(true);
		nameGroup.idText.addModifyListener( new VerifyingModifyListener());
			
	
		createSymbols(child);
		createTarget(child);
		createStatus(child, 3);
		setHelp(parent);
		setHelpAvailable(true);
		setInitialisationComplete(true);
		
		return child;
	}
	
	private void setHelp(Composite parent) {
		//setHelpAvailable(true);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,
				PLUGINID + ".var");

	}
	
 
	
	 /**
	  * Validates textfields here as at this point all buttons and dialog contents are
	  *  initialised
	  * @see org.eclipse.jface.dialogs.TrayDialog#createButtonBar(org.eclipse.swt.widgets.Composite)
	  */
	 protected Control createButtonBar(Composite parent) {
			Control c =super.createButtonBar(parent);
			updateEnablementAndVerify();
			return c;
			
		}
	private void createTarget(Composite child) {
		new Label (child,SWT.NULL).setText("Target XPath: ");
		target=new Text(child,SWT.BORDER);
	
		if(gvar.getTargetXPath()!=null){
			target.setText(gvar.getTargetXPath());
		}
		GridData gd = new GridData(GridData.FILL_BOTH);
		target.setLayoutData(gd);
		target.addModifyListener(new VerifyingModifyListener());
		target.addFocusListener(new VerifyingFocusListener());
		
		ViewModelButton  =new ViewModelButton(gvar);
		ViewModelButton.create(child, target);
		ViewModelButton.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
					XMLElementXPathGeneratorDialog dialog = new XMLElementXPathGeneratorDialog(Display.getCurrent().getActiveShell(),
							gvar.getModelDocument());
					if(dialog.open()==Window.OK){
						target.setText(dialog.getSelectedXPathFromViewer());
					}
				
				
				
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		
	}

	private void createSymbols(Composite child) {
	
		new Label (child,SWT.NULL).setText("Symbol: ");
		symbols = new Combo(child, SWT.DROP_DOWN| SWT.READ_ONLY);
		symbols.setItems(new String []{"Time", "No Symbol"});
		if(gvar.getSymbol()!=null && gvar.getSymbol().equals(VariableSymbol.TIME)){
			symbols.select(TIME_INDEX);
		}else{
		symbols.select(NO_SYMBOL_INDX);
		}
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan=2;
		symbols.setLayoutData(gd);
		symbols.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
			
				updateEnablementAndVerify();
			}

			
			
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
	}
	private void updateEnablementAndVerify() {
		if(symbols.getSelectionIndex()!=NO_SYMBOL_INDX){
			target.setEnabled(false);
			ViewModelButton.setEnabled(false);
		}else {
			target.setEnabled(true);
			ViewModelButton.setEnabled(true);
		}
		verifyAll(createVerifyObjects());
	}
	@Override
	List<VerifyObject> createVerifyObjects() {
		 VerifyObject vo3= new VerifyObject(nameGroup.idText.getText(), new IsUniqueIDValidator(gvar));
		if(target.isEnabled()){
			VerifyObject v1 = new VerifyObject(target.getText(), new NonEmptyStringValidator("Target XPath"));
			
			return Arrays.asList(new VerifyObject[]{v1,vo3});
		} else {
			return Arrays.asList(new VerifyObject[]{vo3});
		}
	}
	
	protected void okPressed() {
		final String targetstr= target.getText();
		final VariableSymbol symbolstr = (symbols.getSelectionIndex()==TIME_INDEX)?VariableSymbol.TIME:null;
		final String id = nameGroup.idText.getText();
		
		if(hasChanged(targetstr, symbolstr, id)) {
			execute ( new ICommand() {
				
				public void undo() {
					resetOldValues();		
				}
				
				public void redo() {
					if(VariableSymbol.TIME.toString().equals(symbolstr==null?null:symbolstr.toString())){ // symbolstr can be null
						gvar.setTargetXPath(null);			
					} else {
						gvar.setTargetXPath(targetstr);	
					}
					gvar.setSymbol(symbolstr);
					gvar.setId(id);			
				}
				public void execute() {
					redo();
					}
				public String getLabel() {
					return "Edit Variable";
				}
			});
		}
		
		super.okPressed();
		
	}

	private boolean hasChanged(String targetstr, VariableSymbol symbolstr,
			String id) {
	return !targetstr.equals(oldtarget)|| !id.equals(oldId) ||
	   (  symbolstr!=null &&!symbolstr.toString().equals(oldSymbol==null?"":oldSymbol.toString()));
	}

	@Override
	void resetOldValues() {
		gvar.setId(oldId);
		gvar.setName(oldName);
		gvar.setTargetXPath(oldtarget);
		gvar.setSymbol(oldSymbol);
		

	}

}
