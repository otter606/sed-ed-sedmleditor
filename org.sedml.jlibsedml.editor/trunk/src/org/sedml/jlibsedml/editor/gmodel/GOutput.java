package org.sedml.jlibsedml.editor.gmodel;

import org.jlibsedml.Output;


public abstract class GOutput extends GElement {
	public GOutput(Output o) {
		super(o);
	}

	public GOutput() {
		// TODO Auto-generated constructor stub
	}

	 GOutput(GOutput toCopy) {
		super(toCopy);
	}

	@Override
	public boolean canGetSedML() {
		return getId()!=null;
	}
	

}
