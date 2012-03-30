package org.sedml.jlibsedml.editor.actions;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.jlibsedml.AbstractIdentifiableElement;
import org.jlibsedml.DataGenerator;
import org.jlibsedml.Output;
import org.jlibsedml.Plot2D;
import org.jlibsedml.Plot3D;
import org.jlibsedml.Report;
import org.jlibsedml.SEDBase;
import org.jlibsedml.SedML;
import org.jlibsedml.Task;
import org.jlibsedml.Variable;
import org.sedml.jlibsedml.editor.GElementEditPart;
import org.sedml.jlibsedml.editor.MapEditor;
import org.sedml.jlibsedml.editor.gmodel.Connection;
import org.sedml.jlibsedml.editor.gmodel.GChange;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.GModel;
import org.sedml.jlibsedml.editor.gmodel.GOutput;

public class RestrictViewAction extends SelectionAction {
	
	

	public static final String RestrictView_ID = "RestrictView";

	public RestrictViewAction(IWorkbenchPart part) {
		super(part);

	}

	/**
	 * Only active if a single output is selected.
	 */
	@Override
	protected boolean calculateEnabled() {
		if (getSelectedObjects().size() == 1
				&& (getSelectedObjects().get(0) instanceof GElementEditPart)
				&& getModel() != null)
			return true;
		return false;
	}

	protected void init() {
		setId(RestrictView_ID);
		setActionDefinitionId("Restrict View");
		setText("Toggle Restricted View");
		setToolTipText("RestrictView");
		setEnabled(true);
	}

	public void run() {
		GOutput gout = (GOutput) getModel();
		SedML sedml = gout.getParent().buildSEDML();
		// we need an error free document to continue
		if(sedml==null){
			return;
		}

		Set<SEDBase> toHighlight = new HashSet<SEDBase>();
		if (sedml != null) {
			Output o = sedml.getOutputWithId(gout.getId());
			if(o.isPlot2d()){
				toHighlight.addAll(((Plot2D)o).getListOfCurves());
			}else if (o.isPlot3d()){
				toHighlight.addAll(((Plot3D)o).getListOfSurfaces());
			}else if (o.isReport()){
				toHighlight.addAll(((Report)o).getListOfDataSets());
			}
			toHighlight.add(o);
			for (String dgS : o.getAllDataGeneratorReferences()) {
				DataGenerator dg = sedml.getDataGeneratorWithId(dgS);
				toHighlight.add(dg);
				for (Variable v : dg.getListOfVariables()) {
					toHighlight.add(v);
					Task t = sedml.getTaskWithId(v.getReference());
					toHighlight.add(t);
					toHighlight
							.add(sedml.getModelWithId(t.getModelReference()));
					toHighlight.add(sedml.getSimulation(t
							.getSimulationReference()));
				}
			}
			MapEditor me = (MapEditor) getWorkbenchPart();
			Set<GElement> toSelectSS = new HashSet<GElement>();
			for (SEDBase toSelect : toHighlight) {
				for (GElement el : me.getModel().getChildren()) {
					if (el.getId() != null
							&& el.getId().equals(
									((AbstractIdentifiableElement) toSelect)
											.getId())) {
						// changes don't have an ID so need to be treated specially
						if(el.isModel()) {
							for (GChange gc: ((GModel)el).getChanges()){
								toSelectSS.add(gc);
							}
						}
						toSelectSS.add(el);
						break;
					}
				}
				
			}
			for (GElement el : me.getModel().getChildren()) {
				if (toSelectSS.contains(el)){
					el.show();
					showConnections(el);
				}else {
					if(!me.getModel().isRestrictedViewToggle()) {
						el.hide();
						hideConnections(el);
					}
					else {
						el.show();
						showConnections(el);
				}
			}
			}
			me.getModel().setRestrictedViewToggle(!me.getModel().isRestrictedViewToggle());
		   //selectAll(new StructuredSelection(toSelectSS),me);

		}
	}

	

	private void showConnections(GElement el) {
		for (Connection conn: el.getSrcConnections()){
			conn.show();
		}
		for (Connection conn: el.getTargetConnections()){
			conn.show();
		}
		
	}
	
	private void hideConnections(GElement el) {
		for (Connection conn: el.getSrcConnections()){
			conn.hide();
		}
		for (Connection conn: el.getTargetConnections()){
			conn.hide();
		}
		
	}



	GElement getModel() {
		GElement gel = ((GElementEditPart) getSelectedObjects().get(0))
				.getCastedModel();
		if (gel.isOutput()) {
			return gel;
		} else {
			return null;
		}
	}

}
