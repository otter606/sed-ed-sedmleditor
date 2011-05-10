package org.sedml.jlibsedml.editor.gmodel;

import org.jdom.Document;
import org.sedml.SEDMLTags;
import org.sedml.Variable;
import org.sedml.VariableSymbol;

public class GVariable  extends GElement{
	
	
	private String targetXPath;
	private VariableSymbol symbol;
	private GTask task=null;
	private GModel model=null;
	public GVariable(Variable v) {
		super(v);
		setId(v.getId());
		setName(v.getName());
		setTargetXPath(v.getTarget());
		setSymbol(v.getSymbol());
	}
	public GVariable() {
		// TODO Auto-generated constructor stub
	}
	public GModel getModel() {
		return model;
	}
	public void setModel(GModel model) {
		this.model = model;
		firePropertyChange(PropertyChangeNames.PROPERTY_EVENT, null, model);
	}
	public GTask getTask() {
		return task;
	}
	public void setTask(GTask task) {
		this.task = task;
		firePropertyChange(PropertyChangeNames.PROPERTY_EVENT, null, model);
	}

	public String getTargetXPath() {
		return targetXPath;
	}
	public void setTargetXPath(String targetXPath) {
		this.targetXPath = targetXPath;
		firePropertyChange(PropertyChangeNames.PROPERTY_EVENT, null, targetXPath);
	}
	public VariableSymbol getSymbol() {
		return symbol;
	}
	public void setSymbol(VariableSymbol symbol) {
		this.symbol = symbol;
		firePropertyChange(PropertyChangeNames.PROPERTY_EVENT, null, symbol);

		
	}
	@Override
	public boolean canGetSedML() {
		return getId()!=null&& (getTargetXPath()!=null || getSymbol()!=null) && 
		 (getTask()!=null || getModel()!=null);
	}
	
	private Variable createVar() {
		Variable rc=null;
		String ref=null;
		if( getTask()!=null){
			ref= getTask().getId();
		}else if (getModel()!=null){
			ref= getModel().getId();
		}
		if (getTargetXPath() != null) {
			rc= new Variable(getId(), getName(), ref, getTargetXPath());
		} else {
			rc= new Variable(getId(), getName(), ref, getSymbol());
		}
		rc.addAnnotation(createGraphicalInfoAnnotations());
		addNotes(rc);
		return rc;
	}
	
	Variable getSEDMLObject() {
		assert(canGetSedML());
		return createVar();
	}
	
	@Override
	void setUpRefs(GElement target) {
		if(target.isTask()){
			setTask((GTask)target);
		} else if (target.isModel()){
			setModel((GModel)target);
		}
		
	}
	
	void removeRefs(GElement target){
		if(target.isModel()){
			setModel(null);
		} else if (target.isTask()){
			setTask(null);
		}
	}
	@Override
	public String getType() {
		return SEDMLTags.DATAGEN_ATTR_VARIABLE;
	}
	public boolean canGetModel() {
		return getModelDocument()!=null;
	}
	
	
	public Document getModelDocument() {
		Document rc = null;
		if(getTask()!=null){
			GTask t = getTask();
			if(t.getModel()!=null){
				GModel m = t.getModel();
				rc=m.getModelDocument();
				}
			}
			

		return rc;
	}
	

}
