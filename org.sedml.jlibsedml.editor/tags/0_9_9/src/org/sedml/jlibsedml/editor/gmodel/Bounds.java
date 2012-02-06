package org.sedml.jlibsedml.editor.gmodel;



/**
 * Equivalent to a draw2d rectangle, used for convenience bounds calculations on model.
 * These are just value objects whose location and Size components can only be set
 * through the constructor.
 * @author Richard Adams
 *
 */
public class Bounds implements Comparable<Bounds>{
	
	private final Location location;
	private final Size size;
	
	public Bounds (int x, int y, int width, int height) {
		this(new Location(x,y), new Size(width, height));
	}
	public Bounds(Location location, Size size) {
		super();
		this.location = location;
		this.size = size;
	}
	/**
	 * Copy constructor
	 * @param toCopy A Bounds object
	 */
	public Bounds (Bounds toCopy){
		this.location = toCopy.getLocation();
		this.size = toCopy.getSize();
	}
	
	public Bounds getCopy () {
		return new Bounds(this);
	}
	public Location getLocation() {
		return location;
	}
	public Size getSize() {
		return size;
	}
	/**
	 * Shrinks bounds, keeps centre point the same.
	 * @param x Pixels to shrink width
	 * @param y Pixels to shrink height
	 * @return A new Bounds object.
	 */
	public Bounds shrink( int x, int y){
		Location loc2 = new Location(location.getX() + x, location.getY() + y);
		Size size2 = new Size(size.getWidth() - x - x, size.getHeight() - y - y);
		return new Bounds (loc2, size2);
	}
	
	
	/**
	 * Expands bounds, keeps centre point the same.
	 * @param x Pixels to expand width
	 * @param y Pixels to expand height
	 * @return This {@link Bounds} object.
	 */
	public Bounds expand( int x, int y){
		return shrink(-x, -y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((size == null) ? 0 : size.hashCode());
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
		final Bounds other = (Bounds) obj;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (size == null) {
			if (other.size != null)
				return false;
		} else if (!size.equals(other.size))
			return false;
		return true;
	}
	
	public String toString() {
		return "Bounds : " + location.toString() + size.toString(); 
	}
	
	public int getWidth () {
		return size.getWidth();
	}
	
	public int getHeight () {
		return size.getHeight();
	}

	public int getX(){
		return location.getX();
	}
	
	public int getY() {
		return location.getY();
	
	}
	
	public Location getTopLeft (){
		return location;
	}
	public Location getTopRight (){
		return new Location(getX()+getWidth(), getY());
	}
	public Location getBottomRight (){
		return new Location(getX()+getWidth(), getY()+getHeight());
	}
	
	public Location getBottomLeft (){
		return new Location(getX(), getY()+getHeight());
	}
	/**
	 * Uses same algorithm as draw2d rectangle
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return A union of the  Bounds objects for convenience
	 */
	public Bounds union(int x, int y, int w, int h) {
		int right = Math.max(this.getX() + getWidth(), x + w);
		int bottom = Math.max(this.getY() + getHeight(), y + h);
		Location loc2 = new Location( Math.min(this.getX(), x), Math.min(this.getY(), y));
		Size size2= new Size( right - getX(), bottom - getY());
		return new Bounds(loc2, size2);
	}
	

	
	public Bounds union(Bounds b) {
		return union(b.getX(), b.getY(), b.getWidth(), b.getHeight());
	}
	/**
	 * 
	 * @return A copy of this Bounds, translated by the Location
	 */
	public Bounds translate (Location loc) {
		return new Bounds (getLocation().translate(loc), getSize());
	}
	
	public double getArea (){
		return size.getHeight() * size.getWidth();
	}
	
	/**
	 * Calcu
	 * @return
	 */
	public Location getCentre() {
		return new Location  ( (int)(getX() + (0.5 * getWidth())), 
				(int)(getY() + (0.5 * getHeight())) );
	}
	/**
	 * Sorts on basis of bounded area, will sort in increasing size order
	 */
	public int compareTo(Bounds o) {
		if (Math.abs(o.getArea() - this.getArea()) <0.001 ){
			return 0;
		}
	    else if(o.getArea() < this.getArea()){
			return 1;
		} else {
			return -1;
		}
	}
	/**
	 * 
	 * @param b
	 * @return <code>true</code> if the parameter bounds are wholly contained by this bounds
	 * For two equals bounds, will return false
	 */
	public boolean contains (Bounds other){
	  if(size.getWidth() + location.getX() > other.getWidth() + other.getLocation().getX()&&
			  size.getHeight() + location.getY() > other.getHeight() + other.getLocation().getY()&&
	     location.getX()< other.getLocation().getX() &&
	     location.getY() < other.getLocation().getY()){
		  return true;
	  }
	  return false;
		
	}
}
