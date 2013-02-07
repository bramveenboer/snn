package nnocr;

import java.io.PrintStream;
import java.util.Locale;
import java.util.Scanner;

import ui.UIHulpMethodes;
import nnocr.exception.nnocrException;
import nnocr.geneticalgorithm.GeneticAlgorithm;
import nnocr.multineuralnet.MultiNeuralNet;
import nnocr.neuralnet.NeuralNet;
import nnocr.objects.DataSet;
import nnocr.objects.Sample;
import nnocr.util.InputSelector;

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

	Sample readTrainingObject(Scanner input) throws nnocrException {
		Sample output = new Sample(numberOfInputNodes, numberOfOutputNodes);

		for(int i = 0; i < numberOfInputNodes; i++) {
			output.setInputValue(i, input.nextDouble());
		}
		for(int i = 0; i < numberOfOutputNodes; i++) {
			output.setOutputValue(i, input.nextInt());
		}
		return output;
	}

	DataSet readDataSet(Scanner input) throws nnocrException {
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

	void classify(DataSet dataSet, GeneticAlgorithm ga) throws nnocrException {
		for (Sample sample : dataSet) {
			double[] inputs = sample.getInput();
			double[] targets = sample.getOutput();
			double[] results = ga.update(inputs);

			out.printf("Exptected -> Realized:\t");
			out.printf("%d -> %d\n", getMaxIndex(targets), getMaxIndex(results));
		}
	}

	double errorRate(DataSet dataSet, GeneticAlgorithm ga) throws nnocrException {
		int correct = 0;
		for (Sample sample : dataSet) {
			double[] inputs = sample.getInput();
			double[] targets = sample.getOutput();
			double[] results = ga.update(inputs);
			correct += getMaxIndex(targets) == getMaxIndex(results) ? 1 : 0; 

		}
		return (1 - (correct/(double)dataSet.size())) * 100;
	}

	void setup() throws nnocrException {
		String classifierType;
		try {
			numberOfInputNodes = Integer.parseInt(UIHulpMethodes.vraagGebruikerOmString("Specify the number of Input Nodes:"));
			numberOfHiddenNodes = Integer.parseInt(UIHulpMethodes.vraagGebruikerOmString("Specify the number of Hidden Nodes:"));
			numberOfOutputNodes = Integer.parseInt(UIHulpMethodes.vraagGebruikerOmString("Specify the number of Output Nodes:"));
			classifierType = UIHulpMethodes.vraagGebruikerOmKeuze("What type of classifier should be used?", "Neural Net", "Multi Neural Net");
		} catch (Exception e) {
			throw new nnocrException("Cancel is not allowed here");
		}
		if (classifierType.equals("Neural Net")) {
			ga = new NeuralNet(numberOfInputNodes, numberOfHiddenNodes, numberOfOutputNodes);
		} else if (classifierType.equals("Multi Neural Net")) {
			ga = new MultiNeuralNet(numberOfInputNodes, numberOfHiddenNodes, numberOfOutputNodes);
		} else {
			throw new nnocrException("Invalid option");
		}
		initialized = true;
	}

	void train() throws nnocrException {
		if (!initialized) {
			throw new nnocrException("Can not train uninitialized network");
		}
		iterations = Integer.parseInt(UIHulpMethodes.vraagGebruikerOmString("Specify the number of iterations:"));
		learningRate = Double.parseDouble(UIHulpMethodes.vraagGebruikerOmString("Specify the learning rate to be used:"));
		momentumFactor = Double.parseDouble(UIHulpMethodes.vraagGebruikerOmString("Specify the momentum factor to be used:"));
		DataSet trainingSet = readDataSet(InputSelector.getInput());
		ga.train(trainingSet, iterations, learningRate, momentumFactor);
	}

	void classify() throws nnocrException {
		if (!initialized) {
			throw new nnocrException("Can not classify with uninitialized network");
		}
		DataSet testSet = readDataSet(InputSelector.getInput());
		classify(testSet, ga);
	}

	void visualize() throws nnocrException {
		if (!initialized) {
			throw new nnocrException("Can not visualize uninitialized network");
		}
		ga.draw();
	}

	void action() throws nnocrException {
		String typeAction = UIHulpMethodes.vraagGebruikerOmKeuze("What do you want to do?", "Setup", "Train", "Classify", "Visualize");
		if (typeAction.equals("Setup")) {
			setup();
		} else if (typeAction.equals("Train")) {
			train();
		} else if (typeAction.equals("Classify")) {
			classify();
		} else if (typeAction.equals("Visualize")) {
			visualize();
		} else {
			run = false;
		}
	}

	void start() {
		while (run) {
			try{
				action();
				run = UIHulpMethodes.vraagGebruikerWelOfNiet("Do you want to do something else?");
			} catch (nnocrException e) {
				out.printf("%s\n", e.getMessage());
			}
		}
	}

	public static void main(String[] argv) {
		new Main().start();
	}
}
