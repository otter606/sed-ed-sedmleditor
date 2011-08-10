package org.sedml.jlibsedml.editor.gmodel;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GElementTest {

	GElement toTest;
	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetLocationIsNotNullWhenCreated() {
		toTest=new ElementStub();
		assertNotNull(toTest.getLocation());
	}

	@Test
	public final void testGetSizeIsNotNullWhenCreated() {
		toTest=new ElementStub();
		assertNotNull(toTest.getSize());
	}
	
	@Test
	public final void testCanGetModelIsFalseByDEfault() {
		toTest=new ElementStub();
		assertFalse(toTest.canGetModel());
	}
	
	@Test
	public final void testGetModelDocumentReturnsNullByDefault() {
		toTest=new ElementStub();
		assertNull(toTest.getModelDocument(true));
	}

}
