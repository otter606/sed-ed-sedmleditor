package org.sedml.jlibsedml.editor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.jlibsedml.Libsedml;
import org.jlibsedml.Model;
import org.jlibsedml.SEDBase;
import org.jlibsedml.SedML;
import org.jlibsedml.XMLException;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)

public class SEDMLEditorLinkTest {
	Mockery mockery  = new JUnit4Mockery();
	IWorkbenchPage iwp = mockery.mock(IWorkbenchPage.class);
	IFile ifile = mockery.mock(IFile.class);
	IPath ipath = mockery.mock(IPath.class);
	IViewPart part = mockery.mock(IViewPart.class);
	IStructuredSelection sel = mockery.mock(IStructuredSelection.class);
	
	File SEDX = new File("TestData/sedx.sedx");
	File SEDML = new File("TestData/sedMLBIOM12.xml");
	
	SedML sed;
	
	SEDMLEditorLink link;
	TSS  tss;

	class TSS extends SEDMLEditorLink {
		boolean Selected=false;
		IWorkbenchPage getWorkbenchPage() {
			return iwp;
		}
		
		IFile getFileEditorInputIfActiveEditorIsFileBased() {
			return ifile;
		}
		void selectTarget(SEDBase base) {
			Selected =true;
		}
	}

	@Before
	public void setUp() throws Exception {
		tss=  new TSS();
		link=tss;
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testSelectionChangedWillSelecctIfModelFound() throws XMLException {
		sed = Libsedml.readDocument(SEDML).getSedMLModel();
		mockery.checking(new Expectations(){
			{atLeast(1).of(sel).getFirstElement();will(returnValue(sed.getModels().get(0)));}
			{atLeast(1).of(ifile).getLocation();will(returnValue(ipath));}
			{atLeast(1).of(ipath).toFile();will(returnValue(SEDML));}
			
		});
		link.selectionChanged(part, sel);
		assertTrue(tss.Selected);
	}
	
	@Test
	public final void testSelectionChangedWillSelecctIfModelFoundInSEDX() throws XMLException, FileNotFoundException, CoreException {
		sed = Libsedml.readSEDMLArchive(new FileInputStream(SEDX)).getSedmlDocument().getSedMLModel();
		mockery.checking(new Expectations(){
			{atLeast(1).of(sel).getFirstElement();will(returnValue(sed.getModels().get(0)));}
			{atLeast(1).of(ifile).getLocation();will(returnValue(ipath));}
			{atLeast(1).of(ifile).getContents();will(returnValue(new FileInputStream(SEDX)));}
			{atLeast(1).of(ipath).toFile();will(returnValue(SEDX));}
			
		});
		link.selectionChanged(part, sel);
		assertTrue(tss.Selected);
	}
	
	@Test
	public final void testSelectionChangedWillSelecctIfModelElNotFoundFound() throws XMLException {
		sed = Libsedml.readDocument(SEDML).getSedMLModel();
		mockery.checking(new Expectations(){
			{atLeast(1).of(sel).getFirstElement();will(returnValue( new Model("xxx",null,"src","lang")));}
			{atLeast(1).of(ifile).getLocation();will(returnValue(ipath));}
			{atLeast(1).of(ipath).toFile();will(returnValue(SEDML));}
			
		});
		link.selectionChanged(part, sel);
		assertFalse(tss.Selected);
	}

}
