package org.sedml.jlibsedml.editor.gmodel;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * Locations are  immutable and do not have setX() and setY() methods. <br>
 * To alter a location, create a new Location object.
 * The following constants are defined:<ul>
 * <li> Location.ORIGIN (location at 0,0).
 * </ul>
 * Not designed for subclassing.
 * @author Richard Adams
 *
 */
@XmlType(name="location", namespace="http://www.sedml.sbsi.editor/level1")
public final class Location {
	/**
	 * For JAXB. Not to be used by clients.
	 */
	public Location(){}
	
	@XmlAttribute(name="x")
	private  int x;
	
	@XmlAttribute(name="y")
	private  int y;

    
	/**
	 * Convenience 
	 */
	public static final Location ORIGIN = new Location(0,0);
	
	
	/**
	 * Convenience constructor. X and y values are offset from top-left
	 * hand corner of the dispaly area.
	 * @param x an <code>int</code>
	 * @param y an <code>int</code>
	 */
	public Location (int x, int y){
		this.x = x;
		this.y = y;
		
	}
	
	
	

	/* (non-Javadoc)
	 * @see org.pathwayeditor.businessobjects.ILocation#getX()
	 */
	public int getX(){
		return x;
	}
	
	/* (non-Javadoc)
	 * @see org.pathwayeditor.businessobjects.ILocation#getY()
	 */
	public int getY(){
		return y;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		final Location other = (Location) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}



	public String toString (){
		return new StringBuffer().append(getClass().toString()).
					append("x :" + x).
					append("y:" +y).
					toString();
	}
	
	/**
	 * Calculates a new {@link Location} of this objects' translated location.
	 * @param dx
	 * @param dy
	 * @return A copy of this object
	 */
	public Location translate(int dx, int dy) {
		return new Location(x + dx, y+dy);
		
	}
	public Location translate(Location loc) {
		return translate( loc.getX(), loc.getY() );
		
	}
	
	/**
	 * Gets the difference between this location and the parameter location expressed as a <code>Size</code object.
	 * E..g., <code> new Location(20,20).getDifference (new Location(10,0))</code><br>
	 * will return a Size object width 10, height 20.
	 * @param loc2 
	 * @return A {@link Size}object
	 */
	public Size getDifference (Location loc2) {
		return new Size(getX() - loc2.getX(), getY() - loc2.getY());
	}
	
	/**
	 * Calculates the Euclidian distance between this object and the argument <code>Location</code>
	 * @param loc2 A {@link Location} object
	 * @return a <code>double</code> of the distance in the coordinate system.
	 */
	public double getDistance(Location loc2) {
		return Math.sqrt(Math.pow((getX() - loc2.getX()),2) + Math.pow((getY() - loc2.getY()),2));
	}
	
	/**
	 * Returns the midpoint of this Location and the Location argument. This method is transitive.
	 * I.e., loc2.getMidpoint(loc1) will return the same result as loc1.getMidpoint(loc2);
	 * @param loc2 A {@link Location} objects
	 * @return A new {@link Location} representing the midpoint.
	 */
	public Location getMidPoint (Location loc2) {
		return new Location((int)((getX() + loc2.getX()) /2),(int)((getY() + loc2.getY()) /2));
				               
	}
	
	
	
	

}
