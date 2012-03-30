package org.sedml.jlibsedml.editor.gmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jdom.Element;
import org.jlibsedml.DataGenerator;
import org.jlibsedml.SEDMLTags;
import org.jmathml.ASTNode;
import org.jmathml.ASTToXMLElementVisitor;
import org.jmathml.MathMLReader;

public class GDataGenerator extends GElement {

	private List<GVariable> vars=new ArrayList<GVariable>();
	private List<GParameter> params = new ArrayList<GParameter>();
	private ASTNode math=null;
	/**
	 * Used when reading from document
	 * @param dg
	 */
	public GDataGenerator(DataGenerator dg) {
		super(dg);
		setId(dg.getId());
		setName(dg.getName());
		setMath(dg.getMath());
	}
	/**
	 * For de novo creation on palette.
	 */
	public GDataGenerator() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Copy constructor
	 * @param gDataGenerator
	 */
	 GDataGenerator(GDataGenerator toCopy) {
		super(toCopy);
		if(toCopy.getMath()!=null)
			setMath(copyMath(toCopy));
	}
	 
	 private ASTNode copyMath(GDataGenerator toCopy) {
			ASTToXMLElementVisitor astElementVisitor= new ASTToXMLElementVisitor();
			toCopy.getMath().accept(astElementVisitor);
			Element el =astElementVisitor.getElement();
			ASTNode copied = new MathMLReader().parseMathML(el);
			return copied;
		}
	public ASTNode getMath() {
		return math;
	}
	public void setMath(ASTNode math){
		this.math=math;
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
	public boolean canGetSedML() {
		
			return getId()!=null&& getMath()!=null;
		
	}
	@Override
	DataGenerator getSEDMLObject() {
		assert(canGetSedML());
		DataGenerator dg= new DataGenerator(getId(),getName(),getMath());
		dg.addAnnotation(createGraphicalInfoAnnotations());
		if(getNotes()!=null)
			addNotes(dg);
		for(GParameter gp: params){
			if(gp.canGetSedML()){
				dg.addParameter(gp.getSEDMLObject());
			}
		}
		for(GVariable v: vars){
			if(v.canGetSedML()){
				dg.addVariable(v.getSEDMLObject());
			}
		}
		return dg;
	}
	
	/**
	 * Subclasses can set up cross-references here if need be, default does nothing.
	 * @param target
	 */
	 void setUpRefs(GElement target) {
		 if(target.isVariable()){
			 addVar((GVariable)target);
		 } else if(target.isParameter()){
			 addParameter((GParameter)target);
		 }
	 }
	 
	 void removeRefs(GElement gElement) {
		 if(gElement.isVariable()){
			 removeVar((GVariable)gElement);
		 }else if(gElement.isParameter()){
			removeParameter((GParameter)gElement);
		 }
			
		}
	@Override
	public String getType() {
		return SEDMLTags.DATAGENERATOR_TAG;
	}
	@Override
	public GDataGenerator getCopy() {
		return new GDataGenerator(this);
	}
}
