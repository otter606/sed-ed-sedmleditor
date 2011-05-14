package org.sedml.jlibsedml.editor.product;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.setFixed(false); // can't close workspace
		
		layout.addView(ProjectView.ID, IPageLayout.LEFT, 0.2f, editorArea);
		layout.addShowViewShortcut("org.eclipse.help.ui.HelpView");
	
		layout.addView(IPageLayout.ID_PROBLEM_VIEW,  IPageLayout.BOTTOM, 0.8f, editorArea);
	
	
	}
}
