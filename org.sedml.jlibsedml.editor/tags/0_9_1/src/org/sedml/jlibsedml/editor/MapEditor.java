package org.sedml.jlibsedml.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.sedml.ArchiveComponents;
import org.sedml.Libsedml;
import org.sedml.SEDMLDocument;
import org.sedml.SedML;
import org.sedml.XMLException;
import org.sedml.jlibsedml.editor.configdialogs.BaseConfigDialog;
import org.sedml.jlibsedml.editor.configdialogs.LayoutDiagramDialog;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.GElementBuilder;
import org.sedml.jlibsedml.editor.gmodel.GSedML;
import org.sedml.jlibsedml.editor.gmodel.PropertyChangeNames;
import org.sedml.jlibsedml.editor.handlers.ISEDMLProvider;
import org.sedml.jlibsedml.editor.layout.GraphLayouter;

public class MapEditor extends GraphicalEditorWithFlyoutPalette implements PropertyChangeListener, IViewChanger, ISEDMLProvider {
	
	static boolean listenerAdded = false;
	static {
		
		
	}
	private GSedML gsedml;
	MapEditorActionFactory actionCreator;
	boolean isSedxArchive = false;
	private ArchiveComponents ac=null;

	public MapEditor() {
		init();
	}

	void init() {
		setEditDomain(new DefaultEditDomain(this));
		 actionCreator = new MapEditorActionFactory();
		 if(!listenerAdded) {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().addSelectionListener(
						new SEDMLEditorLink());
				listenerAdded=true;
		 }
	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		
		return EditorPaletteFactory.createPalette();
	}
	
	public GSedML getModel(){
		return gsedml;
	}
	
	public GraphicalViewer getGraphicalViewer() {
		return super.getGraphicalViewer();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		SedML sedml = getModel().buildSEDML();
		
		String sedmlAsStr=new SEDMLDocument(sedml).writeDocumentToString();
		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
		if(ac!=null && isSedxArchive==true){
			ac = new ArchiveComponents(ac.getModelFiles(), new SEDMLDocument(sedml));
			try {
				byte [] archive = Libsedml.writeSEDMLArchive(ac,file.getName());
				file.setContents(new ByteArrayInputStream(archive), true, false, null);
			} catch (XMLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else { // is regular file
		try {
		
			file.setContents(new ByteArrayInputStream(sedmlAsStr.getBytes()), true, // keep
																			
					false, // dont keep history
					monitor); // progress monitor
			markSaveLocation();
		} catch (CoreException ce) {
			ce.printStackTrace();
		} 
		}
		markSaveLocation();

	}

	void markSaveLocation() {
		getCommandStack().markSaveLocation();
	}
	
	public void commandStackChanged(EventObject event) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(event);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
	 */
	protected void setInput(IEditorInput input) {
		
		super.setInput(input);
		try {
			IFile file = ((IFileEditorInput) input).getFile();
			System.err.println("setting input");
			SEDMLDocument doc = null;
			if(file.getFileExtension().equals("sedx")){
				isSedxArchive=true;
				try {
					ac = Libsedml.readSEDMLArchive(file.getContents());
					doc =ac.getSedmlDocument();
					
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
			 doc = Libsedml.readDocument(file.getLocation().toFile());
			}
			if(doc.hasErrors()){
				boolean rc  =showPossErrorDialog();
				if(rc == false){
					return;
				}
			}
			gsedml = createGModel(doc);
			gsedml.setIsSEDX(isSedxArchive);
			if(isSedxArchive){
				gsedml.setArchiveComponents(ac);
			}
			
			
			gsedml.addPropertyChangeListener(this);
			
			if (gsedml.hasIncompleteLayout()) {
				showLayoutDialog();
			}
			setPartName(file.getName());
		} catch (XMLException e) {
			handleLoadException(e);
		}
	}

	 boolean showPossErrorDialog() {
		boolean proceed=true;
		boolean OK =MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "SED-ML has errors", "This SED-ML file has errors which might prevent the correct" +
				" operation of this editor - do you want to continue opening?)");
		if(!OK) {
			proceed=false;
		}
		return proceed;
	}

	 void showLayoutDialog() {
		LayoutDiagramDialog layoutDialog = new LayoutDiagramDialog(Display.getCurrent().getActiveShell());
		if (layoutDialog.open() == Window.OK){
			GraphLayouter louter = new GraphLayouter(gsedml, this);
			
			if(layoutDialog.isLayoutAll()){
				louter.layout();
			} else if (layoutDialog.isLayoutSome()){
				// preserve existing layout state
				List<GElement> GInfoEls= gsedml.getChildrenWithGraphicalInfo();
				louter.setFixedNodes(GInfoEls);
				louter.layout();
			}
		}
	}
	
	

	private GSedML createGModel(SEDMLDocument doc) {
		GElementBuilder builder = new GElementBuilder(doc.getSedMLModel());
		return builder.build();
	}

	private void handleLoadException(Exception e) {
		System.err.println("** Load failed. Using default model. ** \n + " + e.getMessage());
		e.printStackTrace();
		gsedml = new GSedML();
		gsedml.addPropertyChangeListener(this);
		
	}

	protected void createActions() {
		super.createActions();
		actionCreator.createActions(this, getActionRegistry(), getSelectionActions());
		
	}


	@SuppressWarnings( { "unchecked" })
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		ScrollingGraphicalViewer viewer = (ScrollingGraphicalViewer) getGraphicalViewer();

		ScalableFreeformRootEditPart root = new SEDMLRootEditPart();
      
		List zoomLevels = new ArrayList(3);
		zoomLevels.add(ZoomManager.FIT_ALL);
		zoomLevels.add(ZoomManager.FIT_WIDTH);
		zoomLevels.add(ZoomManager.FIT_HEIGHT);
		root.getZoomManager().setZoomLevelContributions(zoomLevels);

		IAction zoomIn = new ZoomInAction(root.getZoomManager());
		IAction zoomOut = new ZoomOutAction(root.getZoomManager());
		getActionRegistry().registerAction(zoomIn);
		getActionRegistry().registerAction(zoomOut);
		getSite().getKeyBindingService().registerAction(zoomIn);
		getSite().getKeyBindingService().registerAction(zoomOut);

		  viewer.setRootEditPart(root);
		// viewer.setEditPartFactory(new GraphicalPartFactory());
			ContextMenuProvider provider = new MapContextMenuProvider(viewer,
					getActionRegistry());
			viewer.setContextMenu(provider);

			getSite().registerContextMenu(
					"org.sedml.jlibsedml.editor.contextmenu", //$NON-NLS-1$
					provider, viewer);

		
		
	}
	

	

	/**
	 * This overrides the default impl in {@link GraphicalEditor} to add some
	 * extra functionality. The super() call must always be retained
	 */
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		// until this line has run, getModel() will return null.
		super.init(site, input);
		doSpecificInit(input);

	}

	 void doSpecificInit(IEditorInput input) {
		setPartName(input.getName());
	}

	/**
	 * Set up the editor's inital content (after creation).
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#initializeGraphicalViewer()
	 */
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new SEDMLEditPartFactory());
		viewer.setContents(getModel()); // set the contents of this editor
		

	
	}
	
	public void flushViewer(){
		getGraphicalViewer().flush();
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals(PropertyChangeNames.CHILD_ADD_EVENT) &&
				 evt.getNewValue()!=null){
			GElement child = (GElement)evt.getNewValue();
			BaseConfigDialog bcd= ConfigDialogFactory.createDialog(child);
			if(bcd!=null){
				bcd.open();
			}
			
			
			
		
		}
	
	}
	
	public void dispose (){
		
		super.dispose();
	}
	



}
