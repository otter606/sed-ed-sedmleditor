package org.sedml.jlibsedml.editor.gmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.sedml.Annotation;
import org.sedml.Notes;
import org.sedml.SEDBase;
import org.sedml.SEDMLTags;
import org.sedml.XMLException;
import org.sedml.jlibsedml.xmlutils.XMLHandler;

public abstract class GElement implements IPropertyChangeSupport {

	public static final String HTTP_WWW_SEDML_SBSI_EDITOR_LEVEL1 = "http://www.sedml.sbsi.editor/level1";
	private Location loc = Location.ORIGIN;
	private Size size =DEFAULT_SIZE;;
	
	private String id,name;
	private Notes notes;
	static final int LOCATION_OFFSET=5;
	
	/**
	 * Default constructor sets size to default size but does not 
	 *  initialize any fields.
	 */
	public GElement () {
		
		propChSupport = new PropertyChangeSupport(this);
		setSize(DEFAULT_SIZE);
		
	}
	
	public GElement (GElement toCopy) {
		this();
		if (toCopy.getNotes()!=null){
			Element cloned = (Element)toCopy.getNotes().getNotesElement().clone();
			Notes clonedN = new Notes(cloned);
			setNotes(clonedN);
		}
		setLocation( new Location (toCopy.getLocation().getX() + LOCATION_OFFSET,toCopy.getLocation().getY() + LOCATION_OFFSET) );
		setSize(toCopy.getSize());
		setName(toCopy.getName());
		
		
	}
	
	public abstract GElement getCopy();
	
	


	/**
	 * Copy constructor to populate notes and annotations from a {@link SEDBase} object.
	 * @param sedbase A {@link SEDBase} object.
	 */
	public GElement(SEDBase sedbase) {
		this();
		if(sedbase.getNotes().size()>=1){
			setNotes(sedbase.getNotes().get(0));
		}
		getGraphicalInfo(sedbase);
	}

	public static final Size DEFAULT_SIZE= new Size(80,45);
	public List<Connection> getSrcConnections() {
		return new ArrayList<Connection>(srcConns);
	}

	public List<Connection> getTargetConnections() {
		return new ArrayList<Connection>(targConns);
		
	}

	public abstract String getType();
	
	
	public void hide(){
		firePropertyChange(PropertyChangeNames.HIDE_ELEMENT_EVENT, null, notes);
	}
	
	public void show() {
		firePropertyChange(PropertyChangeNames.SHOW_ELEMENT_EVENT, null, notes);
	}
	
	public Notes getNotes() {
		return notes;
	}

	public void setNotes(Notes notes) {
		this.notes = notes;
		firePropertyChange(PropertyChangeNames.PROPERTY_EVENT, null, notes);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		firePropertyChange(PropertyChangeNames.PROPERTY_EVENT, null, id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		firePropertyChange(PropertyChangeNames.PROPERTY_EVENT, null, name);
	}

	private PropertyChangeSupport propChSupport;
	private GSedML parent;
	private List<Connection>srcConns = new ArrayList<Connection>();
	private List<Connection>targConns = new ArrayList<Connection>();
	
	/**
	 * Add an incoming or outgoing connection to this shape.
	 * 
	 * @param conn
	 *            a non-null connection instance
	 * @throws IllegalArgumentException
	 *             if the connection is null or has not distinct endpoints
	 */
	void addConnection(Connection conn) {
		if (conn == null || conn.getSource() == conn.getTarget()) {
			throw new IllegalArgumentException();
		}
		if (conn.getSource() == this) {
			srcConns.add(conn);
			setUpRefs(conn.getTarget());
			firePropertyChange(PropertyChangeNames.SOURCE_CONNECTIONS_PROP, null, conn);
		} else if (conn.getTarget() == this) {
			setUpRefs(conn.getTarget());
			targConns.add(conn);
			firePropertyChange(PropertyChangeNames.TARGET_CONNECTIONS_PROP, null, conn);
		}
		
	}
	/**
	 * Subclasses can set up cross-references here if need be, default does nothing.
	 * @param target
	 */
	 void setUpRefs(GElement target) {}

	public boolean isConnected (){
		return srcConns.size()>0 || targConns.size() >0;
	}
	
	/**
	 * Remove an incoming or outgoing connection from this shape.
	 * 
	 * @param conn
	 *            a non-null connection instance
	 * @throws IllegalArgumentException
	 *             if the parameter is null
	 */
	void removeConnection(Connection conn) {
		if (conn == null) {
			throw new IllegalArgumentException();
		}
		if (conn.getSource() == this) {
			srcConns.remove(conn);
			firePropertyChange(PropertyChangeNames.SOURCE_CONNECTIONS_PROP, null, conn);
		} else if (conn.getTarget() == this) {
			targConns.remove(conn);
			firePropertyChange(PropertyChangeNames.TARGET_CONNECTIONS_PROP, null, conn);
		}
		removeRefs(conn.getTarget());
	}
	
	 void removeRefs(GElement gElement) {
		// TODO Auto-generated method stub
		
	}

	
	public Location getLocation() {
		return loc;
	}

	public void setLocation(Location loc) {
		this.loc = loc;
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener){
		propChSupport.addPropertyChangeListener(listener);
	}
	
	
	public void firePropertyChange(String property,Object oldValue,Object newValue){
		propChSupport.firePropertyChange(property, oldValue, newValue);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propChSupport.removePropertyChangeListener(listener);
	}

	public void setParent(GSedML gSedML) {
		this.parent=gSedML;
		
	}
	
	public GSedML  getParent(){
		return parent;
	}
	
	

	public boolean isModel() {
		return this instanceof GModel;
	}
	
	public boolean isSimulation() {
		return this instanceof GSimulation;
	}
	
	public boolean isTask() {
		return this instanceof GTask;
	}
	
	public boolean isDataGenerator() {
		return this instanceof GDataGenerator;
	}
	/*
	 * Boolean test for whether a Graphical model object is populated with
	 *  sufficient info to create a jlibsedml object.
	 */
	public abstract boolean canGetSedML();
	
	/*
	 * Gets or creates a new jlibsedml object, if canGetSedML()==true
	 *  e.g., if it's been populated from a valid file rather than de-novo.
	 */
	abstract SEDBase getSEDMLObject();
	
	static Marshaller createMarshaller(Class clazz) throws JAXBException{
		
		JAXBContext jc = JAXBContext.newInstance(clazz);
		 
	    Marshaller marshaller = jc.createMarshaller();
	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    return marshaller;
		
	}

	 void setGraphicalAnnotations(Annotation el) throws JAXBException{
		
		JAXBContext jc = JAXBContext.newInstance(Container.class); 
	    Unmarshaller unm =  jc.createUnmarshaller();
	    XMLHandler xmlh = new XMLHandler();
	    String annStr = xmlh.getElementAsString(el.getAnnotationElement());
	    Container c = (Container)unm.unmarshal(new StringReader(annStr));
	    setLocation(c.getLoc());
	    setSize(c.getSize());
		
	}

	final Annotation createGraphicalInfoAnnotations(){
	
		Container c = new Container(getLocation(),getSize());	 
        Marshaller marshaller=null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			marshaller = createMarshaller(Container.class);
			 marshaller.marshal(c, baos);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String out=(new String (baos.toByteArray()));
        Element el=null;
		try {
			el = getElement(out);
		} catch (XMLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return new Annotation(el);
		
	}
	
	 void addNotes(SEDBase sedbase) {
		if(notes!=null){
			sedbase.addNote(notes);
		}
		
	}
	
	
	private Element getElement(String xml) throws XMLException {
	SAXBuilder builder = new SAXBuilder();
    
	org.jdom.Document doc;
	try {
		doc = builder.build(new StringReader(xml));
		
	} catch (JDOMException e) {
		throw new XMLException("Error reading file", e);
	} catch (IOException e) {
		throw new XMLException("Error reading file", e);
	}
	return doc.detachRootElement();
	}
	
	public boolean isOutput() {
		return this instanceof GOutput;
	}

	public boolean isVariable() {
		return this instanceof GVariable;
	}

	public boolean isParameter() {
		return this instanceof GParameter;
	}

	public boolean isReport() {
		return this instanceof GReport;
	}
	public boolean isPlot2D() {
		return this instanceof GPlot2D;
	}
	public boolean isCurve() {
		return this instanceof GCurve;
	}
	
	public boolean isDataset() {
		return this instanceof GDataset;
	}
	
	public boolean isComputeChange() {
		return isChange() && ((GChange)this).getChType().equals(SEDMLTags.COMPUTE_CHANGE_KIND);
	}
	public boolean isChangeAttribute() {
		return isChange() && ((GChange)this).getChType().equals(SEDMLTags.CHANGE_ATTRIBUTE_KIND);
	}
	
	public boolean isModifyXMLChange() {
		return isChange() && (
				((GChange)this).getChType().equals(SEDMLTags.CHANGE_XML_KIND) ||
				((GChange)this).getChType().equals(SEDMLTags.ADD_XML_KIND));
	}
	public boolean isChange() {
		return this instanceof GChange;
	}
	
	 void getGraphicalInfo(SEDBase sb) {
		Annotation ann = getGraphAnnotations(sb);
		if(ann!=null){
			try {
				setGraphicalAnnotations(ann);
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	Annotation getGraphAnnotations(SEDBase sbase){
		for (Annotation ann: sbase.getAnnotation()){
			if(HTTP_WWW_SEDML_SBSI_EDITOR_LEVEL1.equals(ann.getAnnotationElement().getNamespaceURI())){
				return ann;
			}
		}
		return null;
	}




	/**
	 * Boolean test for whether this object can access the model referenced
	 *  in the  SEDML model element.
	 * @return <code>true</code> if the model can be accessed.
	 */
	public boolean canGetModel() {
		return getModelDocument() != null;
	}
	
	/**
	 * Subclasses can override. This method should return <code>null</code>
	 *  if <code>canGetModel() == false</code>. This default implementation
	 *   returns <code>null</code>.
	 * @return  A {@link org.jdom.Document} object or <code> null</code> if
	 *   model could not be identified.
	 */
	public Document getModelDocument() {
		return null;
	}
	/**
	 * Concise Information for display in UI about this element. Subclasses can override. By default this
	 *  returns the ID of the element.
	 * @return A <code>String</code>.
	 */
	public String getDisplay1(){
		return "ID: " + getId();
	}
	
	/**
	 * Concise information for display in secondary UI location about this element. Subclasses can override. By default this
	 *  returns the name of this element.
	 * @return A <code>String</code>.
	 */
	public String getDisplay2(){
		return "Name: " + getName();
	}




	




	

}
