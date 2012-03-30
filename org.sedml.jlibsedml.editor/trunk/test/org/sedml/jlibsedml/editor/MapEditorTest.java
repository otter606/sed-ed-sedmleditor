package org.sedml.jlibsedml.editor;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IFileEditorInput;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class MapEditorTest {
	
	
	class MapEditorTSS extends MapEditor {
		 boolean isShownLayoutDialog=false;
		 
		 boolean canProceed=true;
		 boolean errorDialogShown=false;
		 void showLayoutDialog() {
			 isShownLayoutDialog=true;
		 }
		 boolean showPossErrorDialog (){
			 errorDialogShown=true;
			 return canProceed;
		 }
		 void init() {}
		 void markSaveLocation(){}
	}
	MapEditor mep;
	MapEditorTSS mepTSS;
	final File regularSEDML = new File ("TestData/sedMLBIOM12.xml");
	final File sedmlWithErrors = new File ("TestData/sedMLBIOM12WithErrors.xml");
	JUnit4Mockery mock = new JUnit4Mockery();
	IFileEditorInput mockInput = mock.mock(IFileEditorInput.class);
	IFile mockFile = mock.mock(IFile.class);
	IPath mockSEDMLPath = mock.mock(IPath.class);
	@Before
	public void setUp() throws Exception {
		mepTSS =new MapEditorTSS();
		mep = mepTSS;
	}

	@After
	public void tearDown() throws Exception {
	}

	

	@Test
	public final void testDoSaveRegularSEDML() throws CoreException {
		
		setUpSEDMLExpectations(regularSEDML);
		mep.setInput(mockInput);
		
		// saving expectations
		mock.checking(new Expectations() {
			{one(mockInput).getFile();will(returnValue(mockFile));}
			{one(mockFile).setContents(with(any(InputStream.class)), 
							with(any(Boolean.class)),with(any(Boolean.class)),
							(IProgressMonitor) with(nullValue()));}
		});
		mep.doSave(null);
	}

	@Test
	public final void testInitIEditorSiteIEditorInputWithSedxFileWithLayout() throws FileNotFoundException, CoreException {
		final FileInputStream fisSEDX = new FileInputStream("TestData/sedx.sedx");
		mock.checking(new Expectations() {
			{one(mockInput).getFile();will(returnValue(mockFile));}
			{one(mockFile).getFileExtension();will(returnValue("sedx"));}
			{one(mockFile).getContents();will(returnValue(fisSEDX));}
			{atLeast(1).of(mockFile).getName();will(returnValue("sedx.sedx"));}
		});
		mep.setInput(mockInput);
		assertNotNull(mep.getModel());
		assertTrue(mep.getModel().isSedxArchive());
		assertNotNull(mep.getModel().getArchiveComponents());
		
	}
	
	
	@Test
	public final void testSaveAsSEDX() throws FileNotFoundException, CoreException {
		final FileInputStream fisSEDX = new FileInputStream("TestData/sedx.sedx");
		mock.checking(new Expectations() {
			{one(mockInput).getFile();will(returnValue(mockFile));}
			{one(mockFile).getFileExtension();will(returnValue("sedx"));}
			{one(mockFile).getContents();will(returnValue(fisSEDX));}
			{atLeast(1).of(mockFile).getName();will(returnValue("sedx.sedx"));}
		});
		mep.setInput(mockInput);
		// saving expectations
		mock.checking(new Expectations() {
			{one(mockInput).getFile();will(returnValue(mockFile));}
			{one(mockFile).setContents(with(any(InputStream.class)), 
							with(any(Boolean.class)),with(any(Boolean.class)),
							(IProgressMonitor) with(nullValue()));}
		});
		mep.doSave(null);
		
	}
	
	@Test
	public final void testInitIEditorSiteIEditorInputWithSEDMLFile() throws FileNotFoundException, CoreException {
		setUpSEDMLExpectations(regularSEDML);
		
		mep.setInput(mockInput);
		assertTrue(mepTSS.isShownLayoutDialog);
		assertFalse(mep.getModel().isSedxArchive());
		assertNull(mep.getModel().getArchiveComponents());
	}
	
	void setUpSEDMLExpectations (final File file){
		mock.checking(new Expectations() {
			{one(mockInput).getFile();will(returnValue(mockFile));}
			{one(mockFile).getFileExtension();will(returnValue("xml"));}
			{one(mockFile).getLocation();will(returnValue(mockSEDMLPath));}
			{atLeast(1).of(mockSEDMLPath).toFile();will(returnValue(file));}
			{atLeast(1).of(mockFile).getName();}
		});
	}
	@Test
	public final void tesEditorInputWithSEDMLFileWithErrorsShowsDialog() throws FileNotFoundException, CoreException {
		
		setUpSEDMLExpectations(sedmlWithErrors);
		
		mep.setInput(mockInput);
		assertTrue(mepTSS.errorDialogShown);
		assertTrue(mepTSS.isShownLayoutDialog);
		assertNotNull(mep.getModel());
		assertFalse(mep.isSedxArchive);
	}

}
