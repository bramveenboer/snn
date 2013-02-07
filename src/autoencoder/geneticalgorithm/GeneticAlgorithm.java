package nnocr.geneticalgorithm;

import nnocr.exception.nnocrException;
import nnocr.objects.DataSet;

public interface GeneticAlgorithm {
	 double[] update(double[] inputs) throws nnocrException;
	 void train(DataSet samples, int iterations, double learningRate, double momentumFactor) throws nnocrException;
	 void draw();
}
