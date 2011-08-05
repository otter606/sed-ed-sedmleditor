package org.sedml.jlibsedml.editor.handlers;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sedml.ArchiveComponents;
import org.sedml.Libsedml;
import org.sedml.Model;
import org.sedml.SedML;
import org.sedml.XMLException;
import org.sedml.jlibsedml.editor.gmodel.GElementBuilder;
import org.sedml.jlibsedml.editor.gmodel.GSedML;

@RunWith(JMock.class)
public class SaveAsSedxHandlerTest {
	private static final String EXISTENT_MODEL = "TestData/BIOMD0000000028.xml";
	File SEDML_EG = new File("TestData/sedMLBIOM12.xml");
	Mockery mockery = new JUnit4Mockery();
	final ISEDMLProvider iep = mockery.mock(ISEDMLProvider.class);
	SaveAsSedxArchiveHandlerTSS handlerTSS;
	SaveAsSedxArchiveHandler handler;
	class SaveAsSedxArchiveHandlerTSS extends SaveAsSedxArchiveHandler {
		boolean setIsSedx = false;
		boolean isSedxDialogShowed = false;
		boolean isModelNotResolved=false;
		boolean isPerformSaveCalled=false;
		ISEDMLProvider getEditor(ExecutionEvent event){
			return iep;
		}
		 void showAlreadyIsSedxInfoDialog(ExecutionEvent event) {
			 isSedxDialogShowed=true;
		 }
		 void showModelNotResolvedError(ExecutionEvent event, Model m){
			 isModelNotResolved=true;
		 }
		 void performSave(ExecutionEvent event, ISEDMLProvider mep,
					ArchiveComponents ac){
			 isPerformSaveCalled=true;
		 }
		
	}
	@Before
	public void setUp() throws Exception {
		handlerTSS = new SaveAsSedxArchiveHandlerTSS();
		handler = handlerTSS;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testExecuteShowsDialogIfIsAlreadySedx() throws ExecutionException {	
		final GSedML sdml = new GSedML();
		sdml.setIsSEDX(true);
		mockery.checking(new Expectations(){
			{one(iep).getModel();will(returnValue(sdml));}
		});
		handler.execute(null);
		assertTrue(handlerTSS.isSedxDialogShowed);
		
	}
	
	@Test
	public final void testExecuteShowsDialogIfIfModelCannotBeResolved() throws ExecutionException, XMLException {	
		GElementBuilder builder = new GElementBuilder(Libsedml.readDocument(SEDML_EG).getSedMLModel());
		final GSedML gsedml = builder.build();
		
		mockery.checking(new Expectations(){
			{one(iep).getModel();will(returnValue(gsedml));}
		});
		handler.execute(null);
		assertFalse(handlerTSS.isSedxDialogShowed);
		assertTrue(handlerTSS.isModelNotResolved);
	}
	
	@Test
	public final void testExecute() throws ExecutionException, XMLException {	
		SedML sed = Libsedml.readDocument(SEDML_EG).getSedMLModel();
		sed.getModels().get(0).setSource(EXISTENT_MODEL);
		GElementBuilder builder = new GElementBuilder(sed);
		final GSedML gsedml = builder.build();
		
		mockery.checking(new Expectations(){
			{one(iep).getModel();will(returnValue(gsedml));}
		});
		handler.execute(null);
		assertFalse(handlerTSS.isSedxDialogShowed);
		assertFalse(handlerTSS.isModelNotResolved);
		assertTrue(handlerTSS.isPerformSaveCalled);
	}

}
