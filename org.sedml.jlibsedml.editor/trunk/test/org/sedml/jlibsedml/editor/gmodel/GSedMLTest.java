package org.sedml.jlibsedml.editor.gmodel;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sedml.SEDBase;

public class GSedMLTest {
	GSedML gsedml;
	@Before
	public void setUp() throws Exception {
		gsedml= new GSedML();
		
	}
	public class GElementStub extends GElement {
		public boolean canGetSedML() {
			return false;
		}

		@Override
		SEDBase getSEDMLObject() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getType() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testAddChild() {
		gsedml.addChild(new GElementStub());
		assertEquals(1,gsedml.getChildren().size());
	}

	@Test
	public final void testRemoveChild() {
		GElement el = new GElementStub();
		gsedml.addChild(el);
		gsedml.removeChild(el);
		assertEquals(0,gsedml.getChildren().size());
	}

	

	@Test
	public final void testSetChildIndex() {
		
		GElement el1 = new GElementStub();
		GElement el2 =  new GElementStub();
		gsedml.addChild(el1);
		gsedml.addChild(el2);
		gsedml.setChildIndex(el2, 0);
		assertEquals(el2,gsedml.getChildren().get(0));
	}



	@Test
	public final void testGenerateID() {
		long CURRMAX=123;
		ElementStub es1 = createELementStub("x"+CURRMAX);
		ElementStub es2 = createELementStub("id");
		ElementStub es3 = createELementStub("23c4");
		addChildren(es1,es2,es3);
		assertEquals(GSedML.PREFIX+(CURRMAX+1), gsedml.generateIDForChildElement());
		
	}
	
	@Test
	public final void testGenerateIDWhenNoChildren() {
		String EXPECTED=GSedML.PREFIX + "1";
		assertNotNull(gsedml.generateIDForChildElement());
		assertEquals(EXPECTED, gsedml.generateIDForChildElement());
		
	}
	
	@Test
	public final void testhasIncompleteLayout() {
		ElementStub es1 = createELementStub("x");
		ElementStub es2 = createELementStub("id");
		ElementStub es3 = createELementStub("23c4");
		gsedml.addChild(es1);
		gsedml.addChild(es2);
		gsedml.addChild(es3);
		assertTrue(gsedml.hasIncompleteLayout());
		Location l = new Location(2, 3);
		es1.setLocation(l);
		assertEquals(1,gsedml.getChildrenWithGraphicalInfo().size());
		es2.setLocation(l);
		es3.setLocation(l);
		assertFalse(gsedml.hasIncompleteLayout());

	}

	private void addChildren(ElementStub ... stubs) {
		for (ElementStub stub: stubs){
			gsedml.addChild(stub);
		}
		
	}

	private ElementStub createELementStub(String id) {
		ElementStub s = new ElementStub();
		s.setId(id);
		return s;
	
	}

}
