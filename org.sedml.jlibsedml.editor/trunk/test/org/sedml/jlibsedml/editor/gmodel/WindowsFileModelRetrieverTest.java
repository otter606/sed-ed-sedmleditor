package org.sedml.jlibsedml.editor.gmodel;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.jlibsedml.execution.IModelResolver;
import org.junit.Before;
import org.junit.Test;

public class WindowsFileModelRetrieverTest {
	private static final String TEST_DATA_SED_MLBIOM12_XML = "TestData/sedMLBIOM12.xml";
	IModelResolver resolver;
	
	@Before
	public void setUp(){
		resolver =  new WindowsFileRetriever();
	}
	@Test
	public void testGetModelXMLForTestDataRelativePath() throws URISyntaxException {
		if(!WindowsFileRetriever.isWindows()){
			return;
		}
		URI uri = new URI(TEST_DATA_SED_MLBIOM12_XML);
		assertNotNull(resolver.getModelXMLFor(uri));
	}
	
	@Test
	
	public void testGetModelXMLForTestDataAbsPathDoesnNotFormURI()  {
		if(!WindowsFileRetriever.isWindows()){
			return;
		}
		File f = new File(TEST_DATA_SED_MLBIOM12_XML);
		// this won't work as has backslashes
		
		try {
			URI uri = new URI(f.getAbsolutePath());
		} catch (URISyntaxException e) {
			return;
		}
		fail("Did not throw URISyntax Exception");
		
	}
	
	@Test
	public void testGetModelXMLForTestDataAbsPathMustConvert() throws URISyntaxException {
		if(!WindowsFileRetriever.isWindows()){
			return;
		}
		File f = new File(TEST_DATA_SED_MLBIOM12_XML);
		String uriStr=WindowsFileRetriever.convertAbsoluteFilePathToURI(f.getAbsolutePath());
		URI uri = new URI(uriStr);
		assertNotNull(resolver.getModelXMLFor(uri));
	}

}
