package nnocr.multineuralnet;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import nnocr.exception.nnocrException;
import nnocr.geneticalgorithm.GeneticAlgorithm;
import nnocr.objects.DataSet;

public class MultiNeuralNet implements GeneticAlgorithm {

	protected int numberOfInputNodes,
				  numberOfHiddenNodes,
				  numberOfOutputNodes;
	protected ArrayList<NeuralClassifier> neuralClassifiers;
	protected Semaphore semaphore;
	
	public MultiNeuralNet(int numberOfInputNodes, int numberOfHiddenNodes, int numberOfOutputNodes) {
		this.numberOfInputNodes  = numberOfInputNodes;
		this.numberOfHiddenNodes = numberOfHiddenNodes;
		this.numberOfOutputNodes = numberOfOutputNodes;
		this.semaphore = new Semaphore(numberOfOutputNodes);
		this.neuralClassifiers = makeNeuralNets();
	}
	
	protected ArrayList<NeuralClassifier> makeNeuralNets() {
		ArrayList<NeuralClassifier> neuralClassifiers = new ArrayList<NeuralClassifier>();
		for(int i = 0; i < numberOfOutputNodes; i++) {
			double[] target = new double[numberOfOutputNodes];
			target[i] = 1;
			neuralClassifiers.add(new NeuralClassifier(numberOfInputNodes, numberOfHiddenNodes, 1, target));
		}
		return neuralClassifiers;
	}
	
	/* Update */
	public double[] update(double[] inputs) throws nnocrException {
		if (inputs.length != numberOfInputNodes) {
			throw new nnocrException("Wrong number of inputs");
		}
		double[] outputs = new double[numberOfOutputNodes];
		for(int i = 0; i < numberOfOutputNodes; i++) {
			outputs[i] = neuralClassifiers.get(i).update(inputs)[0];
		}
		return outputs;
	}
	
	/* Train */
	public void train(DataSet samples, int iterations, double learningRate, double momentumFactor) {
		Training training = new Training(samples, iterations, learningRate, momentumFactor);
		for (NeuralClassifier neuralClassifier : neuralClassifiers) {
			neuralClassifier.train(training, semaphore);
		}
		resetSemaphores();
	}
	
	protected void resetSemaphores() {
		for (int i = 0; i < numberOfOutputNodes; i++) {
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		semaphore.release(numberOfOutputNodes);
	}
	
	/* Graphics */
	public void draw(){
		for(int i = 0; i < neuralClassifiers.size(); i++){
			neuralClassifiers.get(i).draw();
		}
	}
}
