package org.sedml.jlibsedml.editor.configdialogs;


import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
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
import org.sedml.Algorithm;
import org.sedml.UniformTimeCourse;
import org.sedml.jlibsedml.editor.gmodel.GSimulation;
import org.sedml.modelsupport.KisaoOntology;
import org.sedml.modelsupport.KisaoTerm;
/**
 * Simulation config dialog.
 * @author radams
 *
 */
public class UTCConfigDialog extends BaseConfigDialog {

	private GSimulation sim;
	
	private Text simStart, outStart, end, numPoints;
	private ComboViewer kisao;
	// in case is cancelled, to revert previous values
	private UniformTimeCourse oldUTC;

	public UTCConfigDialog(Shell activeShell, GSimulation created) {
		super(activeShell);
		this.sim=created;
		recordOldValues();
	}
	
	private void setHelp(Composite parent) {
		//setHelpAvailable(true);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,
				PLUGINID + ".simulationHelp");

	}
	
	private void recordOldValues() {
		oldUTC= new UniformTimeCourse(sim.getId(), sim.getName(), sim.getStart(), sim.getOutStart(), sim.getOutEnd(),
						sim.getNumPoints(), sim.getAlgorithm());
		
	}
	
	
	 void resetOldValues() {
		sim.setAlgorithm(oldUTC.getAlgorithm());
		sim.setName(oldUTC.getName());
		sim.setNumPoints(oldUTC.getNumberOfPoints());
		sim.setStart(oldUTC.getInitialTime());
		sim.setOutStart(oldUTC.getOutputStartTime());
		sim.setOutEnd(oldUTC.getOutputEndTime());
	}

	protected Control createDialogArea(Composite parent) {
		Composite child =(Composite)super.createDialogArea(parent);
		getShell().setText("Add properties to time course");
		child.setLayout(createGridLayout(3));
		new IDNameGroup(child, sim);
		createSimStart(child);
		createOutputStart(child);
		createEnd(child);
		createTimepoints(child);
		createAlgorithm(child);
		createStatus(child,3);
		setHelp(parent);
		setHelpAvailable(true);
		setInitialisationComplete(true);
		return child;
		
	}

	private void createAlgorithm(Composite child) {
		new Label(child, SWT.NULL).setText("Algorithm ");
		kisao = new ComboViewer(child);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER,true, false);
		gd.horizontalSpan=2;
		kisao.getCombo().setLayoutData(gd);
		kisao.setContentProvider(new IStructuredContentProvider() {	
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {	}
			public void dispose() {}
			public Object[] getElements(Object inputElement) {
				return ((List<KisaoTerm>)inputElement).toArray();
			}
		});
		kisao.setLabelProvider(new LabelProvider() {
			public String getText(Object element) {
				return ((KisaoTerm)element).getName() + ", (" + ((KisaoTerm)element).getId()+")";
			}
		});
		// orders kisao terms alaphabetically
		kisao.setComparator(new ViewerComparator(new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.toLowerCase().compareTo(o2.toLowerCase());
			}
		}));
		kisao.setInput(KisaoOntology.getInstance().getTerms());
		if(sim.getAlgorithm()!=null){
			IStructuredSelection sel=null;
			for(KisaoTerm kt: KisaoOntology.getInstance().getTerms()){
				if(kt.getId().equals(sim.getAlgorithm().getKisaoID())){
					sel=new StructuredSelection(kt);
					break;
				}
			}
			if(sel!=null){
				kisao.setSelection(sel, true);
			}
		}
		kisao.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				KisaoTerm term = (KisaoTerm)((IStructuredSelection)event.getSelection()).getFirstElement();
				sim.setAlgorithm(new Algorithm(term.getId()));		
			}
		});
		
	}



	
	 List<VerifyObject> createVerifyObjects() {
		VerifyObject v1 = new VerifyObject(numPoints.getText(), new PositiveIntValidator());
		VerifyObject v2 = new VerifyObject(simStart.getText(), new PositiveDoubleValidator());
		VerifyObject v3 = new VerifyObject(outStart.getText(), new PositiveDoubleValidator());
		VerifyObject v4 = new VerifyObject(end.getText(), new PositiveDoubleValidator());
		return Arrays.asList(new VerifyObject[]{v1,v2,v3,v4});
	}	
	private void createTimepoints(Composite child) {
		new Label(child, SWT.NULL).setText("Number of timepoints: ");
		numPoints = new Text(child,SWT.BORDER);
		numPoints.setText(Integer.toString(sim.getNumPoints()));
		GridData gd = new GridData(SWT.FILL, SWT.CENTER,true, false);
		gd.horizontalSpan=2;
		numPoints.addModifyListener(new VerifyingModifyListener());
		numPoints.addFocusListener(new VerifyingFocusListener());
		numPoints.setLayoutData(gd);
		
	}
	
	
	/*
	 * We disable OK unless fields are valid 
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed() {
		final double simStartd = Double.parseDouble(simStart.getText());
		final double outStartd = Double.parseDouble(outStart.getText());
		final double outEndd = Double.parseDouble(end.getText());
		final int numpointss = Integer.parseInt(numPoints.getText());
		final Algorithm alg = sim.getAlgorithm();
		if( hasChanged(simStartd, outStartd, outEndd, numpointss )
				|| !sim.getAlgorithm().equals(oldUTC.getAlgorithm())){
			execute( new ICommand() {
				
				public void undo() {
					resetOldValues();
				}
				
				public void redo() {
					sim.setStart(simStartd);
					sim.setOutStart(outStartd);
					sim.setOutEnd(outEndd);
					sim.setNumPoints(numpointss);
					sim.setAlgorithm(alg);
				}
				
				public void execute() {
					redo();		
				}
				public String getLabel() {
					return "Edit Simulation";
				}
			});
		}
		
		super.okPressed();
		
	}
	
	
	

	private boolean hasChanged(double simStartd, double outStartd,
			double outEndd, int numpointss) {
		double TOLERANCE = 0.000001;
		return numpointss != oldUTC.getNumberOfPoints() ||
		Math.abs( simStartd - oldUTC.getInitialTime()) >TOLERANCE ||
		Math.abs(outStartd-oldUTC.getOutputStartTime()) > TOLERANCE ||
		Math.abs(outEndd-oldUTC.getOutputEndTime()) > TOLERANCE; 
		}

	private void createEnd(Composite child) {
		new Label(child, SWT.NULL).setText("Simulation end: ");
		end = new Text(child,SWT.BORDER);
		end.setText(Double.toString(sim.getOutEnd()));
		GridData gd = new GridData(SWT.FILL, SWT.CENTER,true, false);
		gd.horizontalSpan=2;
		end.setLayoutData(gd);
		end.addFocusListener(new VerifyingFocusListener());
		end.addModifyListener(new VerifyingModifyListener());
		
	}

	private void createOutputStart(Composite child) {
		new Label(child, SWT.NULL).setText("Output start: ");
		 outStart = new Text(child,SWT.BORDER);
		 outStart.setText(Double.toString(sim.getOutStart()));
		GridData gd = new GridData(SWT.FILL, SWT.CENTER,true, false);
		gd.horizontalSpan=2;
		outStart.setLayoutData(gd);
		outStart.addFocusListener(new VerifyingFocusListener());
		outStart.addModifyListener(new VerifyingModifyListener());
		
	}

	private void createSimStart(Composite child) {
		new Label(child, SWT.NULL).setText("Simulation start: ");
		simStart = new Text(child,SWT.BORDER);
		simStart.setText(Double.toString(sim.getStart()));
		GridData gd = new GridData(SWT.FILL, SWT.CENTER,true, false);
		gd.horizontalSpan=2;
		simStart.setLayoutData(gd);
		simStart.addFocusListener(new VerifyingFocusListener());
		simStart.addModifyListener(new VerifyingModifyListener());
	}

}
