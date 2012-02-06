package org.sedml.jlibsedml.editor;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;

/**
 * Visual representation of SEDML elements, which are basic shapes with text labels inside
 *  for properties.
 * @author radams
 *
 */
public class GElementFigure extends RoundedRectangle {
	Label typeLabel;
	Label display1;
	Label display2;
	GElementEditPart ep;
	GElementFigure(final GElementEditPart ep){
		this.ep=ep;
		
		setLayoutManager(new ToolbarLayout());
		setOpaque(true); // non-transparent figure
		setBackgroundColor(ColorConstants.lightBlue);
		setForegroundColor(getColour());
		typeLabel= new Label(ep.getCastedModel().getType());
		typeLabel.setBackgroundColor(ColorConstants.white);
		typeLabel.setForegroundColor(ColorConstants.black);
		typeLabel.setOpaque(true);
		
		typeLabel.setBorder(new LineBorder());
		add(typeLabel);
		display1=new Label(ep.getCastedModel().getDisplay1());
		add(display1);
		
		display2=new Label(ep.getCastedModel().getDisplay2());
		
		add(display2);
			
	}
	 private Color getColour() {
		if(ep.getCastedModel().canGetSedML()){
			return ColorConstants.black;
		}else {
			return ColorConstants.red;
		}
	}
	void setDisplay1(String idLabelText) {
		display1.setText(idLabelText);
		if(ep.getCastedModel().getNotes()!=null){
			ImageDescriptor id = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/note.png");
		
			display1.setIcon(id.createImage());
		}else {
			display1.setIcon(null);
		}
	}
	 void setDisplay2(String nameLabelText) {
		display2.setText(nameLabelText);
	}
	 
	 void updateColour(){
		 setForegroundColor(getColour());
	 }

}
