package nnocr.multineuralnet;

import java.util.concurrent.Semaphore;

import nnocr.exception.nnocrException;
import nnocr.neuralnet.NeuralNet;
import nnocr.objects.DataSet;
import nnocr.objects.Sample;

public class NeuralClassifier extends NeuralNet implements Runnable {	

	/* Training */
	protected Training training;
	protected double[] target;
	protected Semaphore semaphore;
	
	public NeuralClassifier(int numberOfInputNodes, int numberOfHiddenNodes, int numberOfOutputNodes, double[] target) {
		super(numberOfInputNodes, numberOfHiddenNodes, 1);
		this.target = target;
	}
		
	/* Train */
	public void train(Training training, Semaphore semaphore) {
		this.training = training;
		this.semaphore = semaphore;
		new Thread(this).start();
	}
		
	public void train(DataSet samples, int iterations, double learningRate, double momentumFactor) throws nnocrException {
		for (int i = 0; i < iterations; i++) {
			double error = 0;
			for (Sample sample : samples) {
				double[] inputs = sample.getInput();
				update(inputs);
				double[] target = sample.getOutput();
				if (match(this.target, target)) {
					target = new double[1];
					target[0] = 1;
				} else {
					target = new double[1];
					target[0] = 0;
				}
				error += backPropagate(target, learningRate, momentumFactor);
			}
			/*if (i % 10 == 0) {
				System.out.printf("Error: %f\n", error);
			}*/
		}
	}
	
	public void run() {
		try {
			semaphore.acquire();
			train(training.samples, training.iterations, training.learningRate, training.momentumFactor);
			semaphore.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (nnocrException e) {
			System.err.println(e.getMessage());
		}
	}
	
	protected boolean match(double[] a, double[] b) {
		for (int i = 0; i < a.length; i++) {
			if (a[i] != b[i]) {
				return false;
			}
		}
		return true;
	}
}