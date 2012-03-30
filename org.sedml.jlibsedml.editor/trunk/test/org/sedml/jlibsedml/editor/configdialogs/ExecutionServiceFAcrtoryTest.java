package org.sedml.jlibsedml.editor.configdialogs;


import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExecutionServiceFAcrtoryTest {

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testNotNull(){
		assertNotNull(ExecutorServiceFactory.getInstance());
	}
	
	@Test(expected=IllegalStateException.class)
	public void assertGetExecutorThrowsISEIfNotSetWithNonNullValue(){
		ExecutorServiceFactory.getInstance().getCommandExecutor();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void assertSetExecutorThrowsIAEIfSetWithNullValue(){
		ExecutorServiceFactory.getInstance().setCommandExecutor(null);
	}

	@After
	public void tearDown() throws Exception {
	}

}
