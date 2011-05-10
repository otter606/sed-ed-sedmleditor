package org.sedml.jlibsedml.editor.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.sedml.jlibsedml.editor.GElementEditPart;
import org.sedml.jlibsedml.editor.MapEditor;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.layout.GraphLayouter;

public class LayoutAction extends SelectionAction {

	/**
	 * Ratio of empty space area to node area.
	 */
	private static final double SPACE_FACTOR = 5;
	public static String LAYOUT_ID = "org.sedml.layout";

	public LayoutAction(IWorkbenchPart part) {
		super(part);
		// TODO Auto-generated constructor stub
	}

	protected void init() {
		setId(LAYOUT_ID);
		setActionDefinitionId("Layout");
		setText("Layout");
		setToolTipText("Lays out the diagram");
		setEnabled(true);
	}

	@Override
	protected boolean calculateEnabled() {
		return true;
	}

	public void run() {

		GraphLayouter layouter = new GraphLayouter( ((MapEditor)getWorkbenchPart()).getModel(),((MapEditor)getWorkbenchPart()) );
		
		List<GElement> selected = new ArrayList<GElement>();
		// lock selected parts
		for (int i = 0; i < getSelectedObjects().size(); i++) {
			if (getSelectedObjects().get(i) instanceof GElementEditPart) {
				GElement gep = (GElement) ((GElementEditPart) getSelectedObjects()
						.get(i)).getModel();
				selected.add(gep);
			}

		}
		layouter.setFixedNodes(selected);
		layouter.layout();
		
		

	}

}
