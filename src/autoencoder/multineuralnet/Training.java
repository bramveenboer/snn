package nnocr.multineuralnet;

import nnocr.objects.DataSet;

public class Training {
	protected DataSet samples;
	protected int iterations;
	protected double learningRate;
	protected double momentumFactor;
	
	Training(DataSet samples, int iterations, double learningRate, double momentumFactor) {
		this.samples = samples;
		this.iterations = iterations;
		this.learningRate = learningRate;
		this.momentumFactor = momentumFactor;
	}
}
