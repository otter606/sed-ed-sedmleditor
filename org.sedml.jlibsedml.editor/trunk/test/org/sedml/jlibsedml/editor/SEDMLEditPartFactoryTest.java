package org.sedml.jlibsedml.editor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.gef.EditPart;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sedml.jlibsedml.editor.gmodel.Connection;
import org.sedml.jlibsedml.editor.gmodel.ElementStub;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.GSedML;

@RunWith(JMock.class)
public class SEDMLEditPartFactoryTest {
	Mockery mockery = new JUnit4Mockery();
	EditPart ep = mockery.mock(EditPart.class);
	SEDMLEditPartFactory factory;
	@Before
	public void setUp() throws Exception {
		factory = new SEDMLEditPartFactory();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testCreateEditPartReturnsEPForGElement() {
		GElement model  =  new ElementStub();
		assertNotNull(factory.createEditPart(ep,model));
		assertEquals(model, factory.createEditPart(ep, model).getModel());
	}
	
	@Test
	public final void testCreateEditPartReturnsEPForConnection() {
		Connection conn  =  new Connection(new ElementStub(),new ElementStub());
		assertNotNull(factory.createEditPart(ep,conn));
		assertEquals(conn, factory.createEditPart(ep, conn).getModel());
	}
	
	@Test
	public final void testCreateEditPartReturnsEPSEDMLRoot() {
		GSedML rt  = new GSedML();
		assertNotNull(factory.createEditPart(ep,rt));
		assertEquals(rt, factory.createEditPart(ep, rt).getModel());
	}

}
