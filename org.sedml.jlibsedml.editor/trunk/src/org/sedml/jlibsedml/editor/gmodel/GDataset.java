package org.sedml.jlibsedml.editor.gmodel;

import org.jlibsedml.DataSet;
import org.jlibsedml.SEDMLTags;

public class GDataset extends GElement {
	private GDataGenerator dg;
private String label;
	public GDataset(DataSet c) {
		super(c);
		setId(c.getId());
		setName(c.getName());
		setLabel(c.getLabel());
}

	public GDataset() {
		// TODO Auto-generated constructor stub
	}

	

	 GDataset(GDataset toCopy) {
		super(toCopy);
		setLabel(toCopy.getLabel());
	}

	public String getLabel() {
	return label;
}

public void setLabel(String label) {
	this.label = label;
}

	public GDataGenerator getDg() {
		return dg;
	}

	public void setDg(GDataGenerator dg) {
		this.dg = dg;
		firePropertyChange(PropertyChangeNames.PROPERTY_EVENT, null, dg);
	}
	
	/**
	 * Subclasses can set up cross-references here if need be, default does nothing.
	 * @param target
	 */
	 void setUpRefs(GElement target) {
		 if(target.isDataGenerator()){
			 setDg((GDataGenerator)target);
		 } 
	 }
	 
	 void removeRefs(GElement target) {
		 if(target.isDataGenerator()){
			 setDg(null);
		 } 
			
		}

	@Override
	public boolean canGetSedML() {
		return getId()!=null && getDg()!=null;
	}

	
	DataSet getSEDMLObject() {
		assert(canGetSedML());
		DataSet d= new DataSet(getId(), getName(), getId(), dg.getId());
		d.addAnnotation(createGraphicalInfoAnnotations());
		addNotes(d);
		return d;
	}

	@Override
	public String getType() {
		return SEDMLTags.OUTPUT_DATASET;
	}

	@Override
	public GDataset getCopy() {
		return new GDataset(this);
	}
}
