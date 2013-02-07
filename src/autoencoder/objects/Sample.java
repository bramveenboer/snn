package autoencoder.objects;

import autoencoder.exception.AutoencoderException;

public class Sample {
	
	protected double[] input,
					   output;
	protected int numberOfInputNodes,
	              numberOfOutputNodes;
	
	public Sample(int numberOfInputNodes, int numberOfOutputNodes) {
		this.input = new double[numberOfInputNodes];
		this.output = new double[numberOfOutputNodes];
		this.numberOfInputNodes = numberOfInputNodes;
		this.numberOfOutputNodes = numberOfOutputNodes;
	}
	
	public double[] getInput() {
		return input.clone();
	}
	
	public double[] getOutput() {
		return output.clone();
	}
	
	public void setInputValue(int index, double value) throws AutoencoderException {
		if (0 > index || index < numberOfInputNodes) {
			input[index] = value;
		} else {
			throw new AutoencoderException("Can not set input value, index out of bounds");
		}
	}
	
	public void setOutputValue(int index, double value) throws AutoencoderException {
		if (0 > index || index < numberOfOutputNodes) {
			output[index] = value;
		} else {
			throw new AutoencoderException("Can not set output value, index out of bounds");
		}
	}

	/* adjusts resolution of the samples by factor 'rate'. Not used. 
	 * might be useful for data with a very large number of input nodes,
	 * less calculations are needed to train or update the network, but
	 * precision might be compromised */
	public void downSample(int rate) {
		double[] input = new double[this.numberOfInputNodes / rate];
		for (int i = 0; i < this.numberOfInputNodes; i += rate) {
			double value = 0;
			for (int j = 0; j < rate; j++) {
				value += input[i+j] / rate;
			}
			input[i / rate] = value;
		}
		this.input = input;
		this.numberOfInputNodes /= rate;
	}
}
