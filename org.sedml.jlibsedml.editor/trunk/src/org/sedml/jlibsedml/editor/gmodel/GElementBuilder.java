package org.sedml.jlibsedml.editor.gmodel;

import java.util.HashMap;
import java.util.Map;

import org.jlibsedml.Change;
import org.jlibsedml.ComputeChange;
import org.jlibsedml.Curve;
import org.jlibsedml.DataGenerator;
import org.jlibsedml.DataSet;
import org.jlibsedml.Model;
import org.jlibsedml.Output;
import org.jlibsedml.Parameter;
import org.jlibsedml.Plot2D;
import org.jlibsedml.Report;
import org.jlibsedml.SedML;
import org.jlibsedml.Simulation;
import org.jlibsedml.Task;
import org.jlibsedml.UniformTimeCourse;
import org.jlibsedml.Variable;

/**
 * Generates the graphical hierarchy from a persisted SEDML file.
 * @author radams
 *
 */
public class GElementBuilder {

	private SedML sedml;
	private Map<String, GElement> id2Element= new HashMap<String, GElement>();
	public GElementBuilder(SedML sedMLModel) {
		this.sedml=sedMLModel;
	}
	
	public GSedML build(){
		GSedML gsedml = new GSedML();
		for (Model m: sedml.getModels()){
			GModel gmod = new GModel(m);
			id2Element.put(m.getId(), gmod);
			gsedml.addChild(gmod);
			for (Change c:m.getListOfChanges()){
				GChange gc = new GChange(c);
				gsedml.addChild(gc);
				new Connection(gmod,gc);
				if(gc.isComputeChange()){
					for (Variable v: ((ComputeChange)c).getListOfVariables()){
						createGVar(gsedml, gmod, gc, v);
					}
					for (Parameter p:  ((ComputeChange)c).getListOfParameters()){
						createGParam(gsedml, gc, p);
						
					}
				}
			}
		}
		for(Simulation sim:sedml.getSimulations()){
			GSimulation gsim = new GSimulation((UniformTimeCourse)sim);
			gsedml.addChild(gsim);
			id2Element.put(sim.getId(), gsim);
		}
		for(Task t:sedml.getTasks()){
			GTask gt = new GTask(t);
			id2Element.put(t.getId(), gt);
			gsedml.addChild(gt);
			GElement modelMap = id2Element.get(t.getModelReference());
			GElement simMap = id2Element.get(t.getSimulationReference());
			if(modelMap!=null && modelMap.isModel()){
				new Connection(gt,modelMap);
			}if(	simMap!=null && simMap.isSimulation()) {
				new Connection(gt, simMap);
				}
		}
		for (DataGenerator dg : sedml.getDataGenerators()){
			GDataGenerator gdg=new GDataGenerator(dg);
			id2Element.put(gdg.getId(), gdg);
			gsedml.addChild(gdg);
			for (Variable v:dg.getListOfVariables()){
				GVariable gvar = new GVariable(v);
				id2Element.put(v.getId(), gvar);
				gsedml.addChild(gvar);
				new Connection(gdg, gvar);
				GElement taskMap = id2Element.get(v.getReference());
				if(taskMap!=null && taskMap.isTask()){
					new Connection(gvar,taskMap);
				}
			}
			for (Parameter p:  dg.getListOfParameters()){
				createGParam( gsedml,gdg,p);
				
			}
		}
		for (Output o : sedml.getOutputs()){
			if(o.isPlot2d()){
				GPlot2D gp2d= new GPlot2D((Plot2D)o);
				id2Element.put(o.getId(), gp2d);
				gsedml.addChild(gp2d);
				for(Curve c: ((Plot2D)o).getListOfCurves()) {
					GCurve gc = new GCurve(c);
					id2Element.put(c.getId(), gc);
					gsedml.addChild(gc);
					new Connection(gp2d, gc);
					GElement xdg = id2Element.get(c.getXDataReference());
					if(xdg!=null && xdg.isDataGenerator()){
						new Connection(gc,xdg);
					}
					GElement ydg = id2Element.get(c.getYDataReference());
					if(ydg!=null && ydg.isDataGenerator()){
						new Connection(gc,ydg);
					}
					
				}
			}else if(o.isReport()){
				GReport gr= new GReport((Report)o);
				id2Element.put(o.getId(), gr);
				gsedml.addChild(gr);
				for(DataSet c: ((Report)o).getListOfDataSets()) {
					GDataset gc = new GDataset(c);
					id2Element.put(o.getId(), gc);
					gsedml.addChild(gc);
					new Connection(gr, gc);
					GElement dg = id2Element.get(c.getDataReference());
					if(dg!=null && dg.isDataGenerator()){
						new Connection(gc,dg);
					}
					
				}
			}
		}
		return gsedml;
		
	}

	private void createGParam(GSedML gsedml, GElement gc, Parameter p) {
		GParameter gp = new GParameter(p);
		id2Element.put(p.getId(), gp);
		gsedml.addChild(gp);
		new Connection(gc,gp);
	}

	private void createGVar(GSedML gsedml, GElement gmod, GChange gc, Variable v) {
		GVariable gvar = new GVariable(v);
		id2Element.put(v.getId(), gmod);
		gsedml.addChild(gvar);
		new Connection(gc,gvar);
	}

	
}
