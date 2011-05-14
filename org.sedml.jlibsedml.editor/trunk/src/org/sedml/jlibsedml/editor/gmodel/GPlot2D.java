package org.sedml.jlibsedml.editor.gmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.sedml.Plot2D;
import org.sedml.SEDMLTags;

public class GPlot2D extends GOutput {
	
	private List<GCurve> curves = new ArrayList<GCurve>();

	public GPlot2D(Plot2D o) {
		super(o);
		setId(o.getId());
		setName(o.getName());
	}

	public GPlot2D() {
		// TODO Auto-generated constructor stub
	}

	 GPlot2D(GPlot2D toCopy) {
		super(toCopy);
	}

	Plot2D getSEDMLObject() {
		assert(canGetSedML());
		Plot2D rc = new Plot2D(getId(), getName());
		rc.addAnnotation(createGraphicalInfoAnnotations());
		addNotes(rc);
		return rc;
	}
	
	public boolean addCurve(GCurve c){
		return curves.add(c);
	}
	
	public boolean removeCurve(GCurve c){
		return curves.remove(c);
	}
	public List<GCurve> getCurves (){
		return Collections.unmodifiableList(curves);
	}
	@Override
	public String getType() {
		return SEDMLTags.OUTPUT_P2D;
	}
	
	/**
	 * Subclasses can set up cross-references here if need be, default does nothing.
	 * @param target
	 */
	 void setUpRefs(GElement target) {
		 if(target.isCurve()){
			 addCurve((GCurve)target);
		 } 
	 }
	 
	 void removeRefs(GElement target) {
		 if(target.isCurve()){
			removeCurve((GCurve)target);
		 } 
			
		}

	@Override
	public GPlot2D getCopy() {
		return new GPlot2D(this);
	}
	

}
