package org.sedml.jlibsedml.editor;

import java.util.List;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.AlignmentAction;
import org.eclipse.gef.ui.actions.CopyTemplateAction;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.MatchHeightAction;
import org.eclipse.gef.ui.actions.MatchWidthAction;
import org.eclipse.gef.ui.actions.SelectAllAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchPart;
import org.sedml.jlibsedml.editor.actions.AddChangeAction;
import org.sedml.jlibsedml.editor.actions.AddCurveAction;
import org.sedml.jlibsedml.editor.actions.AddDatasetAction;
import org.sedml.jlibsedml.editor.actions.AddNotesAction;
import org.sedml.jlibsedml.editor.actions.AddParameterAction;
import org.sedml.jlibsedml.editor.actions.AddVariableAction;
import org.sedml.jlibsedml.editor.actions.CopyObjectsAction;
import org.sedml.jlibsedml.editor.actions.CutObjectsAction;
import org.sedml.jlibsedml.editor.actions.LayoutAction;
import org.sedml.jlibsedml.editor.actions.PasteAction;
import org.sedml.jlibsedml.editor.actions.PushOneBackZOrderAction;
import org.sedml.jlibsedml.editor.actions.PushOneForwardZOrderAction;
import org.sedml.jlibsedml.editor.actions.PushToBackZOrderAction;
import org.sedml.jlibsedml.editor.actions.PushToFrontZOrderAction;
import org.sedml.jlibsedml.editor.actions.RestrictViewAction;

/**
 * Local helper class for {@link MapEditor} for registering editing actions
 * @author Richard Adams
 *
 */
class MapEditorActionFactory {

	public void createActions(MapEditor mapEditor, ActionRegistry registry, List selectionActions) {
		// TODO Auto-generated method stub
		
	IAction action;

	action = new CopyTemplateAction(mapEditor);
	registry.registerAction(action);
	
	action = new PushToBackZOrderAction(mapEditor);
	registry.registerAction(action);
	selectionActions.add(action.getId());
	
	action = new PushOneBackZOrderAction(mapEditor);
	registry.registerAction(action);
	selectionActions.add(action.getId());
	
	action = new PushToFrontZOrderAction(mapEditor);
	registry.registerAction(action);
	selectionActions.add(action.getId());
	
	action = new PushOneForwardZOrderAction(mapEditor);
	registry.registerAction(action);
	selectionActions.add(action.getId());
	
	action = new AddVariableAction(mapEditor);
	registry.registerAction(action);
	selectionActions.add(action.getId());
	
	action = new AddParameterAction(mapEditor);
	registry.registerAction(action);
	selectionActions.add(action.getId());
	
	action = new AddCurveAction(mapEditor);
	registry.registerAction(action);
	selectionActions.add(action.getId());
	
	action = new AddChangeAction(mapEditor);
	registry.registerAction(action);
	selectionActions.add(action.getId());
	
	action = new AddDatasetAction(mapEditor);
	registry.registerAction(action);
	selectionActions.add(action.getId());
	
	action = new AddNotesAction(mapEditor);
	registry.registerAction(action);
	selectionActions.add(action.getId());
	
	action = new LayoutAction(mapEditor);
	registry.registerAction(action);
	selectionActions.add(action.getId());
	
	action = new RestrictViewAction(mapEditor);
	registry.registerAction(action);
	selectionActions.add(action.getId());
	
	
	
	
	

	
	action = new MatchWidthAction(mapEditor);
	registry.registerAction(action);
	selectionActions.add(action.getId());

	action = new MatchHeightAction(mapEditor);
	registry.registerAction(action);
	selectionActions.add(action.getId());

	action = new DirectEditAction((IWorkbenchPart) mapEditor);
	registry.registerAction(action);
	selectionActions.add(action.getId());


	action = new CopyObjectsAction( mapEditor);
	registry.registerAction(action);
	selectionActions.add(action.getId());
	
	action = new CutObjectsAction( mapEditor);
	registry.registerAction(action);
	selectionActions.add(action.getId());

	
	action = new SelectAllAction(mapEditor);
	registry.registerAction(action);
	selectionActions.add(action.getId());

	action = new PasteAction(mapEditor);
	registry.registerAction(action);
	selectionActions.add(action.getId());

	// action = new ShowGridAction ( (IWorkbenchPart)mapEditor , mapEditor ) ;
	// registry.registerAction(action) ;
	// selectionActions.add(action.getId());

	action = new AlignmentAction((IWorkbenchPart) mapEditor,
			PositionConstants.LEFT);
	registry.registerAction(action);
	selectionActions.add(action.getId());

	action = new AlignmentAction((IWorkbenchPart) mapEditor,
			PositionConstants.RIGHT);
	registry.registerAction(action);
	selectionActions.add(action.getId());

	action = new AlignmentAction((IWorkbenchPart) mapEditor,
			PositionConstants.TOP);
	registry.registerAction(action);
	selectionActions.add(action.getId());

	action = new AlignmentAction((IWorkbenchPart) mapEditor,
			PositionConstants.BOTTOM);
	registry.registerAction(action);
	selectionActions.add(action.getId());

	action = new AlignmentAction((IWorkbenchPart) mapEditor,
			PositionConstants.CENTER);
	registry.registerAction(action);
	selectionActions.add(action.getId());

	action = new AlignmentAction((IWorkbenchPart) mapEditor,
			PositionConstants.MIDDLE);
	registry.registerAction(action);
	selectionActions.add(action.getId());

	
	
		


}

 




}
