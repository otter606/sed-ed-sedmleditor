package org.sedml.jlibsedml.editor.gmodel;

import java.util.ArrayList;
import java.util.List;

import org.jlibsedml.DataGenerator;
import org.jlibsedml.DataSet;
import org.jlibsedml.Model;
import org.jlibsedml.Plot2D;
import org.jlibsedml.Report;
import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.SedML;
import org.jlibsedml.Simulation;
import org.jlibsedml.Task;

/**
 * Generates a SEDML object model from a GSEDML representation if there are no
 * missing mandatory fields in the GSEDML objects.
 * 
 * @author radams
 * 
 */
public class SEDMLBuilder {

	List<GElement> gelsWithErrors = new ArrayList<GElement>();

	public SedML buildSEDML(GSedML graphModel) {

		SedML rc = new SEDMLDocument().getSedMLModel();

		for (GElement child : graphModel.getChildren()) {
			if (child.isModel()) {
				GModel gmod = (GModel) child;
				if (!gmod.canGetSedML()) {
					gelsWithErrors.add(child);
				} else {
					Model m = gmod.getSEDMLObject();
					rc.addModel(m);
					if (gmod.getChanges().size() > 0) {
						for (GChange gc : gmod.getChanges()) {
							if (!gc.canGetSedML()) {
								gelsWithErrors.add(child);
							} else {
								boolean b = m.addChange(gc.getSEDMLObject());
								System.err.println(b);
							}
						}

					}

				}
			} else if (child.isSimulation()) {
				GSimulation gsim = (GSimulation) child;
				if (!gsim.canGetSedML()) {
					gelsWithErrors.add(child);
				} else {
					Simulation sim = gsim.getSEDMLObject();
					rc.addSimulation(sim);

				}
			} else if (child.isTask()) {
				GTask gtsk = (GTask) child;
				if (!gtsk.canGetSedML()) {
					gelsWithErrors.add(child);
				} else {
					Task t = gtsk.getSEDMLObject();
					rc.addTask(t);
				}
			} else if (child.isDataGenerator()) {
				GDataGenerator gDG = (GDataGenerator) child;
				if (!gDG.canGetSedML()) {
					gelsWithErrors.add(child);
				} else {
					DataGenerator dg = gDG.getSEDMLObject();
					for (GVariable gv : gDG.getVars()) {
						if (!gv.canGetSedML()) {
							gelsWithErrors.add(gv);
						} else {
							dg.addVariable(gv.getSEDMLObject());
						}
					}
					for (GParameter gp : gDG.getParams()) {
						if (!gp.canGetSedML()) {
							gelsWithErrors.add(gp);
						} else {
							dg.addParameter(gp.getSEDMLObject());
						}
					}
					rc.addDataGenerator(dg);
				}
			} else if (child.isOutput()) {
				if (child.isReport()) {
					GReport grep = (GReport) child;
					if (!grep.canGetSedML()) {
						gelsWithErrors.add(grep);
					} else {
						Report rep = grep.getSEDMLObject();
						for (GDataset gd : grep.getGDatasets()) {
							if (!gd.canGetSedML()) {
								gelsWithErrors.add(gd);
							} else {
								DataSet ds = gd.getSEDMLObject();
								rep.addDataSet(ds);
							}

						}
						rc.addOutput(rep);
					}
				} else if (child.isPlot2D()) {
					GPlot2D gp2d = (GPlot2D) child;
					if (!gp2d.canGetSedML()) {
						gelsWithErrors.add(child);
					} else {
						Plot2D p2d = gp2d.getSEDMLObject();
						for (GCurve gc : gp2d.getCurves()) {
							if (!gc.canGetSedML()) {
								gelsWithErrors.add(gc);
							} else {
								p2d.addCurve(gc.getSEDMLObject());
							}
						}
						rc.addOutput(p2d);
					}

				}
			}

		}

		return rc;

	}

	List<GVariable> getGVariables(GSedML gsedml) {
		List<GVariable> rc = new ArrayList<GVariable>();
		for (GElement child : gsedml.getChildren()) {
			if (child.isVariable()) {
				rc.add((GVariable) child);
			}
		}
		return rc;
	}

	List<GParameter> getGParameters(GSedML gsedml) {
		List<GParameter> rc = new ArrayList<GParameter>();
		for (GElement child : gsedml.getChildren()) {
			if (child.isParameter()) {
				rc.add((GParameter) child);
			}
		}
		return rc;
	}

}
