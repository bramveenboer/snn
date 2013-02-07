package nnocr.neuralnet;

import java.util.Arrays;

import nnocr.exception.nnocrException;
import nnocr.geneticalgorithm.GeneticAlgorithm;
import nnocr.graphics.Drawing;
import nnocr.objects.DataSet;
import nnocr.objects.Sample;

public class NeuralNet implements GeneticAlgorithm{	
	/* Constants */
	protected static final double MIN_RANDOM = -0.2;
	protected static final double MAX_RANDOM = 0.2;
	
	/* Number of nodes */
	protected int numberOfInputNodes;
	protected int numberOfHiddenNodes;
	protected int numberOfOutputNodes;
	
	/* Activations for nodes */
	protected double[] activationsInput;
	protected double[] activationsHidden;
	protected double[] activationsOutput;
	
	/* Weights */
	protected double[][] weightsInput;
	protected double[][] weightsOutput;
	
	/* Momemtum */
	protected double[][] momentumInput;
	protected double[][] momentumOutput;
	
	/* Graphics */
	protected int UI_WIDTH = 800;
	protected int UI_HEIGHT = 400;
	
	public NeuralNet(int numberOfInputNodes, int numberOfHiddenNodes, int numberOfOutputNodes) {
		// number of input, hidden, and output nodes
		this.numberOfInputNodes = numberOfInputNodes;
		this.numberOfHiddenNodes = numberOfHiddenNodes;
		this.numberOfOutputNodes = numberOfOutputNodes;
		// activations for node
		this.activationsInput = new double[this.numberOfInputNodes];
		this.activationsHidden = new double[this.numberOfHiddenNodes];
		this.activationsOutput = new double[this.numberOfOutputNodes];
		// create random weights
		this.weightsInput = new double[this.numberOfInputNodes][this.numberOfHiddenNodes];
		fillRandom(weightsInput, MIN_RANDOM, MAX_RANDOM);
		this.weightsOutput = new double[this.numberOfHiddenNodes][this.numberOfOutputNodes];
		fillRandom(weightsOutput, 10*MIN_RANDOM, 10*MAX_RANDOM);
		// create momentums
		this.momentumInput = new double[this.numberOfInputNodes][this.numberOfHiddenNodes];
		this.momentumOutput = new double[this.numberOfHiddenNodes][this.numberOfInputNodes];
	}
	
	/* Update */
	public double[] update(double[] inputs) throws nnocrException {
		if (inputs.length != numberOfInputNodes) {
			throw new nnocrException("Wrong number of inputs");
		}
		// update input activations
		for (int i = 0; i < numberOfInputNodes; i++) {
			activationsInput[i] = inputs[i];
		}
		// update hidden activations
		for (int h = 0; h < numberOfHiddenNodes; h++) {
			double sum = 0;
			for (int i = 0; i < numberOfInputNodes; i++) {
				sum += activationsInput[i] * weightsInput[i][h];
			}
			this.activationsHidden[h] = sigmoid(sum);
		}
		// update output activations
		for (int o = 0; o < numberOfOutputNodes; o++) {
			double sum = 0;
			for (int h = 0; h < numberOfHiddenNodes; h++) {
				sum += activationsHidden[h] * weightsOutput[h][o];
			}
			activationsOutput[o] = sigmoid(sum);
		}
		return activationsOutput;
	}
	
	
	/* Backpropagate */
	protected double backPropagate(double[] targets, double learningRate, double momentumFactor) throws nnocrException {
		if (targets.length != this.numberOfOutputNodes) {
			throw new nnocrException("Wrong number of target values");
		}
		// calculate errors for output
		double[] outputDeltas = new double[this.numberOfOutputNodes];
		for (int o = 0; o < this.numberOfOutputNodes; o++ ) {
			double error = targets[o] - this.activationsOutput[o];
			outputDeltas[o] = dsigmoid(this.activationsOutput[o]) * error;
		}
		// calculate errors for hidden
		double[] hiddenDeltas = new double[this.numberOfHiddenNodes];
		for (int h = 0; h < this.numberOfHiddenNodes; h++) {
			double error = 0;
			for (int o = 0; o < this.numberOfOutputNodes; o++) {
				error += outputDeltas[o] * this.weightsOutput[h][o];
			}
			hiddenDeltas[h] = dsigmoid(this.activationsHidden[h]) * error;
		}
		// update output weights
		for (int h = 0; h < this.numberOfHiddenNodes; h++) {
			for(int o = 0; o < this.numberOfOutputNodes; o++) {
				double change = outputDeltas[o] * this.activationsHidden[h] + momentumFactor * this.momentumOutput[h][o];
				this.weightsOutput[h][o] += learningRate * change;
				this.momentumOutput[h][o] = change;
			}
		}
		// update input weights
		for (int i = 0; i < this.numberOfInputNodes; i++) {
			for (int h = 0; h < this.numberOfHiddenNodes; h++) {
				double change = hiddenDeltas[h] * this.activationsInput[i];
				this.weightsInput[i][h] += learningRate * change + momentumFactor * this.momentumInput[i][h];
				this.momentumInput[i][h] = change;
			}
		}
		// calculate error
		double error = 0;
		for (int i = 0; i < targets.length; i++) {
			error += 0.5 * Math.pow((targets[i] - this.activationsOutput[i]), 2);
		}
		return error;
	}

	/* Test method to easily verify if the neural net works correctly for some DataSet */
	public void test(DataSet samples) throws nnocrException {
		for(Sample sample : samples) {
			double[] inputs = sample.getInput();
			double[] targets = sample.getOutput();
			double[] results = update(inputs);
			System.out.printf("%s -> %s\n", Arrays.toString(targets), Arrays.toString(results));
		}
	}
	
	/* Train */
	public void train(DataSet samples, int iterations, double learningRate, double momentumFactor) throws nnocrException {
		for (int i = 0; i < iterations; i++) {
			double error = 0;
			for (Sample sample : samples) {
				double[] inputs = sample.getInput();
				double[] targets = sample.getOutput();
				update(inputs);
				error += backPropagate(targets, learningRate, momentumFactor);
			}
			/* Printing errors is only recommended to verify if training really works */
			/*if (i % 100 == 0) {
				System.out.printf("Error %f\n", error);
			}*/
		}
	}
	
	/* Util */
	protected double random(double min, double max) {
		return ((max - min) * Math.random()) + min;
	}
	
	protected void fillRandom(double[][] doubleArray, double minRandom, double maxRandom) {
		for (int i = 0 ; i < doubleArray.length; i++) {
			for (int j = 0; j < doubleArray[0].length; j++) {
				doubleArray[i][j] = random(minRandom, maxRandom);
			}
		}		
	}
	
	protected double sigmoid(double x) {
		return Math.tanh(x);
	}
	
	protected double dsigmoid(double y) {
		return 1 - Math.pow(y, 2);
	}

	public int getNumberOfHiddenNodes() {
		return numberOfHiddenNodes;
	}

	public int getNumberOfOutputNodes() {
		return numberOfOutputNodes;
	}
	
	public int getNumberOfInputNodes() {
		return numberOfInputNodes;
	}
	
	public double getInputWeight(int input, int hidden){
		return weightsInput[input][hidden];
	}
	
	public double getOutputWeight(int input, int hidden){
		return weightsOutput[input][hidden];
	}
	
	/* Graphics */	
	public void draw(){
		Drawing.draw(this);
	}	
}