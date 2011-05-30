package org.sedml.jlibsedml.editor.configdialogs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.sedml.NewXML;
import org.sedml.SEDMLTags;
import org.sedml.SedML;
import org.sedml.XPathTarget;
import org.sedml.jlibsedml.editor.gmodel.GChange;
import org.sedml.jlibsedml.xmlUI.BaseXMLDialog;
import org.sedml.jlibsedml.xmlUI.ViewModelButton;
import org.sedml.jlibsedml.xmlUI.XMLAttributeXPathGeneratorDialog;
import org.sedml.jlibsedml.xmlUI.XMLElementXPathGeneratorDialog;
import org.sedml.jlibsedml.xmlUI.XMLPreviewer;
import org.sedml.jlibsedml.xmlutils.XMLHandler;
import org.sedml.jlibsedml.xmlutils.XMLUtils;

public class ChangeConfigDialog extends BaseConfigDialog {

	private GChange gc;

	private String oldName;
	private String oldType;
	private XPathTarget oldTarget;
	private Combo chTypeCombo;
	private Button previewModel;
	private ViewModelButton viewModelButt;
	private Text target;
	private Text replacement;
	private String[] comboItems = new String[] { SEDMLTags.CHANGE_ATTRIBUTE_KIND,
			SEDMLTags.REMOVE_XML_KIND, SEDMLTags.ADD_XML_KIND,
			SEDMLTags.CHANGE_XML_KIND };
	VerifyingFocusListener vfl = new VerifyingFocusListener();
	VerifyingModifyListener vml =new VerifyingModifyListener();

	private XMLHandler xmlValidator=new XMLHandler();

	private NewXML oldNewxml;

	private String oldValue;

	public ChangeConfigDialog(Shell shell, GChange gc) {
		super(shell);
		this.gc = gc;
		this.oldName = gc.getName();
		this.oldType = gc.getChType();
		this.oldTarget=gc.getTarget();
		this.oldNewxml=gc.getNewxml();
		this.oldValue=gc.getNewValue();

	}
	
	

	protected Control createDialogArea(Composite parent) {
		Composite child = (Composite) super.createDialogArea(parent);
		getShell().setText("Add properties to Change ");
		// Composite child = new Composite(parent,SWT.NULL);
		GridLayout gl = new GridLayout();
		gl.numColumns = 3;
		gl.verticalSpacing = 15;
		child.setLayout(gl);

	
		createStatus(child, 3);
		createTarget(child);
		createChType(child);
		createReplacementText(child);
		setHelp(child);
		setHelpAvailable(true);
		setInitialisationComplete(true);

		return child;
	}
	
	private void setHelp(Composite parent) {
		//setHelpAvailable(true);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,
				PLUGINID + ".change");

	}

	private void createReplacementText(Composite child) {
		new Label(child, SWT.NULL).setText("Replacement value: ");
		replacement = new Text(child, SWT.BORDER | SWT.WRAP);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		replacement.setLayoutData(gd);
		replacement.addModifyListener(new VerifyingModifyListener() {
			public void modifyText(ModifyEvent e) {
				super.modifyText(e);
			//	boolean enablePreview = 
				previewModel.setEnabled(calculateEnabled());
				
			}
		});
		replacement.addFocusListener(new VerifyingFocusListener());
		if(gc.getNewxml()!=null){
			for (Element el : gc.getNewxml().getXml()){
				el.setNamespace(Namespace.NO_NAMESPACE); // else acquires SEDML-NS when saved, we don't want this
				  // as its supposed to go into model NS at runtime
			}
			String xmlAsString=xmlValidator.getElementsAsString(gc.getNewxml().getXml());
			replacement.setText(xmlAsString);	
		}else if(gc.isChangeAttribute() && gc.getNewValue()!=null){
			replacement.setText(gc.getNewValue());
		}

		
	}

	private void createTarget(Composite child) {
		new Label(child, SWT.NULL).setText("Target XPath: ");
		target = new Text(child, SWT.BORDER);

		if (gc.getTarget() != null) {
			target.setText(gc.getTarget().getTargetAsString());
		}
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.minimumWidth=100;
		target.setLayoutData(gd);
		Composite modelViewButtons = new Composite(child,SWT.NONE);
		GridLayout gl2 = new GridLayout(1, true);
		modelViewButtons.setLayout(gl2);
		 viewModelButt = new ViewModelButton(gc);
		viewModelButt.create(modelViewButtons, target);
		viewModelButt.addSelectionListener(new SelectionListener() {
			final String ERROR_TITLE="Error reading model file";
			final String ERROR_MESSAGE="There was an error reading the model file - please check it is valid XML. \n";
			public void widgetSelected(SelectionEvent e) {
				Document doc;
				final Shell activeShell = Display.getCurrent().getActiveShell();
				try {
					
					doc = new XMLUtils().readDoc(new ByteArrayInputStream(gc.getModelAsString().
							getBytes("UTF-8")));
					BaseXMLDialog xmlDialog=null;
					if(chTypeCombo.getText().equals(SEDMLTags.CHANGE_ATTRIBUTE_KIND)){
						xmlDialog = new XMLAttributeXPathGeneratorDialog(activeShell,
								gc.getModelDocument());
					} else {
						xmlDialog=new XMLElementXPathGeneratorDialog(activeShell,
								gc.getModelDocument());
					}
					if(xmlDialog.open()==Window.OK){
						target.setText(xmlDialog.getSelectedXPathFromViewer());
						previewModel.setEnabled(calculateEnabled());
					}
				} catch (JDOMException e1) {
					MessageDialog.openError(activeShell,ERROR_TITLE ,
							ERROR_MESSAGE  +e1.getMessage());
				} catch (IOException e1) {
					MessageDialog.openError(activeShell,ERROR_TITLE ,
							ERROR_MESSAGE  +e1.getMessage());
					e1.printStackTrace();
				}
			
				
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		previewModel = new Button(modelViewButtons, SWT.PUSH);
		previewModel.setText("Preview model");
		previewModel.setEnabled(calculateEnabled());
		
		// should only be active if calculateEnabled() is true
		previewModel.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				if(gc.canGetModel()){
					//GChange tm = gc.getCopy();
					updateModel(gc, chTypeCombo.getText(), target.getText(), replacement.getText());
				//	tm.setOwner(gc.getOwner());
					Document model = gc.getModelDocument();
					XMLPreviewer viewer = new XMLPreviewer(getShell(), model);
					viewer.open();
				}
				
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		
		target.addModifyListener(new VerifyingModifyListener());
		target.addFocusListener(new VerifyingFocusListener());

	}

	private boolean calculateEnabled() {
		if(! viewModelButt.isEnabled() ||  target== null || target.getText().length() ==0) {
			return false;
		}
		if(chTypeCombo==null){
			return false;
		}
		if(!chTypeCombo.getText().equals(SEDMLTags.REMOVE_XML_KIND)){
			if (replacement==null || replacement.getText().length()==0){
				return false;
			}
		}
		return true;
	}



	private void createChType(Composite child) {
		new Label(child, SWT.NULL).setText("Change type:");
		chTypeCombo = new Combo(child, SWT.DROP_DOWN | SWT.READ_ONLY);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.horizontalSpan = 2;
		chTypeCombo.setLayoutData(gd);
		
		// set secltion based on previous value if it exists
		chTypeCombo.setItems(comboItems);
		chTypeCombo.select(0);
		for (int i = 0;i<comboItems.length;i++){
			if( comboItems[i].equals(gc.getChType())){
				chTypeCombo.select(i);
			}
		}
		
		chTypeCombo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				verifyAll(createVerifyObjects());
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
	
	}

	@Override
	List<VerifyObject> createVerifyObjects() {
		 // depending on what the change is, we want different verifiers
		if (chTypeCombo.getText().equals(SEDMLTags.ADD_XML_KIND)
				|| chTypeCombo.getText().equals(SEDMLTags.CHANGE_XML_KIND)) {
			VerifyObject v1 = new VerifyObject(replacement.getText(), xmlValidator);
			return Arrays.asList(new VerifyObject[]{v1});
			
		} else if (chTypeCombo.getText().equals(SEDMLTags.CHANGE_ATTRIBUTE_KIND)){
			VerifyObject v1 = new VerifyObject(replacement.getText(), new DoubleValidator());
			return Arrays.asList(new VerifyObject[]{v1});
		} else {
			return Collections.EMPTY_LIST;
		}

	}

	protected void okPressed() {
		final String chType = chTypeCombo.getText();
		final String targ = target.getText();
		final String replace=replacement.getText();
		if(hasChangedAttributes(chType, targ, replace)){
		execute( new ICommand() {
			
			public void undo() {
				resetOldValues();		
			}
			
			public void redo() {
				updateModel(gc,chType, targ, replace);
			}

			
			
			public void execute() {
				redo();	
			}

			public String getLabel() {
				return "Edit Change";
			}
		});
		}
		super.okPressed();
	

	}
	
	private void updateModel( GChange change, final String chType, final String targ,
			final String replace) {
		change.setChType(chType);
		change.setTarget(new XPathTarget(targ));
		if(change.isChangeAttribute()){
			change.setNewValue(replace);
		}else if (change.isModifyXMLChange()){
			List<Element> xmlEls= xmlValidator.getXMLElements(replace);
			
			change.setNewxml(new NewXML(xmlEls));
		}
	}

	// execute only if has changed
	private boolean hasChangedAttributes(final String chType,
			final String targ, final String replace) {
		return !chType.equals(oldType) || 
				 !targ.equals(oldTarget==null?null:oldTarget.getTargetAsString()) ||
				 !replace.equals(oldValue)||
				 (oldNewxml !=null &&!replace.equals(xmlValidator.getElementsAsString(oldNewxml.getXml())));
	}

	@Override
	void resetOldValues() {
		gc.setName(oldName);
		gc.setChType(oldType);
		gc.setTarget(oldTarget);
		if(SEDMLTags.CHANGE_ATTRIBUTE_KIND.equals(oldType)){
			gc.setNewValue(oldValue);
		}
		if(SEDMLTags.CHANGE_XML_KIND.equals(oldType) || SEDMLTags.ADD_XML_KIND.equals(oldType)){
			gc.setNewxml(oldNewxml);
		}
		

	}

}
