package org.sedml.jlibsedml.editor.gmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.sedml.Report;
import org.sedml.SEDMLTags;

public class GReport extends GOutput {
	
	private List<GDataset> ds = new ArrayList<GDataset>();

	
	public GReport(Report report) {
		super(report);
		setId(report.getId());
		setName(report.getName());
		
	}

	public GReport() {
		super();
	}

	 GReport(GReport toCopy) {
		super(toCopy);
	}

	Report getSEDMLObject() {
		assert(canGetSedML());
		Report r= new Report(getId(),getName());
		r.addAnnotation(createGraphicalInfoAnnotations());
		addNotes(r);
		return r;
	}
	
	public boolean addDataset(GDataset c){
		return ds.add(c);
	}
	
	public boolean removeDataset(GDataset c){
		return ds.remove(c);
	}
	public List<GDataset> getGDatasets (){
		return Collections.unmodifiableList(ds);
	}
	@Override
	public String getType() {
		return SEDMLTags.OUTPUT_REPORT;
	}
	
	/**
	 * Subclasses can set up cross-references here if need be, default does nothing.
	 * @param target
	 */
	 void setUpRefs(GElement target) {
		 if(target.isDataset()){
			 addDataset((GDataset)target);
		 } 
	 }
	 
	 void removeRefs(GElement target) {
		 if(target.isDataset()){
			removeDataset((GDataset)target);
		 } 
			
		}

	@Override
	public GReport getCopy() {
		return new GReport(this);
	}

}
