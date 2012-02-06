package org.sedml.jlibsedml.editor.gmodel;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * Equivalent to a draw2d 'Dimension' object.<br>
 * This class is final and immutable.
 * @author Richard Adams
 *
 */
@XmlType(name="size", namespace="http://www.sedml.sbsi.editor/level1")
public final class Size  {
	
public Size(){}


	
@XmlAttribute(name="width")
private  int width;

@XmlAttribute(name="height")
private  int height;
	
	/**
	 * Convenience constructor to get a  Size object
	 * @param width
	 * @param height
	 */
	public Size (int width, int height){
		this.width = width;
		this.height= height;
	}
	
	
	/* (non-Javadoc)
	 * @see org.pathwayeditor.businessobjects.Size#getWidth()
	 */
	public int getWidth () {
		return width;
	}
	
	/* (non-Javadoc)
	 * @see org.pathwayeditor.businessobjects.Size#getHeight()
	 */
	public int getHeight(){
		return height;
	}
	

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Size other = (Size) obj;
		if (height != other.height)
			return false;
		if (width != other.width)
			return false;
		return true;
	}

	public String toString() {
		return new StringBuffer().append(getClass().toString())
		.append("[").append(width).append(",").append(height).append("]")
		 .toString();
	}

	
	/**
	 * Shrinks this size by the given parameters. It is the clients responsibility
	 * to deal with the cases where the size becomes negative,.
	 * @param x amount to shrink x in pixels
	 * @param y amount to shrink y in pixels 
	 * @return A copy of this object as a convenience.
	 */
	public Size shrink (int x, int y){
		return new Size(this.width - x ,this.height -y);
	}
	
	/**
	 * Expands this size by the given parameters. It is the clients responsibility
	 * to deal with the cases where the size becomes negative,.
	 * @param x amount to expand x in pixels
	 * @param y amount to expand y in pixels 
	 * @return A copy of this object as a convenience.
	 */
	public Size  expand (int x, int y){
		return shrink(-x, -y);
	}
    
	
	

}
