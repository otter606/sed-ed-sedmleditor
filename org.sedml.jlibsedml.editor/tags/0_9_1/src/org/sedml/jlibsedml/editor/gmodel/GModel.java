package org.sedml.jlibsedml.editor.gmodel;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.sedml.Model;
import org.sedml.SEDMLTags;
import org.sedml.execution.ArchiveModelResolver;
import org.sedml.execution.FileModelResolver;
import org.sedml.execution.ModelResolver;
import org.sedml.modelsupport.BioModelsModelsRetriever;

public class GModel extends GElement {
	
	private String  source, language;

	private List<GChange> changes = new ArrayList<GChange>();
	
	
	/**
	 * Constructor for generating from SED-ML model.
	 * @param m A non-null <code>Model</code> element.
	 */
	public GModel(Model m) {
		super(m);
		setId(m.getId());
		setName(m.getName());
		setSource(m.getSource());
		setLanguage(m.getLanguage());
	}

	/**
	 * Default constructor for de novo creations
	 */
	public GModel() {
		
	}

	/**
	 * Copy constructor - just copies attributes
	 * @param tocopy
	 */
	 GModel(GModel tocopy) {
		super(tocopy);
		setSource(tocopy.getSource());
		setLanguage(tocopy.getLanguage());
		
	}

	public boolean addChange(GChange c){
		c.setModel(this);
		return changes.add(c);
	
	}
	
	public boolean removeChange(GChange c){
		c.setModel(null);
		return changes.remove(c);
		
	}
	
	/**
	 * Subclasses can set up cross-references here if need be, default does nothing.
	 * @param target
	 */
	 void setUpRefs(GElement target) {
		 if(target.isChange()){
			 addChange((GChange)target);
		 } 
	 }
	 
	 void removeRefs(GElement target) {
		 if(target.isChange()){
			 removeChange((GChange)target);
		 }
			
		}
	
	
	
	public List<GChange> getChanges() {
		return Collections.unmodifiableList(changes);
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
		firePropertyChange(PropertyChangeNames.PROPERTY_EVENT, null, source);
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
		firePropertyChange(PropertyChangeNames.PROPERTY_EVENT, null, source);
	}

	@Override
	public boolean canGetSedML() {
		return getId()!=null && getLanguage()!=null && getLanguage().length()>0 && 
	     getSource()!=null &&getSource().length()>0;
	}

	Model getSEDMLObject() {
		assert(canGetSedML());
		Model m = new Model(getId(),getName(),getLanguage(),getSource());
		m.addAnnotation(createGraphicalInfoAnnotations());
		addNotes(m);
		return m;
	}
	@Override
	public String getType() {
		return SEDMLTags.MODEL_TAG;
		}
	
	public String getModelAsString(){
		
	String rc=null;
	if(getSource()!=null){
		
		
		if(  canGetSedML() && getParent()!=null){
			if(WindowsFileRetriever.isWindows()) {
				setSource(WindowsFileRetriever.convertAbsoluteFilePathToURI(getSource()));;
			}
			ModelResolver mr = new ModelResolver(getParent().buildSEDML());
			mr.add(new FileModelResolver());
			if(getParent().isSedxArchive()){
				mr.add(new ArchiveModelResolver(getParent().getArchiveComponents()));
			}
			if(WindowsFileRetriever.isWindows()){
				mr.add(new WindowsFileRetriever());
			}
			mr.add(new BioModelsModelsRetriever());
		
			
			
			
			rc =mr.getModelString(getSEDMLObject());
		}
	}
	return rc;
	}
	
	

	public Document getModelDocument() {
		Document rc = null;
		SAXBuilder builder = new SAXBuilder();
		try {
			String modelStr= getModelAsString();
			if(modelStr== null|| modelStr.equals("")) {
				return null;
			}
			rc = builder.build(new StringReader(modelStr));
		} catch (JDOMException e) {
		
			e.printStackTrace();
			return null;
		} catch (IOException e) {
		
			e.printStackTrace();
			return null;
		}
		return rc;
		
	}

	@Override
	public GModel getCopy() {
		return new GModel(this);
	}
	
	
	

}
