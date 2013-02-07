package nnocr;

import java.util.Locale;

import nnocr.exception.nnocrException;
import nnocr.multineuralnet.MultiNeuralNet;
import nnocr.neuralnet.NeuralNet;
import nnocr.objects.DataSet;
import nnocr.util.InputSelector;

public class ParameterTestingPlace extends Main {

	DataSet set1;
	DataSet set2;
	DataSet set3;
	
	DataSet trainingSet;
	DataSet testSet;
	
	double learningRate = 0.15;
	double momentumFactor = 0;
	int numberOfInputNodes  = 256;
	int numberOfHiddenNodes = 4;
	int numberOfOutputNodes = 10;
	int iterations = 500;
	
	ParameterTestingPlace() {
		Locale.setDefault(Locale.US);
		try {
			set1 = readDataSet(InputSelector.getInput());
			set2 = readDataSet(InputSelector.getInput());
			set3 = readDataSet(InputSelector.getInput());
		} catch (nnocrException e) {
			e.printStackTrace();
		}
	}
	
	void testParametersRepeated() throws nnocrException {
		trainingSet = set1;
		testSet = set2;
		testParameters();
		
		trainingSet = set1;
		testSet = set3;
		out.println("=======================================");
		testParameters();
		
		trainingSet = set2;
		testSet = set1;
		out.println("=======================================");
		testParameters();
		
		trainingSet = set2;
		testSet = set3;
		out.println("=======================================");
		testParameters();
		
		trainingSet = set3;
		testSet = set1;
		out.println("=======================================");
		testParameters();
	}
	/**
	 * Test number of iterations, learning rate, momentum, and number of hidden nodes
	 */
	void testParameters() throws nnocrException {	
		testIterations();
		out.println("------------------------------");
		testLearningRate();
		out.println("------------------------------");
		testMomentumFactor();
		out.println("------------------------------");
		testHiddenNodes();
		out.println("------------------------------");
	}
	
	void testIterations() throws nnocrException {
		out.printf("Iterations \t mnn \t nn\n");
		for (int i = 0; i < 1000; i += 10) {
			NeuralNet nn = new NeuralNet(numberOfInputNodes, numberOfHiddenNodes, numberOfOutputNodes);
			MultiNeuralNet mnn = new MultiNeuralNet(numberOfInputNodes, numberOfHiddenNodes, numberOfOutputNodes);
			nn.train(trainingSet, i, learningRate, momentumFactor);
			mnn.train(trainingSet, i, learningRate, momentumFactor);
			double errorRateMNN = errorRate(testSet, mnn);
			double errorRateNN = errorRate(testSet, nn);
			out.printf("%d \t %f \t %f\n", i, errorRateMNN, errorRateNN);
		}
	}
	
	void testLearningRate() throws nnocrException {
		out.printf("Learningrate \t mnn \t nn\n");
		for (double i = 0; i < 1; i += 0.01) {
			NeuralNet nn = new NeuralNet(numberOfInputNodes, numberOfHiddenNodes, numberOfOutputNodes);
			MultiNeuralNet mnn = new MultiNeuralNet(numberOfInputNodes, numberOfHiddenNodes, numberOfOutputNodes);
			nn.train(trainingSet, iterations, i, momentumFactor);
			mnn.train(trainingSet, iterations, i, momentumFactor);
			double errorRateMNN = errorRate(testSet, mnn);
			double errorRateNN = errorRate(testSet, nn);
			out.printf("%f \t %f \t %f\n", i, errorRateMNN, errorRateNN);
		}
	}
	
	void testMomentumFactor() throws nnocrException {
		out.printf("Momentumfactor \t mnn \t nn\n");
		for (double i = 0; i < 1; i += 0.01) {
			NeuralNet nn = new NeuralNet(numberOfInputNodes, numberOfHiddenNodes, numberOfOutputNodes);
			MultiNeuralNet mnn = new MultiNeuralNet(numberOfInputNodes, numberOfHiddenNodes, numberOfOutputNodes);
			nn.train(trainingSet, iterations, learningRate, i);
			mnn.train(trainingSet, iterations, learningRate, i);
			double errorRateMNN = errorRate(testSet, mnn);
			double errorRateNN = errorRate(testSet, nn);
			out.printf("%f \t %f \t %f\n", i, errorRateMNN, errorRateNN);
		}
	}

	void testHiddenNodes() throws nnocrException {
		out.printf("Hidden nodes \t mnn \t nn\n");
		for (int i = 0; i < 100; i += 1) {
			NeuralNet nn = new NeuralNet(numberOfInputNodes, i, numberOfOutputNodes);
			MultiNeuralNet mnn = new MultiNeuralNet(numberOfInputNodes, i, numberOfOutputNodes);
			nn.train(trainingSet, iterations, learningRate, momentumFactor);
			mnn.train(trainingSet, iterations, learningRate, momentumFactor);
			double errorRateMNN = errorRate(testSet, mnn);
			double errorRateNN = errorRate(testSet, nn);
			out.printf("%d \t %f \t %f\n", i, errorRateMNN, errorRateNN);
		}
	}
	
	public static void main(String[] argv) {
		try {
			new ParameterTestingPlace().testParametersRepeated();
		} catch (nnocrException e) {
			e.printStackTrace();
		}
	}
}
