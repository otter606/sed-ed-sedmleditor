package org.sedml.jlibsedml.editor.gmodel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

 class PropListenerStub implements PropertyChangeListener {

	PropertyChangeEvent evt;
	public void propertyChange(PropertyChangeEvent evt) {
		this.evt=evt;

	}

}
