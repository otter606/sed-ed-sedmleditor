package org.sedml.jlibsedml.editor.gmodel;

import org.sedml.Curve;
import org.sedml.SEDMLTags;

public class GCurve extends GElement {
	
	private boolean logx, logy;
	private GDataGenerator x, y;

	public GCurve(Curve c) {
		super(c);
		setId(c.getId());
		setName(c.getName());
		logx=c.getLogX();
		logy=c.getLogY();
		
	}
	public GCurve() {
		// TODO Auto-generated constructor stub
	}
	public boolean isLogx() {
		return logx;
	}
	public void setLogx(boolean logx) {
		this.logx = logx;
		
		
	}
	public boolean isLogy() {
		return logy;
	}
	public void setLogy(boolean logy) {
		this.logy = logy;
	}
	/**
	 * 
	 * @param dg non-null.
	 * @return
	 */
	public  boolean isXDataGenerator(final GDataGenerator dg){
		return dg.equals(x);
	}
	
	/**
	 * 
	 * @param dg  non-null
	 * @return
	 */
	public  boolean isYDataGenerator(final GDataGenerator dg){
		return dg.equals(y);
	}
	public GDataGenerator getX() {
		return x;
	}
	public void setX(GDataGenerator x) {
		this.x = x;
		firePropertyChange(PropertyChangeNames.PROPERTY_EVENT, null, x);
	}
	public GDataGenerator getY() {
		return y;
	}
	public void setY(GDataGenerator y) {
		this.y = y;
		firePropertyChange(PropertyChangeNames.PROPERTY_EVENT, null, y);
	}
	@Override
	public boolean canGetSedML() {
		return getId()!=null && getX()!=null && getY()!=null;
	}
	@Override
	Curve getSEDMLObject() {
		assert(canGetSedML());
		Curve c = new Curve(getId(), getName(), logx, logy, x.getId(), y.getId());
		c.addAnnotation(createGraphicalInfoAnnotations());
		addNotes(c);
		
		return c;
		
	}
	
	private GDataGenerator oldx, oldy;// to make sure x and ya re set 
	//correctly after undo
	void setUpRefs(GElement target) {
		if(target.isDataGenerator()){
			if(target.equals(oldx)||(x==null&&!target.equals(oldy))){
				setX((GDataGenerator)target);
			}else if (y ==null&&!target.equals(oldx)||target.equals(oldy)){
				setY((GDataGenerator)target);
			}
		}
	}
	
	 void removeRefs(GElement gElement) {
			if(x!=null&&x.equals(gElement)){
				oldx=x;
				setX(null);
			}else if (y!=null&&y.equals(gElement)){
				oldy=y;
				setY(null);
			}
			
		}

	

	@Override
	public String getType() {
		return SEDMLTags.OUTPUT_CURVE;
	}
	

}
