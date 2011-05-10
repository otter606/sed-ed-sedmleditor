package org.sedml.jlibsedml.editor.gmodel;

import org.sedml.Output;

public abstract class GOutput extends GElement {
	public GOutput(Output o) {
		super(o);
	}

	public GOutput() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canGetSedML() {
		return getId()!=null;
	}
	

}
