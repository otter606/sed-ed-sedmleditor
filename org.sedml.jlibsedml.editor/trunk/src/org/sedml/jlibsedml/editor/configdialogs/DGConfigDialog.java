package org.sedml.jlibsedml.editor.configdialogs;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.jmathml.ASTNode;
import org.jmathml.FormulaFormatter;
import org.sedml.Libsedml;
import org.sedml.jlibsedml.editor.gmodel.GDataGenerator;
import org.sedml.jlibsedml.editor.gmodel.GVariable;

public class DGConfigDialog extends BaseConfigDialog {
	private GDataGenerator gdg;
	private Text maths;
	private ASTNode oldMaths;
	private IDNameGroup nameGroup;
	private ListViewer varViewer;
	public DGConfigDialog(Shell shell, GDataGenerator gdg) {
		super(shell);
		this.gdg=gdg;
		this.oldMaths=gdg.getMath();
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite child =(Composite)super.createDialogArea(parent);
		getShell().setText("Add properties to Data Generator");
		//Composite child = new Composite(parent,SWT.NULL);
		child.setLayout(createGridLayout(3));
		nameGroup=new IDNameGroup(child, gdg);
		
		createMaths(child);
		createVarViewer(child);
		createStatus(child, 3);
		setHelp(parent);
		setHelpAvailable(true);
		setInitialisationComplete(true);
		return child;
		
	}
	private void setHelp(Composite parent) {
		//setHelpAvailable(true);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,
				PLUGINID + ".dg");

	}


	private void createVarViewer(Composite child) {
		org.eclipse.swt.widgets.List list2 = new org.eclipse.swt.widgets.List(child,SWT.SINGLE);
		varViewer = new ListViewer(list2);
		varViewer.setContentProvider( new IStructuredContentProvider() {	
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {	}
			public void dispose() {}
			public Object[] getElements(Object inputElement) {
				return (gdg.getVars().toArray());
			}
		});
		varViewer.setLabelProvider(new LabelProvider() {
			public String getText(Object element) {
				return ((GVariable)element).getId() + ", (" + ((GVariable)element).getId()+")";
			}
		});
		varViewer.setComparator(new ViewerComparator(new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.toLowerCase().compareTo(o2.toLowerCase());
			}
		}));
		varViewer.addSelectionChangedListener(new ISelectionChangedListener() {		
			public void selectionChanged(SelectionChangedEvent event) {
				if(event.getSelection()!=null) {
					GVariable gv = (GVariable)((IStructuredSelection)event.getSelection()).getFirstElement();
					int caret = maths.getCaretPosition();
					System.err.println("Caret at " + caret);
					maths.setText(maths.getText().substring(0, caret) + " "+ gv.getId() + " "+
							maths.getText().substring(caret, maths.getText().length()) );
				}
			}
		});
	
		varViewer.setInput(gdg);
		
	}

	private void createMaths(Composite child) {
		new Label(child, SWT.NULL).setText("Maths expression");
		maths = new Text(child,SWT.BORDER);
		String existingMaths="";
		if(gdg.getMath()!=null){
			existingMaths = new FormulaFormatter().formulaToString(gdg.getMath());
		}
		maths.setText(existingMaths);
		maths.addFocusListener(new VerifyingFocusListener());
		maths.addModifyListener(new VerifyingModifyListener());
		GridData gd = new GridData(SWT.FILL, SWT.CENTER,true, false);
		gd.horizontalSpan=2;
		maths.setLayoutData(gd);
		
	}

	@Override
	List<VerifyObject> createVerifyObjects() {
	VerifyObject vo = new VerifyObject(maths.getText(), new MathsValidator());
	
	return Arrays.asList(new VerifyObject[]{vo});
	}

	@Override
	void resetOldValues() {
		gdg.setMath(oldMaths);

	}
	
	protected void okPressed() {
		final String mathsText = maths.getText();
		if(oldMaths == null || !mathsText.equals(new FormulaFormatter().formulaToString(oldMaths))){
			execute( new ICommand() {	
				public void undo() {
					resetOldValues();	
				}
				public void redo() {
					gdg.setMath(Libsedml.parseFormulaString(mathsText));
				}
				
				public void execute() {
					redo();
					}
				public String getLabel() {
					return "Edit Data Generator";
				}
			});
		}
		gdg.setMath(Libsedml.parseFormulaString(maths.getText()));
		super.okPressed();
		
	}

}
