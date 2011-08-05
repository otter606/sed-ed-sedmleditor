package org.sedml.jlibsedml.editor.junglayout;


import static org.junit.Assert.assertEquals;

import java.awt.Dimension;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sedml.Libsedml;
import org.sedml.SEDMLDocument;
import org.sedml.SedML;
import org.sedml.jlibsedml.editor.gmodel.Connection;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.GElementBuilder;
import org.sedml.jlibsedml.editor.gmodel.GSedML;
import org.sedml.jlibsedml.editor.gmodel.Location;
import org.sedml.jlibsedml.editor.gmodel.SEDMLBuilder;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

public class TetJungLayout {
	GSedML gsedml;
	@Before
	public void setUp() throws Exception {
		SedML sed = Libsedml.readDocument(new File("TestData/sedMLBIOM12.xml")).getSedMLModel();
		gsedml = new GElementBuilder(sed).build();
	}
	
	@Test
	public void TestLayout(){
		Graph<GElement,Connection>graph = new DirectedSparseGraph<GElement, Connection>();
		Set<Connection> conns = new HashSet<Connection>();
		
		for (GElement gel: gsedml.getChildren()){
			conns.addAll(gel.getSrcConnections());
			conns.addAll(gel.getTargetConnections());
		}
		for (Connection conn: conns){
			graph.addEdge(conn, conn.getSource(), conn.getTarget());
		}
		assertEquals(41,graph.getVertexCount());
		
		SpringLayout <GElement,Connection> lout =  new SpringLayout<GElement,Connection>(graph);
		lout.initialize();
		lout.setSize(new Dimension(1000,1000));
		lout.setRepulsionRange(200);
		
		
		for (int i = 0; i< 500; i++) {
			System.err.println("stepping");
			lout.step();
			
		}
		for (GElement el: graph.getVertices()){
			el.setLocation(new Location((int)lout.getX(el) , (int)lout.getY(el)));
		}
		SEDMLBuilder out = new SEDMLBuilder();
		SedML outsed = out.buildSEDML(gsedml);
		new SEDMLDocument(outsed).writeDocument(new File("out.xml"));
		
		KKLayout<GElement,Connection> kout =  new KKLayout<GElement,Connection>(graph);
		kout.initialize();
		kout.setSize(new Dimension(1000, 1000));
		kout.setLengthFactor(400);
		
		kout.setMaxIterations(500);
		while(!kout.done()){
			kout.step();
		}
		for (GElement el: graph.getVertices()){
			el.setLocation(new Location((int)kout.getX(el) , (int)kout.getY(el)));
		}
		SedML out2 = out.buildSEDML(gsedml);
		new SEDMLDocument(out2).writeDocument(new File("outkk.xml"));
		
		FRLayout<GElement,Connection> frout =  new FRLayout<GElement,Connection>(graph);
		frout.initialize();
		frout.setSize(new Dimension(1000, 1000));
		frout.setRepulsionMultiplier(0.5);
		frout.setAttractionMultiplier(0.1);
		frout.setMaxIterations(1000);
	
		while(!frout.done()){
			frout.step();
		}
		for (GElement el: graph.getVertices()){
			el.setLocation(new Location((int)frout.getX(el) , (int)frout.getY(el)));
		}
		SedML out3 = out.buildSEDML(gsedml);
		new SEDMLDocument(out3).writeDocument(new File("outfr.xml"));
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	
}
