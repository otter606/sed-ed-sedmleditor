package org.sedml.jlibsedml.editor.gmodel;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.sedml.IModelContent;
import org.sedml.Model;
import org.sedml.SEDMLTags;
import org.sedml.execution.FileModelResolver;

public class GModel extends GElement {
	
	private String  source, language;

	private List<GChange> changes = new ArrayList<GChange>();

	
	public GModel(Model m) {
		super(m);
		setId(m.getId());
		setName(m.getName());
		setSource(m.getSource());
		setLanguage(m.getLanguage());
	}

	public GModel() {
		// TODO Auto-generated constructor stub
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
		String src = getSource();
		FileModelResolver fmr = new FileModelResolver();
		try {
			rc = fmr.getModelXMLFor(new URI(src));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		if(rc == null && getParent().isSedxArchive()){
			List<IModelContent> children = getParent().getSEDXModels();
			for (IModelContent imc: children) {
				if(imc.getName().equals(src)){
					rc= imc.getContents();
				}
			}
		}
	}
	return rc;
	}
	
	public Document getModelDocument() {
		Document rc = null;
		SAXBuilder builder = new SAXBuilder();
		try {
			String modelStr= getModelAsString();
			if(modelStr== null) {
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
	
	
	

}
