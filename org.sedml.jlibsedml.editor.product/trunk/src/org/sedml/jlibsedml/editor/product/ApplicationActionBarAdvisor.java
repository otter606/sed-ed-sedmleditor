package org.sedml.jlibsedml.editor.product;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

  

    private IWorkbenchAction newAction, exitAction, saveAction, saveAsAction;
	private IContributionItem openViewAction;
	IWorkbenchAction resetPerspectiveAction, openPerspective;
	private IWorkbenchAction helpContents, aboutAction;
	
	private IWorkbenchAction preferencesAction;
	
	//edit actions
	private IWorkbenchAction undoAction;
	private IWorkbenchAction redoAction;
	private IWorkbenchAction copyAction;
	private IWorkbenchAction pasteAction;
	private IWorkbenchAction cutAction;
	private IWorkbenchAction deleteAction;
	private IWorkbenchAction selectAllAction;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(final IWorkbenchWindow window) {
		// Creates the actions and registers them.
		// Registering is needed to ensure that key bindings work.
		// The corresponding commands keybindings are defined in the plugin.xml
		// file.
		// Registering also provides automatic disposal of the actions when
		// the window is closed.
        newAction = ActionFactory.NEW_WIZARD_DROP_DOWN.create(window);
        newAction.setText("New");
		exitAction = ActionFactory.QUIT.create(window);
		register(exitAction);
		
		
		
		preferencesAction = ActionFactory.PREFERENCES.create(window);
		register(preferencesAction);
		
		saveAction=ActionFactory.SAVE.create(window);
		register(saveAction);
		
		saveAsAction=ActionFactory.SAVE_AS.create(window);
		register(saveAsAction);
		
		openViewAction = ContributionItemFactory.VIEWS_SHORTLIST.create(window);
		
		resetPerspectiveAction = ActionFactory.RESET_PERSPECTIVE.create(window);
		register(resetPerspectiveAction);
		
		
		helpContents = ActionFactory.HELP_CONTENTS.create(window);
		register (helpContents);
		aboutAction = ActionFactory.ABOUT.create(window);
		aboutAction.setText("About SED-ED");
		register(aboutAction);
		undoAction = ActionFactory.UNDO.create(window);
		register(undoAction);
		redoAction = ActionFactory.REDO.create(window);
		register(redoAction);
		copyAction = ActionFactory.COPY.create(window);
		register(copyAction);		
		pasteAction = ActionFactory.PASTE.create(window);
		register(pasteAction);
		cutAction = ActionFactory.CUT.create(window);
		register(cutAction);
		deleteAction = ActionFactory.DELETE.create(window);
		register(deleteAction);
		getActionBarConfigurer().registerGlobalAction(deleteAction);
		
		selectAllAction = ActionFactory.SELECT_ALL.create(window);
		register(selectAllAction);
	}

	protected void fillMenuBar(IMenuManager menuBar) {
		createFileMenu(menuBar);
		createEditMenu(menuBar);
		createOptionsMenu(menuBar);
		  menuBar.add(new Separator("additions"));
		createWindowMenu(menuBar);

	}

	

	private void createWindowMenu(IMenuManager menuBar) {
		MenuManager winMenu = new MenuManager("&Window",
				IWorkbenchActionConstants.M_WINDOW);
		menuBar.add(winMenu);
		winMenu.add(openViewAction);
		
	}
	
	private MenuManager createEditMenu(IMenuManager menuBar) {
		MenuManager menu = new MenuManager("Edit", IWorkbenchActionConstants.M_EDIT);

		menu.add(undoAction);
		menu.add(redoAction);
		menu.add(new Separator());
       
		menu.add(cutAction);
		menu.add(copyAction);
		menu.add(pasteAction);
		menu.add(new Separator());

		menu.add(deleteAction);
		menu.add(selectAllAction);
		 menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		 menuBar.add(menu);
		return menu;
	}



	private void createOptionsMenu(IMenuManager menuBar) {
		MenuManager winMenu = new MenuManager("&Options",
				"Options");
		menuBar.add(winMenu);
		winMenu.add(preferencesAction);
		
	}

	private void createFileMenu(IMenuManager menuBar) {
		MenuManager fileMenu = new MenuManager("&File",
				IWorkbenchActionConstants.M_FILE);
		menuBar.add(fileMenu);
		fileMenu.add(newAction);
		fileMenu.add(saveAction);
		fileMenu.add(saveAsAction);
		fileMenu.add(exitAction);
	}
    
}
