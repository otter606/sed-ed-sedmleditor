package org.sedml.jlibsedml.editor;

import org.eclipse.gef.tools.CreationTool;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.sedml.jlibsedml.editor.configdialogs.ModelConfigDialog;
import org.sedml.jlibsedml.editor.configdialogs.TaskConfigDialog;
import org.sedml.jlibsedml.editor.configdialogs.UTCConfigDialog;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.GModel;
import org.sedml.jlibsedml.editor.gmodel.GSimulation;
import org.sedml.jlibsedml.editor.gmodel.GTask;

/**
 * Extends standard functionality by adding a dialog to prompt for a name for
 * the just-added shape. 
 * 
 * @author Richard Adams
 * 
 */
public class SedMLCreationTool extends CreationTool {
	public SedMLCreationTool() {
		super();
		
	}

	 

	static final String DIALOG_TITLE = "Enter a name for the new shape...";

	
	 /* This is called after the CreateItemCommand has executedStarted and editparts
	 * have been created. However the figures have not yet been updated, so the text editor will
	 * appear a short delay after the figure updates 
	 */
	protected void handleFinished() {
	

			//	showTextEditorInSeparateThread();

		super.handleFinished();
	}

	 void showTextEditorInSeparateThread() {
		Display.getCurrent().asyncExec(new Runnable(){
		/*
		 *Gives times for figure to refresh after creation 
		 */
		static final long DELAY = 250;
		public void run() {
		try{
			Thread.sleep(1);
		} catch(InterruptedException e){
			
		}
		getNameFromTextEditor();
		}

		
		});
	}
	
	private void getNameFromTextEditor() {
		GElement created = getCreatedShape();
		TrayDialog toOpen =null;
		if(created.isModel()){
			toOpen = new ModelConfigDialog(Display.getCurrent().getActiveShell(), (GModel)created);	
		}else if (created.isSimulation()){
			toOpen = new UTCConfigDialog(Display.getCurrent().getActiveShell(), (GSimulation)created);	
		}else if (created.isTask()){
			toOpen = new TaskConfigDialog(Display.getCurrent().getActiveShell(), (GTask)created);	
		}
		if(toOpen !=null)
			toOpen.open();
		//MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "Opening dialog", "this will be config");
	}
	/*
	 * package for testing
	
    /*
  
	}
    /*
     * PAckage locaal for testing.
     * @return Mid point of display
     */
    Point calculateLocation() {
		Point p = Display.getCurrent().getActiveShell().getLocation();
		int x = p.x + Display.getCurrent().getActiveShell().getSize().x / 2;
		int y = p.y + Display.getCurrent().getActiveShell().getSize().y / 2;
		return new Point(x, y);
	}

	 GElement getCreatedShape() {
		Object createdObject = getCreateRequest().getNewObject();
		if (createdObject instanceof GElement) {
			return (GElement) createdObject;
		}
		return null;
	}
}
