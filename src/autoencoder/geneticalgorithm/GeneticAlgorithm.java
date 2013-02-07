package autoencoder.geneticalgorithm;

import autoencoder.exception.AutoencoderException;
import autoencoder.objects.DataSet;

public interface GeneticAlgorithm {
	 double[] update(double[] inputs) throws AutoencoderException;
	 void train(DataSet samples, int iterations, double learningRate, double momentumFactor) throws AutoencoderException;
}
