package org.sedml.jlibsedml.editor.gmodel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * Container root element for graphical attributes to persist.
 * @author radams
 *
 */
@XmlRootElement(name="sbsi-editor",namespace="http://www.sedml.sbsi.editor/level1")
public class Container {
	public Location getLoc() {
		return loc;
	}

	public Size getSize() {
		return size;
	}

	Container(Location loc, Size size) {
		super();
		this.loc = loc;
		this.size = size;
	}

	public Container(){}
	@XmlElement(name="location", namespace="http://www.sedml.sbsi.editor/level1")
	private Location loc;
	
	@XmlElement(name="size",namespace="http://www.sedml.sbsi.editor/level1")
	private Size size;
	
	
}
