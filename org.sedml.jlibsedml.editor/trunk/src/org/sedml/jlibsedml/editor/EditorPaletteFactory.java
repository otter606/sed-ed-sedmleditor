/*******************************************************************************
 * Copyright (c) 2004, 2010 Elias Volanakis and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Elias Volanakis - initial API and implementation
 *******************************************************************************/
package org.sedml.jlibsedml.editor;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.SimpleFactory;
import org.sedml.jlibsedml.editor.gmodel.GDataGenerator;
import org.sedml.jlibsedml.editor.gmodel.GModel;
import org.sedml.jlibsedml.editor.gmodel.GPlot2D;
import org.sedml.jlibsedml.editor.gmodel.GReport;
import org.sedml.jlibsedml.editor.gmodel.GSimulation;
import org.sedml.jlibsedml.editor.gmodel.GTask;

/**
 * Utility class that can create a GEF Palette.
 * 
 * @see #createPalette()
 * @author Elias Volanakis
 */
final class EditorPaletteFactory {

	/** Create the "Shapes" drawer. */
	private static PaletteContainer createShapesDrawer() {
		PaletteDrawer componentsDrawer = new PaletteDrawer("SED-ML elements");
	

//entries.add(toolEntry);
		CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry(
				"Model", "Create a Model element", GModel.class,
				new SimpleFactory(GModel.class),
				null, null);
		component.setToolClass(SedMLCreationTool.class);
		componentsDrawer.add(component);

		component = new CombinedTemplateCreationEntry("Simulation",
				"Create a simulation", GSimulation.class,
				new SimpleFactory(GSimulation.class),
				null,null);
		component.setToolClass(SedMLCreationTool.class);
		componentsDrawer.add(component);
		
		component = new CombinedTemplateCreationEntry("Task",
				"Create a Task", GTask.class,
				new SimpleFactory(GTask.class),
				null,null);
		component.setToolClass(SedMLCreationTool.class);
		componentsDrawer.add(component);
		component = new CombinedTemplateCreationEntry("DataGenerator",
				"Create a DataGenerator", GDataGenerator.class,
				new SimpleFactory(GDataGenerator.class),
				null,null);
		componentsDrawer.add(component);
	
		component = new CombinedTemplateCreationEntry("Output-Plot2D",
				"Create an Output 2DPlot", GPlot2D.class,
				new SimpleFactory(GPlot2D.class),
				null,null);
		componentsDrawer.add(component);
		component = new CombinedTemplateCreationEntry("Output-Report",
				"Create a Report", GReport.class,
				new SimpleFactory(GReport.class),
				null,null);
		componentsDrawer.add(component);
		return componentsDrawer;
	}

	/**
	 * Creates the PaletteRoot and adds all palette elements. Use this factory
	 * method to create a new palette for your graphical editor.
	 * 
	 * @return a new PaletteRoot
	 */
	static PaletteRoot createPalette() {
		PaletteRoot palette = new PaletteRoot();
		palette.add(createToolsGroup(palette));
		palette.add(createShapesDrawer());
		palette.add(createConnectionsDrawer());
		return palette;
	}

	private static PaletteEntry createConnectionsDrawer() {
		PaletteDrawer componentsDrawer = new PaletteDrawer("Relations");
		// Add (dashed-line) connection tool
		ToolEntry tool2 = new ConnectionCreationToolEntry("Connection",
				"Create a line connection", new CreationFactory() {
					public Object getNewObject() {
						return null;
					}

					// see ShapeEditPart#createEditPolicies()
					// this is abused to transmit the desired line style
					public Object getObjectType() {
						return null;
					}
				}, null,null);
		componentsDrawer.add(tool2);

		return componentsDrawer;
		
	}

	/** Create the "Tools" group. */
	private static PaletteContainer createToolsGroup(PaletteRoot palette) {
		PaletteToolbar toolbar = new PaletteToolbar("Tools");

		// Add a selection tool to the group
		ToolEntry tool = new PanningSelectionToolEntry();
		toolbar.add(tool);
		palette.setDefaultEntry(tool);

		// Add a marquee tool to the group
		toolbar.add(new MarqueeToolEntry());
		
		

		

		return toolbar;
	}

	/** Utility class. */
	private EditorPaletteFactory() {
		// Utility class
	}

}