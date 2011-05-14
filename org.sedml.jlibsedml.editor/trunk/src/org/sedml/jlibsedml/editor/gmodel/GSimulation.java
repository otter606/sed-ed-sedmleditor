package org.sedml.jlibsedml.editor.gmodel;

import org.sedml.Algorithm;
import org.sedml.SEDMLTags;
import org.sedml.Simulation;
import org.sedml.UniformTimeCourse;
import org.sedml.modelsupport.KisaoOntology;

public class GSimulation extends GElement {
	
	private double start=0.0, outStart=0.0, outEnd=10.0;
	private int numPoints=100;
	
	
	private Algorithm algorithm = new Algorithm(KisaoOntology.ALGORITHM_WITH_DETERMINISTIC_RULES.getId());

	public GSimulation(UniformTimeCourse sim2) {
		super(sim2);
		start=sim2.getInitialTime();
		outStart=sim2.getOutputStartTime();
		outEnd=sim2.getOutputEndTime();
		numPoints=sim2.getNumberOfPoints();
		if(sim2.getAlgorithm()!=null){
			algorithm=sim2.getAlgorithm();
		}
		setId(sim2.getId());
		setName(sim2.getName());
	}

	public GSimulation() {
		// TODO Auto-generated constructor stub
	}

	 GSimulation(GSimulation toCopy) {
		super(toCopy);
		setStart(toCopy.getStart());
		setOutStart(toCopy.getOutStart());
		setOutEnd(toCopy.getOutEnd());
		setNumPoints(toCopy.getNumPoints());
		setAlgorithm(new Algorithm(toCopy.getAlgorithm().getKisaoID()));
	}

	public double getStart() {
		return start;
	}

	public void setStart(double start) {
		this.start = start;
	}

	public double getOutStart() {
		return outStart;
	}

	public void setOutStart(double outStart) {
		this.outStart = outStart;
	}

	public double getOutEnd() {
		return outEnd;
	}

	public void setOutEnd(double outEnd) {
		this.outEnd = outEnd;
	}

	public int getNumPoints() {
		return numPoints;
	}

	public void setNumPoints(int numPoints) {
		this.numPoints = numPoints;
	}

	public Algorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	@Override
	public boolean canGetSedML() {
		return getId()!=null && getAlgorithm()!=null;
	}

	
	Simulation getSEDMLObject() {
		assert(canGetSedML());
		Simulation s= new UniformTimeCourse(getId(),getName(),
					getStart(),getOutStart(),getOutEnd(),getNumPoints(),getAlgorithm());
		s.addAnnotation(createGraphicalInfoAnnotations());
		addNotes(s);
		return s;
	}
	@Override
	public String getType() {
		return SEDMLTags.SIM_UTC;
	}

	@Override
	public GSimulation getCopy() {
		return new GSimulation(this);
	}

}
