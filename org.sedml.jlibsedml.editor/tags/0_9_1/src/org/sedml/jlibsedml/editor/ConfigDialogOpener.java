package org.sedml.jlibsedml.editor;

import org.eclipse.swt.widgets.Display;
import org.sedml.jlibsedml.editor.configdialogs.BaseConfigDialog;
import org.sedml.jlibsedml.editor.configdialogs.ChangeConfigDialog;
import org.sedml.jlibsedml.editor.configdialogs.CurveConfigDialog;
import org.sedml.jlibsedml.editor.configdialogs.DGConfigDialog;
import org.sedml.jlibsedml.editor.configdialogs.DatasetConfigDialog;
import org.sedml.jlibsedml.editor.configdialogs.ModelConfigDialog;
import org.sedml.jlibsedml.editor.configdialogs.OutputConfigDialog;
import org.sedml.jlibsedml.editor.configdialogs.ParameterConfigDialog;
import org.sedml.jlibsedml.editor.configdialogs.TaskConfigDialog;
import org.sedml.jlibsedml.editor.configdialogs.UTCConfigDialog;
import org.sedml.jlibsedml.editor.configdialogs.VariableConfigDialog;
import org.sedml.jlibsedml.editor.gmodel.GChange;
import org.sedml.jlibsedml.editor.gmodel.GCurve;
import org.sedml.jlibsedml.editor.gmodel.GDataGenerator;
import org.sedml.jlibsedml.editor.gmodel.GDataset;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.GModel;
import org.sedml.jlibsedml.editor.gmodel.GOutput;
import org.sedml.jlibsedml.editor.gmodel.GParameter;
import org.sedml.jlibsedml.editor.gmodel.GSimulation;
import org.sedml.jlibsedml.editor.gmodel.GTask;
import org.sedml.jlibsedml.editor.gmodel.GVariable;

 class ConfigDialogFactory {
	 static BaseConfigDialog createDialog(GElement gmodel){
		 BaseConfigDialog rc=null;
	
	 if (gmodel.isModel()) {
			rc = new ModelConfigDialog(Display
					.getCurrent().getActiveShell(),
					(GModel) gmodel);
			
		}else if(gmodel.isSimulation()) {
			rc= new UTCConfigDialog(Display
					.getCurrent().getActiveShell(),
					(GSimulation) gmodel);
			
		}else if(gmodel.isTask()) {
			rc = new TaskConfigDialog(Display
					.getCurrent().getActiveShell(),
					(GTask) gmodel);
			
		}else if(gmodel.isDataGenerator()) {
			rc= new DGConfigDialog(Display
					.getCurrent().getActiveShell(),
					(GDataGenerator) gmodel);
			
		}else if(gmodel.isVariable()) {
			rc = new VariableConfigDialog(Display
					.getCurrent().getActiveShell(),
					(GVariable) gmodel);
			
		}else if(gmodel.isPlot2D() || gmodel.isReport()) {
			rc = new OutputConfigDialog(Display
					.getCurrent().getActiveShell(),
					(GOutput)gmodel);
			
		}else if(gmodel.isParameter()) {
			rc = new ParameterConfigDialog(Display
					.getCurrent().getActiveShell(),
					(GParameter)gmodel);
			
		}else if(gmodel.isCurve()) {
			rc = new CurveConfigDialog(Display
					.getCurrent().getActiveShell(),
					(GCurve)gmodel);
			
		}else if(gmodel.isDataset()) {
			rc = new DatasetConfigDialog(Display
					.getCurrent().getActiveShell(),
					(GDataset)gmodel);
			
		}else if(gmodel.isChange()) {
			rc = new ChangeConfigDialog(Display
					.getCurrent().getActiveShell(),
					(GChange)gmodel);
			
		}
	 return rc;
	 
	 }

}
