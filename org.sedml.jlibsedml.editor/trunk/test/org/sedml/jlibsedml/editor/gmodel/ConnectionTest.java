package org.sedml.jlibsedml.editor.gmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConnectionTest {

	Connection conn;
	GElement src,targ, newsrc,newtarg;
	PropListenerStub stub;
	@Before
	public void setUp() throws Exception {
		src= new ElementStub();
		targ=new ElementStub();
		newsrc= new ElementStub();
		newtarg=new ElementStub();
		stub=new PropListenerStub();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected=IllegalArgumentException.class)
	public final void testConnectionWithNullThrowsIAE() {
		new Connection (null,null);
	}
	
	@Test
	public final void testConnectionConstructorConnects() {
		conn=createConnection(src,targ);
		assertEquals(1,src.getSrcConnections().size());
		assertEquals(1,targ.getTargetConnections().size());
		assertEquals(src,conn.getSource());
		assertEquals(targ,conn.getTarget());
	}

	private Connection createConnection(GElement src, GElement targ) {
		return new Connection (src,targ);
	}


	@Test
	public final void testFirePropertyChange() {
		conn=createConnection(src,targ);
		conn.addPropertyChangeListener(stub);
		conn.firePropertyChange("TEST",null,conn);
		assertEquals("TEST",stub.evt.getPropertyName());
	}
		

	@Test
	public final void testRemovePropertyChangeListener() {
		conn=createConnection(src,targ);
		conn.addPropertyChangeListener(stub);
		conn.removePropertyChangeListener(stub);
		conn.firePropertyChange("TEST",null,conn);
		assertNull(stub.evt);
	}

	@Test
	public final void testDisconnect() {
		conn=createConnection(src,targ);
		conn.disconnect();
		assertFalse(src.isConnected());
		assertFalse(targ.isConnected());
	}



	@Test
	public final void testReconnect() {
		conn=createConnection(src,targ);
		conn.disconnect();
		conn.reconnect();
		assertTrue(src.isConnected());
		assertTrue(targ.isConnected());
		
	}

	@Test
	public final void testReconnectGElementGElement() {
		conn=createConnection(src,targ);
		conn.reconnect(newsrc, newtarg);
		assertTrue(newsrc.isConnected());
		assertTrue(newtarg.isConnected());
		assertFalse(src.isConnected());
		assertFalse(targ.isConnected());
	}

}
