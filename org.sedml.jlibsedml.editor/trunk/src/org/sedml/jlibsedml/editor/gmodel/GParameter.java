package org.sedml.jlibsedml.editor.gmodel;

import org.sedml.Parameter;
import org.sedml.SEDMLTags;

public class GParameter  extends GElement{
	
  private double value;

public GParameter(Parameter p) {
	super(p);
	setValue(p.getValue());
	setId(p.getId());
	setName(p.getName());
}

public GParameter() {
	// TODO Auto-generated constructor stub
}

public double getValue() {
	return value;
}

public void setValue(double value) {
	this.value = value;
}

@Override
public boolean canGetSedML() {
	return getId()!=null;
}


Parameter getSEDMLObject() {
	assert(canGetSedML());
	Parameter p= new Parameter(getId(), getName(), getValue());
	p.addAnnotation(createGraphicalInfoAnnotations());
	addNotes(p);
	return p;
}
@Override
public String getType() {
	return SEDMLTags.DATAGEN_ATTR_PARAMETER;
}
	

}
