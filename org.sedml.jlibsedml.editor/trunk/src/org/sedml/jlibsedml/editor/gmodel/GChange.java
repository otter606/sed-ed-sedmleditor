package org.sedml.jlibsedml.editor.gmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jmathml.ASTNode;
import org.jmathml.ASTToXMLElementVisitor;
import org.jmathml.MathMLReader;
import org.sedml.AddXML;
import org.sedml.Change;
import org.sedml.ChangeAttribute;
import org.sedml.ChangeXML;
import org.sedml.ComputeChange;
import org.sedml.NewXML;
import org.sedml.RemoveXML;
import org.sedml.SEDMLTags;
import org.sedml.XPathTarget;

public  class GChange extends GElement {
	public static final String CHANGE = "Change";
	private List<GVariable> vars=new ArrayList<GVariable>();
	private List<GParameter> params = new ArrayList<GParameter>();

	private XPathTarget target;
	private NewXML newxml;
	private String newValue;
	private ASTNode math;
	private GModel owner;
	public GModel getOwner() {
		return owner;
	}

	 GChange (GChange toCopy) {
		super(toCopy);
		setChType(toCopy.getChType());
		if(toCopy.getMath()!=null){
			ASTNode copied = copyMath(toCopy);
			setMath(copied);
		}
		setNewValue(toCopy.getNewValue());
		if(toCopy.getNewxml()!=null) {
			setNewxml(copyNewXML(toCopy.getNewxml()));
		}
		
		setTarget(toCopy.getTarget());
		
	}

	public GChange getCopy() {
		GChange copy = new GChange(this);
		return copy;
	}
	private NewXML copyNewXML(NewXML newxml2) {
		
			List<Element> newels = new ArrayList<Element>();
			for (Element el: newxml2.getXml()){
				newels.add((Element)el.clone());
			}
			return new NewXML(newels);
		
	}

	private ASTNode copyMath(GChange toCopy) {
		ASTToXMLElementVisitor astElementVisitor= new ASTToXMLElementVisitor();
		toCopy.getMath().accept(astElementVisitor);
		Element el =astElementVisitor.getElement();
		ASTNode copied = new MathMLReader().parseMathML(el);
		return copied;
	}
	public void setOwner(GModel owner) {
		this.owner = owner;
	}

	public GChange(Change c) {
		super(c);
		setChType(c.getChangeKind());
		setTarget(c.getTargetXPath());
		if(isChangeAttribute()){
			setNewValue(((ChangeAttribute)c).getNewValue());
		}else if(SEDMLTags.CHANGE_XML_KIND.equals(c.getChangeKind()) ){
			setNewxml( ((ChangeXML)c).getNewXML());
		}else if(SEDMLTags.ADD_XML_KIND.equals(c.getChangeKind()) ){
			setNewxml( ((AddXML)c).getNewXML());
		}else if(isComputeChange()){
			ComputeChange cc = (ComputeChange)c;
			setMath(cc.getMath());
		}
	}

	public GChange() {
		// TODO Auto-generated constructor stub
	}

	public ASTNode getMath() {
		return math;
	}

	public void setMath(ASTNode math) {
		this.math = math;
		firePropertyChange(PropertyChangeNames.PROPERTY_EVENT, null, math);
	}

	private String chType = SEDMLTags.CHANGE_ATTRIBUTE_KIND;// default

	public String getChType() {
		return chType;
	}

	/**
	 * One of:
	 * <ul>
	 * <li>SEDMLTags.CHANGE_ATTRIBUTE_KIND
	 * <li>SEDMLTags.CHANGE_XML_KIND
	 * <li>SEDMLTags.ADD_XML_KIND
	 * <li>SEDMLTags.REMOVE_XML_KIND
	 * <li>SEDMLTags.COMPUTE_CHANGE_KIND
	 * </ul>
	 * */
	public void setChType(String chType) {
		this.chType = chType;
		firePropertyChange(PropertyChangeNames.PROPERTY_EVENT, null, math);
	}

	public NewXML getNewxml() {
		return newxml;
	}

	public void setNewxml(NewXML newxml) {
		this.newxml = newxml;
		this.newValue = null;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
		this.newxml = null;
		firePropertyChange(PropertyChangeNames.PROPERTY_EVENT, null, math);
	}

	public XPathTarget getTarget() {
		return target;
	}

	public void setTarget(XPathTarget target) {
		this.target = target;
		firePropertyChange(PropertyChangeNames.PROPERTY_EVENT, null, math);
	}
	
	
	public boolean addVar(GVariable v){
		return vars.add(v);
	}
	
	public boolean addParameter(GParameter p){
		return params.add(p);
	}
	
	public boolean removeVar(GVariable v){
		return vars.remove(v);
	}
	
	public boolean removeParameter(GParameter p){
		return params.remove(p);
	}
	
	public List<GVariable> getVars() {
		return Collections.unmodifiableList(vars);
	}
	public List<GParameter> getParams() {
		return Collections.unmodifiableList(params);
	}
	@Override
	void setUpRefs(GElement target) {
		if(target.isVariable()){
			addVar((GVariable)target);
		} else if (target.isParameter()){
			addParameter((GParameter)target);
		}
		
	}

	@Override
	public boolean canGetSedML() {
		boolean rc= getTarget() != null;
		if(rc == false || getChType().equals(SEDMLTags.REMOVE_XML_KIND)){
			return rc;
		}else rc=getNewValue() != null || getNewxml() != null || getMath()!=null;
		return rc;
	}

	
	/**
	 * @throws IllegalStateException if getChangeType is not a known change type.
	 */
	Change getSEDMLObject() {
		boolean ok = canGetSedML();
		Change rc=null;
		assert(ok==true);
		if(getChType().equals(SEDMLTags.CHANGE_ATTRIBUTE_KIND)){
			rc= new ChangeAttribute(target, newValue);
		}else if(getChType().equals(SEDMLTags.ADD_XML_KIND)){
			rc= new AddXML(target, newxml);
		}else if(getChType().equals(SEDMLTags.CHANGE_XML_KIND)){
			rc= new ChangeXML(target, newxml);
		}else if(getChType().equals(SEDMLTags.REMOVE_XML_KIND)){
			rc= new RemoveXML(target);
		}else if(getChType().equals(SEDMLTags.COMPUTE_CHANGE)){
			rc= new ComputeChange(target,getMath());
			for(GParameter gp: params){
				if(gp.canGetSedML()){
					((ComputeChange)rc).addParameter(gp.getSEDMLObject());
				}
			}
			for(GVariable v: vars){
				if(v.canGetSedML()){
					((ComputeChange)rc).addVariable(v.getSEDMLObject());
				}
			}
			
		}else {
			throw new IllegalStateException();
		}
		rc.addAnnotation(createGraphicalInfoAnnotations());
		addNotes(rc);
		return rc;
		
	
	}

	@Override
	public String getType() {
		return CHANGE;
	}
	
	public boolean canGetModel() {
	     return owner != null &&
	     owner.getModelDocument()!=null;
	}




	public String getModelAsString() {
		return owner.getModelAsString();
	}
	
	public Document getModelDocument() {
		return owner.getModelDocument();
	}

	public void setModel(GModel gModel) {
		this.owner=gModel;
		
	}
	
	public String getDisplay1(){
		return "Type: " + chType;
	}
	
	public String getDisplay2(){
		return "New val: " + newValue;
	}

	

}
