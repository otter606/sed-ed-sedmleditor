package org.sedml.jlibsedml.editor.gmodel;

public class PropertyChangeNames {
	
	// fire this event when the location of a map object is changed
	public static final String LOCATION_EVENT= "LOCATION_CHANGE_EVENT";
	
	// fire this event when the location of a map object is changed
	public static final String SIZE_EVENT    = "SIZE_CHANGE_EVENT";
	
	// fire this event when the foreground  of a map object is changed
	public static final String FGCOLOUR_EVENT= "FGCOLOUR_EVENT";
	
	// fire this event when the background  of a map object is changed
	public static final String BGCOLOUR_EVENT= "BGCOLOUR_EVENT";
	
	// fire this event when the linestyle  of a map object is changed
	public static final String LINESTYLE_EVENT= "LINESTYLE_EVENT";
	
	// fire this event when a child is added to a map object or map
	public static final String CHILD_ADD_EVENT= "CHILD_ADD_EVENT";
    
	// fire this event when a child  is removed from a map or map item
	public static final String CHILD_REMOVE_EVENT = "CHILD_REMOVE_EVENT";

	public static final String SOURCE_CONNECTIONS_PROP = "SRC_ADD_EVENT";
	
	public static final String TARGET_CONNECTIONS_PROP = "TARGET_ADD_EVENT";

	public static final String PROPERTY_EVENT = "PROPERTY_EVENT";

	public static final String CHILD_PASTE_EVENT = "CHILD_PASTE_EVENT";

	public static final String HIDE_ELEMENT_EVENT = "HIDE_ELEMENT_EVENT";
	
	public static final String SHOW_ELEMENT_EVENT = "SHOW_ELEMENT_EVENT";

}
