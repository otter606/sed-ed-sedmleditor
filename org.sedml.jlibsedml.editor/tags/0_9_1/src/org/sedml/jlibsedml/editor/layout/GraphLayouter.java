package org.sedml.jlibsedml.editor.layout;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.sedml.jlibsedml.editor.IViewChanger;
import org.sedml.jlibsedml.editor.gmodel.Connection;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.editor.gmodel.GSedML;
import org.sedml.jlibsedml.editor.gmodel.Location;
import org.sedml.jlibsedml.editor.gmodel.PropertyChangeNames;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

/**
 * USes jung graph library to layout a SEDML docuemnt. Optionally, nodes that should be 
 *  fixed can be passed into <br/>
 *  
 *  <code> public void setFixedNodes(List<GElement> selected) </code>
 * @author radams
 *
 */
public class GraphLayouter {
	private static final double SPACE_FACTOR = 5;
	
	/**
	 * 
	 * @param root A non-null {@link GSedML} root element
	 * @param editor An {@link IViewChanger}, typically the MapEditor
	 */
	public GraphLayouter(GSedML root, IViewChanger editor) {
		super();
		this.root = root;
		this.editor=editor;
	}

	private GSedML root;
	
	private List<GElement> selected= new ArrayList<GElement>();
	private IViewChanger editor;

	/**
	 * Sets nodes to preserve the location of in the layout. These nodes' positions will not be altered.
	 * @param selected A non-null <code>List</code> of layout elements.
	 */
	public void setFixedNodes(List<GElement> selected) {
		this.selected.clear();
		for (GElement el:selected) {
			this.selected.add(el);
		}
	}
	
	public void layout () {
		
		
		final Graph<GElement, Connection> graph = new DirectedSparseGraph<GElement, Connection>();
		Set<Connection> conns = new HashSet<Connection>();
		
		for (GElement gel : root.getChildren()) {
			conns.addAll(gel.getSrcConnections());
			conns.addAll(gel.getTargetConnections());
		}
		// build graph
		for (Connection conn : conns) {
			graph.addEdge(conn, conn.getSource(), conn.getTarget());
		}

		// calculate area of layout
		double area = GElement.DEFAULT_SIZE.getHeight()
				* GElement.DEFAULT_SIZE.getWidth()
				* root.getChildren().size() * SPACE_FACTOR;

		// configure layout algorithm
		final FRLayout<GElement, Connection> frout = new FRLayout<GElement, Connection>(
				graph);
		frout.initialize();
		frout.setSize(new Dimension((int) Math.sqrt(area), (int) Math
				.sqrt(area)));
		frout.setRepulsionMultiplier(0.5);
		frout.setAttractionMultiplier(0.5);
		frout.setMaxIterations(1000);

		// initialize layout with existing coords, these are not null
		for (GElement el : root.getChildren()) {
			frout.setLocation(el, el.getLocation().getX(), el.getLocation()
					.getY());
		}
		// lock selected parts
		for (GElement el: selected) {
			
				frout.lock(el, true);
			

		}
		
		// run layout in background job to access cancel and bg threading
		Job job = new LayoutJob("Layout", frout, graph);
		job.schedule();

	}
	class LayoutJob extends Job {
       private AbstractLayout<GElement, Connection> layout;
       Graph<GElement, Connection> graph;
		public LayoutJob(String name, FRLayout<GElement, Connection> frout, Graph<GElement, Connection> graph) {
			super(name);
			this.layout=frout;
			this.graph=graph;
			// TODO Auto-generated constructor stub
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			final int ITERATION_COUNT=500;
			try {
				monitor.beginTask("Laying out network", ITERATION_COUNT);
				for (int i = 0; i < ITERATION_COUNT ; i++) {
					if (monitor.isCanceled()) {
						return Status.CANCEL_STATUS;
					}
					((IterativeContext)layout).step();
					Display.getDefault().syncExec(new Runnable() {
                        // all UI changes has to be run in UI thread.
						public void run() {
							for (GElement el : graph.getVertices()) {
								el.setLocation(new Location((int) layout
										.getX(el), (int) layout.getY(el)));
								el.firePropertyChange(
										PropertyChangeNames.LOCATION_EVENT,
										null, el);
							}

							// forces repaint of figure
							editor.flushViewer();

						}

					});

					monitor.worked(1);

				}
			} finally {
				monitor.done();
			}
			return Status.OK_STATUS;
		}

		}
		
		
	}

	


