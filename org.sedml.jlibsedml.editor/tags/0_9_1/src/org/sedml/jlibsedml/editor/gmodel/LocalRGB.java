package org.sedml.jlibsedml.editor.gmodel;
/**
 * POJO for holding graphics-library  parsed colour data. 
 * Provides static constants for pure RED, GREEN, BLUE,BLACK, WHITE.
 * @author Richard Adams
 *
 */
public class LocalRGB {
	public static final LocalRGB WHITE = new LocalRGB(255,255,255);
	public static final LocalRGB BLACK = new LocalRGB(0,0,0);
	public static final LocalRGB RED = new LocalRGB(255,0,0);
	public static final LocalRGB GREEN = new LocalRGB(0,255,0);
	public static final LocalRGB BLUE = new LocalRGB(0,0,255);
	private final int red, green, blue;

	public int getRed() {
		return red;
	}


	public int getGreen() {
		return green;
	}

	

	public int getBlue() {
		return blue;
	}


	
	
    /**
     * Params must be >= 0 and <= 255
     * @param red
     * @param green
     * @param blue
     * @throws IllegalArgumentException if parameters are out of range
     */
	public LocalRGB(int red, int green, int blue) {
		super();
		if (red > 255 || green > 255 || blue > 255 ||
		    red <0 || blue <0 || green < 0){
			throw new IllegalArgumentException(" Local RGB parameters not set properly: " + red + ","+ green +"," + blue);
		}
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	/**
	 * Used to stringify color data to Hibernate Layer
	 * changes to this  and LocalRGB.getColour () need to be coordinated
	 * @return  a <code>String </code> of format '123,345,456' - a comma separated list of integers.
	 */
	public String toString (){
		return new StringBuffer().append(red).append(",").append(green).append(",").append(blue)
		    .toString();
	}
	
	/**
	 * Used to create LocalRGB from string argument. It is used from xStream to convert color input in string format to LocalRGB.
	 * @param RGB A String in the format "1,2,3"
	 * @return A {@link LocalRGB}
	 * @throws NumberFormatException if RGB String is not formatted correctly.
	 * @throws IllegalArgumentException if a {@link LocalRGB} object cannot be created.
	 */
	public static LocalRGB fromString(String RGB){
		
		String[] colors = RGB.split(",");
		
		return new LocalRGB(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + blue;
		result = prime * result + green;
		result = prime * result + red;
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
		final LocalRGB other = (LocalRGB) obj;
		if (blue != other.blue)
			return false;
		if (green != other.green)
			return false;
		if (red != other.red)
			return false;
		return true;
	}
	
  
}
