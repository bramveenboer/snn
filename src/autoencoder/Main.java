package autoencoder;

import java.io.PrintStream;
import java.util.Locale;
import java.util.Scanner;

import autoencoder.exception.AutoencoderException;
import autoencoder.geneticalgorithm.GeneticAlgorithm;
import autoencoder.neuralnet.NeuralNet;
import autoencoder.objects.DataSet;
import autoencoder.objects.Sample;
import autoencoder.util.InputSelector;

public class Main {

	/* Network properties */
	GeneticAlgorithm ga;
	int numberOfInputNodes  = 256,
	numberOfHiddenNodes = 4,
	numberOfOutputNodes = 10;

	/* Training parameters */
	int	iterations          = 500;
	double learningRate     = 0.1;
	double	momentumFactor  	= 0;

	/* Control booleans */
	boolean initialized = false;
	boolean run = true;

	/* Util */
	PrintStream out;

	Main() {
		Locale.setDefault(Locale.US);
		out = new PrintStream(System.out);
	}

	Sample readTrainingObject(Scanner input) throws AutoencoderException {
		Sample output = new Sample(numberOfInputNodes, numberOfOutputNodes);

		for(int i = 0; i < numberOfInputNodes; i++) {
			output.setInputValue(i, input.nextDouble());
		}
		for(int i = 0; i < numberOfOutputNodes; i++) {
			output.setOutputValue(i, input.nextInt());
		}
		return output;
	}

	DataSet readDataSet(Scanner input) throws AutoencoderException {
		DataSet output = new DataSet();
		while (input.hasNext()) {
			Scanner trainingObjectScanner = new Scanner(input.nextLine());
			Sample sample = readTrainingObject(trainingObjectScanner);
			output.add(sample);
		}
		return output;
	}

	int getMaxIndex(double[] input) {
		double max = input[0];
		int maxIndex = 0;
		for (int i = 1; i < input.length; i++) {
			double current = input[i];
			if (current > max) {
				max = current;
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	void classify(DataSet dataSet, GeneticAlgorithm ga) throws AutoencoderException {
		for (Sample sample : dataSet) {
			double[] inputs = sample.getInput();
			double[] targets = sample.getOutput();
			double[] results = ga.update(inputs);

			out.printf("Exptected -> Realized:\t");
			out.printf("%d -> %d\n", getMaxIndex(targets), getMaxIndex(results));
		}
	}

	double errorRate(DataSet dataSet, GeneticAlgorithm ga) throws AutoencoderException {
		int correct = 0;
		for (Sample sample : dataSet) {
			double[] inputs = sample.getInput();
			double[] targets = sample.getOutput();
			double[] results = ga.update(inputs);
			correct += getMaxIndex(targets) == getMaxIndex(results) ? 1 : 0; 

		}
		return (1 - (correct/(double)dataSet.size())) * 100;
	}

	void setup() throws AutoencoderException {
		numberOfInputNodes = 10;
		numberOfHiddenNodes = 4;
		numberOfOutputNodes = 10;
		ga = new NeuralNet(numberOfInputNodes, numberOfHiddenNodes, numberOfOutputNodes);
		initialized = true;
	}

	void train() throws AutoencoderException {
		if (!initialized) {
			throw new AutoencoderException("Can not train uninitialized network");
		}
		iterations = 100;
		learningRate = 0.5;
		momentumFactor = 0;
		DataSet trainingSet = readDataSet(InputSelector.getInput());
		ga.train(trainingSet, iterations, learningRate, momentumFactor);
	}

	void classify() throws AutoencoderException {
		if (!initialized) {
			throw new AutoencoderException("Can not classify with uninitialized network");
		}
		DataSet testSet = readDataSet(InputSelector.getInput());
		classify(testSet, ga);
	}

	void start() {
		try {
			setup();
			train();
			classify();
		} catch (AutoencoderException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] argv) {
		new Main().start();
	}
}
