package autoencoder;

import java.io.PrintStream;
import java.util.Locale;
import java.util.Scanner;

import autoencoder.exception.AutoencoderException;
import autoencoder.neuralnet.Network;
import autoencoder.objects.Dataset;
import autoencoder.objects.Sample;
import autoencoder.util.InputSelector;

public class Main {

	/* Network properties */
	Network network;
	int nInput  	= 16;
	int nHidden		= 3;
	int nOutput 	= 16;

	/* Training parameters */
	int	iterations          = 100;
	double learningRate     = 0.1;
	double	momentumFactor  = 0;

	/* Util */
	PrintStream out;
	boolean initialized;

	Main() {
		Locale.setDefault(Locale.US);
		out = new PrintStream(System.out);
	}

	Sample readTrainingObject(Scanner input) throws AutoencoderException {
		Sample output = new Sample(nInput, nHidden);

		for(int i = 0; i < nInput; i++) {
			output.setInputValue(i, input.nextDouble());
		}
		for(int i = 0; i < nHidden; i++) {
			output.setOutputValue(i, input.nextInt());
		}
		return output;
	}

	Dataset readDataSet(Scanner input) throws AutoencoderException {
		Dataset output = new Dataset();
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

	void classify(Dataset dataset, Network network) throws AutoencoderException {
		for (Sample sample : dataset) {
			double[] inputs = sample.getInput();
			double[] targets = sample.getOutput();
			double[] results = network.update(inputs);

			out.printf("Exptected -> Realized:\t");
			out.printf("%d -> %d\n", getMaxIndex(targets), getMaxIndex(results));
		}
	}

	double errorRate(Dataset dataset, Network network) throws AutoencoderException {
		int correct = 0;
		for (Sample sample : dataset) {
			double[] inputs = sample.getInput();
			double[] targets = sample.getOutput();
			double[] results = network.update(inputs);
			correct += getMaxIndex(targets) == getMaxIndex(results) ? 1 : 0; 

		}
		return (1 - (correct/(double)dataset.size())) * 100;
	}

	void setup() {
		network = new Network(nInput, nHidden, nOutput);
		initialized = true;
	}
	
	void train() throws AutoencoderException {
		if (!initialized) {
			throw new AutoencoderException("Can not train uninitialized network");
		}
		Dataset trainingSet = readDataSet(InputSelector.getInput());
		network.train(trainingSet, iterations, learningRate, momentumFactor);
	}

	void classify() throws AutoencoderException {
		if (!initialized) {
			throw new AutoencoderException("Can not classify with uninitialized network");
		}
		Dataset testSet = readDataSet(InputSelector.getInput());
		classify(testSet, network);
	}
	
	Dataset XORDataset() {
		Dataset dataset = new Dataset();
		Sample sample;
		double[] input, output;
		
		// Add 1 0 -> 1
		sample = new Sample(nInput, nOutput);
		input = new double[nInput];
		input[0] = 1;
		output = new double[nOutput];
		output[0] = 1;
		dataset.add(sample);
		
		// Add 0 1 -> 1
		sample = new Sample(nInput, nOutput);
		input = new double[nInput];
		input[nInput - 1] = 1;
		output = new double[nOutput];
		output[nInput -1 ] = 1;
		dataset.add(sample);
		
		// Add 0 0 -> 0
		sample = new Sample(nInput, nOutput);
		input = new double[nInput];
		output = new double[nOutput];
		dataset.add(sample);
		
		// Add 1 1 -> 0
		sample = new Sample(nInput, nOutput);
		input = new double[nInput];
		input[0] = 1;
		input[nInput - 1] = 1;
		output = new double[nOutput];
		output[nInput -1 ] = 1;
		dataset.add(sample);
		
		return dataset;
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
