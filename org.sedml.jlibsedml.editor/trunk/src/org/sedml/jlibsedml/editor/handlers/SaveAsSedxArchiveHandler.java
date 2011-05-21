package org.sedml.jlibsedml.editor.handlers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.sedml.ArchiveComponents;
import org.sedml.FileModelContent;
import org.sedml.IModelContent;
import org.sedml.Libsedml;
import org.sedml.Model;
import org.sedml.SEDMLDocument;
import org.sedml.SedML;
import org.sedml.XMLException;
import org.sedml.execution.FileModelResolver;
import org.sedml.execution.ModelResolver;
import org.sedml.jlibsedml.editor.MapEditor;
import org.sedml.jlibsedml.editor.gmodel.GSedML;
import org.sedml.jlibsedml.editor.gmodel.SEDMLBuilder;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SaveAsSedxArchiveHandler extends AbstractHandler {
	
	public final static String ALREADY_IS_SEDX_TITLE="Edited file is alraeady SEDX archive";
	public final static String ALREADY_IS_SEDX_MSG=" The file you're trying to save is already " +
			" a SEDX file, which can be saved by the standard Ctrl-S or File->Save mechanism.";
	private static final String MODEL_NOT_FOUND_TITLE = "Model not found";
	private static final String MODEL_NOT_FOUND_MSG1 = "The model contents referenced by [";
	private static final String MODEL_NOT_FOUND_MSG2 = "] could not be retrieved. Please check its 'source' attribute to " +
			"  ensure that it can be resolved. ";

	/**
	 * The constructor.
	 */
	public SaveAsSedxArchiveHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
	
		IEditorPart iep = HandlerUtil.getActiveEditor(event);
		if(iep instanceof MapEditor){
			MapEditor mep = (MapEditor)iep;
			GSedML gsed = mep.getModel();
			if(gsed.isSedxArchive()){
				showAlreadyIsSedxInfoDialog(event);
				return null;
			}
			
			SEDMLBuilder builder = new SEDMLBuilder();
			SedML sed =builder.buildSEDML(gsed);
			Set<String>seenBases= new HashSet<String>();
			for (Model m: sed.getModels()){
				ModelResolver mr = new ModelResolver(sed);
				mr.add(new FileModelResolver());
				String modelAsString = mr.getModelString(m);
				if(modelAsString==null){
					showModelNotResolvedError(event, m);
					return null;
				}
				List<String> modelBases = new ArrayList<String>();
				getModelModificationTree(m, modelBases, sed);
				String baseModel = modelBases.get(0);
				if(!seenBases.contains(baseModel)){
					seenBases.add(baseModel);
				}
	
			}
			List<IModelContent> models = new ArrayList<IModelContent>();
			for (String s: seenBases){
				String src = sed.getModelWithId(s).getSource();
				File f  = new File(src);
				models.add(new FileModelContent(f));
			}
			ArchiveComponents ac = new ArchiveComponents(models, new SEDMLDocument(sed));
			
//			
			doSaveAs(HandlerUtil.getActiveShell(event), (IFileEditorInput)mep.getEditorInput(), ac);
			
		}
		return null;
	}
	
	  private void showModelNotResolvedError(ExecutionEvent event, Model m) {
		  MessageDialog.openInformation(HandlerUtil.getActiveShell(event),MODEL_NOT_FOUND_TITLE, MODEL_NOT_FOUND_MSG1 + 
				    ( m.getName()!=null?m.getName():m.getId()) +
				    		  MODEL_NOT_FOUND_MSG2);
		
	}



	private void showAlreadyIsSedxInfoDialog(ExecutionEvent event) {
		MessageDialog.openInformation(HandlerUtil.getActiveShell(event),ALREADY_IS_SEDX_TITLE, ALREADY_IS_SEDX_MSG);
	}
	
	void getModelModificationTree(Model m, List<String> modelRefs2, SedML sedml) {
		String modelSrcRef = m.getSource();
		
		modelRefs2.add(m.getId());
		if (sedml.getModelWithId(modelSrcRef)!=null){
			
			getModelModificationTree(sedml.getModelWithId(modelSrcRef),modelRefs2,sedml);
		}else {
			Collections.reverse(modelRefs2);
		}
	}
	


	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSaveAs()
	 */
	public void doSaveAs(Shell shell, IFileEditorInput input, final ArchiveComponents ac) {
		// Show a SaveAs dialog
		
		SaveAsDialog dialog = new SaveAsDialog(shell);
		IPath origpath = input.getFile().getProjectRelativePath().removeFileExtension().addFileExtension("sedx");
		dialog.setOriginalFile(input.getFile().getParent().getFile(origpath));
		dialog.setTitle ("Save as SEDX archive");
		
		dialog.open();
		
		IPath path = dialog.getResult();	
		if (path != null) {
			// try to save the editor's contents under a different file name
			final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			try {
				new ProgressMonitorDialog(shell).run(
						false, // don't fork
						false, // not cancelable
						new WorkspaceModifyOperation() { // run this operation
							public void execute(final IProgressMonitor monitor) {
								try {
									byte [] archive = Libsedml.writeSEDMLArchive(ac, file.getName());
									file.create(
										new ByteArrayInputStream(archive), // contents
										true, // keep saving, even if IFile is out of sync with the Workspace
										monitor); // progress monitor
								} catch (CoreException ce) {
									ce.printStackTrace();
								} catch (XMLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} 
							}
						});
				// set input to the new file
		//		input.setInput(new FileEditorInput(file));
			//	getCommandStack().markSaveLocation();
			} catch (InterruptedException ie) {
	  			// should not happen, since the monitor dialog is not cancelable
				ie.printStackTrace(); 
			} catch (InvocationTargetException ite) { 
				ite.printStackTrace(); 
			}
		}
	}
}
