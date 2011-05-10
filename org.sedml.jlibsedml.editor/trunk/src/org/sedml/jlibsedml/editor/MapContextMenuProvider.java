/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.sedml.jlibsedml.editor;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.ActionFactory;
import org.sedml.jlibsedml.editor.actions.AddChangeAction;
import org.sedml.jlibsedml.editor.actions.AddCurveAction;
import org.sedml.jlibsedml.editor.actions.AddDatasetAction;
import org.sedml.jlibsedml.editor.actions.AddNotesAction;
import org.sedml.jlibsedml.editor.actions.AddParameterAction;
import org.sedml.jlibsedml.editor.actions.AddVariableAction;
import org.sedml.jlibsedml.editor.actions.LayoutAction;
import org.sedml.jlibsedml.editor.actions.PushOneBackZOrderAction;
import org.sedml.jlibsedml.editor.actions.PushOneForwardZOrderAction;
import org.sedml.jlibsedml.editor.actions.PushToBackZOrderAction;
import org.sedml.jlibsedml.editor.actions.PushToFrontZOrderAction;


/**
 * Assembles actions for use in the map context menu. Actions must previously
 * be defined in EDitorActionContributor or declared in plugin.xml
 * @author Richard Adams
 *
 */
public class MapContextMenuProvider
	extends org.eclipse.gef.ContextMenuProvider {

private ActionRegistry actionRegistry;

public MapContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
	super(viewer);
	setActionRegistry(registry);
}

/* (non-Javadoc)
 * @see org.eclipse.gef.ContextMenuProvider#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
 */
public void buildContextMenu(IMenuManager manager) {
	GEFActionConstants.addStandardActionGroups(manager);
	
	IAction action;
//	action = getActionRegistry().getAction("mytestaction");
//	if (action.isEnabled()){
//		manager.appendToGroup(GEFActionConstants.GROUP_UNDO, action);
//	}
	
//	action = getActionRegistry().getAction (GEFActionConstants.TOGGLE_GRID_VISIBILITY ) ;
//	if ( action.isEnabled())
//		manager.appendToGroup(GEFActionConstants.GROUP_VIEW, action);

//	action = getActionRegistry().getAction(ActionFactory.CUT.getId());
//	if (action.isEnabled()){
//		manager.appendToGroup(GEFActionConstants.GROUP_COPY, action);
//	}
//	
//	action = getActionRegistry().getAction(ActionFactory.COPY.getId());
//	if (action.isEnabled()){
//		manager.appendToGroup(GEFActionConstants.GROUP_COPY, action);
//	}
//	
//	action = getActionRegistry().getAction(ActionFactory.PASTE.getId());
//	if (action.isEnabled()){
//		manager.appendToGroup(GEFActionConstants.GROUP_COPY, action);
//	}
	
	action = getActionRegistry().getAction(ActionFactory.SELECT_ALL.getId());
	if (action.isEnabled()){
		manager.appendToGroup(GEFActionConstants.GROUP_COPY, action);
	}
	


	action = getActionRegistry().getAction(ActionFactory.UNDO.getId());
	manager.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

	action = getActionRegistry().getAction(ActionFactory.REDO.getId());
	manager.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

	action = getActionRegistry().getAction(ActionFactory.DELETE.getId());
	if (action.isEnabled())
		manager.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

//	action = getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT);
//	if (action.isEnabled())
//		manager.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
	
	
	// Alignment Actions
	MenuManager submenu = new MenuManager("Align");

	action = getActionRegistry().getAction(GEFActionConstants.ALIGN_LEFT);
	if (action.isEnabled())
		submenu.add(action);

	action = getActionRegistry().getAction(GEFActionConstants.ALIGN_CENTER);
	if (action.isEnabled())
		submenu.add(action);

	action = getActionRegistry().getAction(GEFActionConstants.ALIGN_RIGHT);
	if (action.isEnabled())
		submenu.add(action);
		
	submenu.add(new Separator());
	
	action = getActionRegistry().getAction(GEFActionConstants.ALIGN_TOP);
	if (action.isEnabled())
		submenu.add(action);

	action = getActionRegistry().getAction(GEFActionConstants.ALIGN_MIDDLE);
	if (action.isEnabled())
		submenu.add(action);

	action = getActionRegistry().getAction(GEFActionConstants.ALIGN_BOTTOM);
	if (action.isEnabled())
		submenu.add(action);

	if (!submenu.isEmpty())
		manager.appendToGroup(GEFActionConstants.GROUP_REST, submenu);

	action = getActionRegistry().getAction(ActionFactory.SAVE.getId());
	manager.appendToGroup(GEFActionConstants.GROUP_SAVE, action);
	
	
	String [] SEDML_ADD_IDS= new String [] {AddVariableAction.ADD_VARIABLE_ID,
			AddParameterAction.ADD_PARAMETER_ID,AddCurveAction.ADD_CURVE_ID,
			AddChangeAction.ADD_CHANGE_ID,AddDatasetAction.ADD_Dataset_ID, AddNotesAction.ADD_NOTE_ID, LayoutAction.LAYOUT_ID};
	
	for(String actionid: SEDML_ADD_IDS){
		action = getActionRegistry().getAction(actionid);
		if (action.isEnabled())
			manager.appendToGroup(GEFActionConstants.GROUP_REST,action);
	}
	
	
	
	
	//ZORDER ACTIONS
	MenuManager order = new MenuManager("Change Order");
	
	action = getActionRegistry().getAction(PushToBackZOrderAction.PUSH_TO_BACK_Z_ORDERID);
	if (action.isEnabled())
		order.add(action);
	
	action = getActionRegistry().getAction(PushOneBackZOrderAction.PUSH_BACK_Z_ORDERID);
	if (action.isEnabled())
		order.add(action);
	
	action = getActionRegistry().getAction(PushToFrontZOrderAction.PUSH_TO_FRONT_Z_ORDERID);
	if (action.isEnabled())
		order.add(action);
	
	action = getActionRegistry().getAction(PushOneForwardZOrderAction.PUSH_FORWARD_Z_ORDERID);
	if (action.isEnabled())
		order.add(action);
		
	if (!order.isEmpty())
		manager.appendToGroup(GEFActionConstants.GROUP_REST, order);
	
	

}


private ActionRegistry getActionRegistry() {
	return actionRegistry;
}

private void setActionRegistry(ActionRegistry registry) {
	actionRegistry = registry;
}

}
