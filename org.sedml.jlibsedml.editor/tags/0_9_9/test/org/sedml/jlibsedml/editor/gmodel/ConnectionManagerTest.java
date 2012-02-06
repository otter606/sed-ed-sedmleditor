package org.sedml.jlibsedml.editor.gmodel;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConnectionManagerTest {

	ConnectionManager manager;
	GTask task = TestUtils.createValidGTask("S");
	@Before
	public void setUp() throws Exception {
		manager= new ConnectionManager();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testCanConnect() {
		assertTrue(manager.canConnectReferenceRelations(new GTask(), new GModel() ));
		assertTrue(manager.canConnectReferenceRelations(new GTask(), new GSimulation() ));
		assertTrue(manager.canConnectReferenceRelations(new GVariable(), new GTask() ));
		assertFalse(manager.canConnectReferenceRelations(new GVariable(), new GModel() ));
		
		assertTrue(manager.canConnectReferenceRelations(new GCurve(), new GDataGenerator() ));
		assertTrue(manager.canConnectReferenceRelations(new GDataset(), new GDataGenerator() ));
	}
	
	@Test
	public final void testCanConnectAgg() {
		assertTrue(manager.canConnectAggregateRelations(new GModel(), new GChange() ));
		assertTrue(manager.canConnectAggregateRelations(new GDataGenerator(), new GParameter() ));
		assertTrue(manager.canConnectAggregateRelations(new GDataGenerator(), new GVariable() ));
	
		assertTrue(manager.canConnectAggregateRelations(new GPlot2D(), new GCurve()));
		assertTrue(manager.canConnectAggregateRelations(new GReport(), new GDataset() ));
	}
	
	@Test
	public final void testVariableCantHaveTwoParents (){
		GVariable var = new GVariable();
		GDataGenerator dg = new GDataGenerator();
		Connection conn = new Connection(dg, var);
		GDataGenerator dg2 = new GDataGenerator();
		GVariable var2 = new GVariable();
		// regular connect
		assertFalse(manager.canConnect(dg2,var, false,false ));
		
		assertTrue(manager.canConnect(dg2,var2, false,false ));
		// connect 2
		Connection conn2 = new Connection(dg2,var2);
		
		assertTrue(manager.canConnect(dg2,var, true,false )); // src reconnect OK -dg has 2 vars
		assertFalse(manager.canConnect(dg2,var, false,true )); // target reconnect not OK 
		
	}
	
	@Test
	public final void testDatasetCantHaveTwoParents (){
		GDataset dat = new GDataset();
		GReport rep = new GReport();
		Connection conn = new Connection(rep, dat);
		GReport rep2 = new GReport();
		GDataset dat2 = new GDataset();
		// regular connect
		assertFalse(manager.canConnect(rep2,dat, false,false ));
		
		assertTrue(manager.canConnect(rep2,dat2, false,false ));
		// connect 2
		Connection conn2 = new Connection(rep2,dat2);
		
		assertTrue(manager.canConnect(rep2,dat, true,false )); // src reconnect OK -dg has 2 vars
		assertFalse(manager.canConnect(rep2,dat,false,true)); // target reconnect not OK 
		
	}
	
	@Test
	public final void testTaskCantBeConnectedToTwoModels (){
		GTask task1 = TestUtils.createInValidGTask("task1");
		GModel model1 = TestUtils.createValidGModel("model1");
		Connection conn = new Connection(task1, model1);
		GTask task2 = TestUtils.createInValidGTask("task1");
		GModel m2 = TestUtils.createValidGModel("model2");
		// regular connect
		assertTrue(manager.canConnect(task2,m2, false,false ));
		
		assertTrue(manager.canConnect(task1,m2, false,true ));//connect task to unconnected model OK
		Connection conn2 = new Connection(task2, m2);
		assertTrue(manager.canConnect(task2,model1, true,false )); //can connecc
		
		
	}
	

	@Test
	public final void testVariableCantBeConnectedToTwoTasks (){
		GTask task1 = TestUtils.createInValidGTask("task1");
		GVariable v1 = new GVariable();
	
		GTask task2 = TestUtils.createInValidGTask("task1");
		GVariable v2 = new GVariable();
		// regular connect
		assertTrue(manager.canConnect(v1,task1, false,false ));
		Connection conn = new Connection(v1, task1);
		
		assertTrue(manager.canConnect(v2,task1, false,true ));//connect var to existing task
		
		Connection conn2 = new Connection(v2, task2);
		assertTrue(manager.canConnect(v1,task2, false,true )); //can connecc
		
		
		assertFalse(manager.canConnect(v1,task2, true,false )); //can connecc
		
		
	}
	
	@Test
	public final void testDatasetCantBeConnectedToTwoDatagenerators (){
		GDataGenerator dg1 = new GDataGenerator();
		GDataGenerator dg2 = new GDataGenerator();
		GDataset ds1 = new GDataset();
		GDataset ds2 = new GDataset();
		// regular connect
		assertTrue(manager.canConnect(ds1,dg1, false,false ));
		Connection conn = new Connection(ds1,dg1);
		
		assertTrue(manager.canConnect(ds1,dg2,false,true ));//reconnect to new DG
		assertTrue(manager.canConnect(ds2,dg1,false,false ));//reconnect to new DG
		Connection conn2 = new Connection(ds2,dg2);
		assertFalse(manager.canConnect(ds1,dg2,true,false ));//cant reconnect to Var with already dg
		assertTrue(manager.canConnect(ds1,dg2,false,true ));//reconnect to new DG
		
		
		
		
	}
	
	
	@Test
	public final void testCurveCantHaveTwoParents (){
		GCurve curve = new GCurve();
		GPlot2D plot2d = new GPlot2D();
		Connection conn = new Connection(plot2d, curve);
		GCurve curve2 = new GCurve();
		GPlot2D plot2d2 = new GPlot2D();
		// regular connect
		assertFalse(manager.canConnect(plot2d2,curve, false,false ));
		
		assertTrue(manager.canConnect(plot2d2,curve2, false,false ));
		// connect 2
		Connection conn2 = new Connection(plot2d2,curve2);
		
		assertTrue(manager.canConnect(plot2d2,curve, true,false )); // src reconnect OK -dg has 2 vars
		assertFalse(manager.canConnect(plot2d2,curve,false,true)); // target reconnect not OK 
		
	}

}
