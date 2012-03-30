package org.sedml.jlibsedml.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.part.FileEditorInput;
import org.jlibsedml.AbstractIdentifiableElement;
import org.jlibsedml.Libsedml;
import org.jlibsedml.SEDBase;
import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.SedML;
import org.jlibsedml.extensions.ElementSearchVisitor;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.GSedML;

/**
 * This listeners detects selections of the SED-ML elements in the project's view. Then, if the 
 *  currently active editor is a SED-ED graphical editor, it will make the editor scroll to and 
 *   highlight the element that was selected in the viewer. <br>
 *   A current limitation is that there is no way to tell from the selection, what file it belongs to - so if the current
 *    editor refers to a SEDML file with elements with the same ID, it will not be able to tell that the SEDML object graphs c
 *     come from different files. 
 * @author radams
 *
 */
public class SEDMLEditorLink implements ISelectionListener {
	
	
	
	
	IWorkbenchPage getWorkbenchPage() {
		IWorkbenchPage workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if (workbenchPage != null)
			return workbenchPage;

		try {
			workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().openPage(null);
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().setActivePage(workbenchPage);
		} catch (WorkbenchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pathwayeditor.application.utilities.IMapManager#getIEditorForOpenMap(org.pathwayeditor.businessobjectsAPI.IMap)
	 */
	IFile getFileEditorInputIfActiveEditorIsFileBased() {

		IWorkbenchPage workbenchPage = getWorkbenchPage();
		IEditorPart iep = workbenchPage.getActiveEditor();
		if (iep.getEditorInput() instanceof FileEditorInput && iep instanceof MapEditor) {
			FileEditorInput fei = (FileEditorInput)iep.getEditorInput();
			return  fei.getFile();
			
		} else {
			return null;
		}
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		
			if (part instanceof IViewPart) {
				ISelection sel = selection;
				
				if(sel instanceof IStructuredSelection){
					Object iss = ((IStructuredSelection)sel).getFirstElement();
					if(iss instanceof AbstractIdentifiableElement){
						AbstractIdentifiableElement toSearch = (AbstractIdentifiableElement)iss;
						IFile fileInput = getFileEditorInputIfActiveEditorIsFileBased();
						boolean isSEDX= fileInput.getLocation().toFile().getName().endsWith("sedx");
						boolean isSEDML=Libsedml.isSEDML(fileInput.getLocation().toFile());
						if(fileInput!=null){
							if (isSEDML || isSEDX
									 ){
								try {
								SEDMLDocument doc= null;
								if(isSEDML)
									 doc = Libsedml.readDocument(fileInput.getLocation().toFile());
								else {
									doc=Libsedml.readSEDMLArchive(fileInput.getContents()).getSedmlDocument();
								}
								SedML sed = doc.getSedMLModel();
								ElementSearchVisitor searcher = new ElementSearchVisitor(toSearch.getId());
								sed.accept(searcher);
								if(searcher.getFoundElement()!=null){
									SEDBase base = searcher.getFoundElement();
									selectTarget(base);
								}
								
								}catch (Exception ie) {
									ie.printStackTrace();
								}
								
							}
					}
				}
			}
			
		}
		
	}

	void selectTarget(SEDBase base) {
		IViewChanger me = (IViewChanger)getWorkbenchPage().getActiveEditor();
		GSedML model = me.getModel();
		selectTarget(base, me, model);
	}

	void selectTarget(SEDBase base, IViewChanger me, GSedML model) {
		for (GElement el: model.getChildren()){
			if (el.getId()!=null && el.getId().equals(((AbstractIdentifiableElement)base).getId())){
				EditPart ep =(EditPart)((EditPartViewer)me.getGraphicalViewer()).getEditPartRegistry().get(el);
				if(ep!=null){
					selectTarget(ep, (EditPartViewer)me.getGraphicalViewer());
				}
				break;
			}
		}
	}
	
	private void selectTarget(EditPart target, EditPartViewer viewer) {
		if (target != null) {
		//	viewer.deselect(sourceEditPart);
			viewer.reveal(target);
		
			viewer.select(target);
			target.activate();
		}
	}

}
