package org.sedml.jlibsedml.editor.gmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sedml.ArchiveComponents;
import org.sedml.SedML;

/**
 * Top-level graphical element, corresponds to SedML element.
 * @author radams
 *
 */
public class GSedML implements IPropertyChangeSupport {
	
	
	private PropertyChangeSupport propSupport;
	public GSedML() {
		super();
		propSupport = new PropertyChangeSupport(this);
	}

	
	
	
	
	private List<GElement> children = new ArrayList<GElement>();
	private long idStart=0;
	private boolean isSedx;
	
	private ArchiveComponents content;
	
	
	
	public boolean addChild(GElement child) {
		child.setParent(this);
		return children.add(child);
		
	}
	
	public boolean removeChild(GElement child) {
		child.setParent(null);
		return children.remove(child);
	}
	
	public List<GElement> getChildren(){
		return Collections.unmodifiableList(children);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener){
		propSupport.addPropertyChangeListener(listener);
	}
	
	
	public void firePropertyChange(String property,Object oldValue,Object newValue){
		propSupport.firePropertyChange(property, oldValue, newValue);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propSupport.removePropertyChangeListener(listener);
	}
	
	/**
	 * Moves a child element to a new location in its collection. It is the responsibility of
	 * clients to ensure that the parameter child is indeed a child, and that
	 * the new index is a legal index for the <code>List</code> of child
	 * objects. 
	 * 
	 * @return <code>true</code> if the child index is set correctly
	 * @throws IllegalArgumentException
	 *             if index is outside valid bounds, or child to be moved is not
	 *             a child of this MapObject
	 *@author Richard Adams
	 */
	public boolean setChildIndex(GElement child, int newIndex) {
		if (newIndex < 0 || newIndex > children.size() - 1 || !children.contains(child)) {
			throw new IllegalArgumentException();
		}
		boolean rc = children.remove(child);
		children.add(newIndex, child);
		
		return rc;

	}


    static Pattern ID_PATTERN=Pattern.compile("\\d+$");
    static String PREFIX="element";
    /**
     * Generates an ID by scanning current child elements for their
     * id values, and generates an id with a numerical suffix. <br>
     * Repeated calls to this method will return the same ID if no children have been added.
     * @return A non-empty <code>String</code>.
     */
	public  synchronized String generateIDForChildElement() {
		long currBiggest=0;
		for (GElement child: getChildren()){
			if(child.getId()!=null) {
			Matcher m = ID_PATTERN.matcher(child.getId());
			if(m.find()){
				long l = Long.parseLong(m.group());
				if(l>currBiggest){
					currBiggest=l;
				}
			}
			}
		}
		return PREFIX+ ++currBiggest;
	}

	/**
	 * Generates a {@link SedML} object from the graphical layer.
	 * @return A {@link SedML} object
	 */
	public SedML buildSEDML() {
		return new SEDMLBuilder().buildSEDML(this);
	}

	/**
	 * Gets a modifiable <code>List</code> which is a subset of all the child elements with graphical
	 *    information.
	 * @return A possibly empty but non-null <code>List</code> of GElements
	 */
	public List<GElement> getChildrenWithGraphicalInfo() {
	List<GElement>rc = new ArrayList<GElement>();
		for (GElement child: getChildren()){
			if(child.getLocation()!=null && !child.getLocation().equals(Location.ORIGIN)){
				rc.add(child);
			}
		}
		return rc;
	}
	
	/**
	 * Boolean test for whether all child elements have alayout or not:
	 * @return <code>true</code> if > 0 child  elements have no graphical information associated with them.
	 */
	public boolean hasIncompleteLayout (){
		return getChildrenWithGraphicalInfo().size() < getChildren().size();
	}
	
    boolean isSedxArchive(){
    	return isSedx;
    }
    
    public void setIsSEDX(boolean isSedxArchive){
    	this.isSedx=isSedxArchive;
    }
    
    /**
     * 
     * @return an {@link ArchiveComponents} if this element has been read in from a SEDX archive (i.e., isSedxArchive()===true).
     *   or <code> null</code> otherwise.
     */
    public ArchiveComponents getArchiveComponents(){
    	return content;
    }
    
    /**
     * Setter for {@link ArchiveComponents} which should be called along with setSedxArchive(true);
     * @param content
     */
    public void setArchiveComponents(ArchiveComponents content) {
    	 this.content=content;
    }

}
