package org.sedml.jlibsedml.editor.gmodel;

import org.sedml.SEDMLTags;
import org.sedml.Task;

public class GTask extends GElement{
	
	private GModel model;
	private GSimulation sim;
	public GTask(Task t) {
		super(t);
		setId(t.getId());
		setName(t.getName());
	}
	public GTask() {
		// TODO Auto-generated constructor stub
	}
	public GModel getModel() {
		return model;
	}
	public void setModel(GModel m) {
		this.model = m;
		firePropertyChange(PropertyChangeNames.PROPERTY_EVENT, null, m);
	}
	public GSimulation getSim() {
		return sim;
	}
	public void setSim(GSimulation sim) {
		this.sim = sim;
		firePropertyChange(PropertyChangeNames.PROPERTY_EVENT, null, sim);
	}
	@Override
	public boolean canGetSedML() {
		return getId()!=null && getModel()!=null && getSim()!=null;
	}

	Task getSEDMLObject() {
		Task t= new Task(getId(),getName(),model.getId(),sim.getId());
		t.addAnnotation(createGraphicalInfoAnnotations());
		addNotes(t);
		return t;
	}
	@Override
	void setUpRefs(GElement target) {
		if(target.isModel()){
			setModel((GModel)target);
		} else if (target.isSimulation()){
			setSim((GSimulation)target);
		}
		
	}
	
	void removeRefs(GElement target){
		if(target.isModel()){
			setModel(null);
		} else if (target.isSimulation()){
			setSim(null);
		}
	}
	@Override
	public String getType() {
		return SEDMLTags.TASK_TAG;
	}

	

}
