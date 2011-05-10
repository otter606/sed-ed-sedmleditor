package org.sedml.jlibsedml.editor.gmodel;

import java.beans.PropertyChangeListener;

public interface IPropertyChangeSupport {

	
		

		void addPropertyChangeListener(PropertyChangeListener listener);
		
		
		void firePropertyChange(String property,Object oldValue,Object newValue);
		
		void removePropertyChangeListener(PropertyChangeListener listener);
	


}
