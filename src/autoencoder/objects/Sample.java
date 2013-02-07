package autoencoder.objects;

import autoencoder.exception.AutoencoderException;

public class Sample {
	
	protected double[] input,
					   output;
	protected int nInput,
	              nOutput;
	
	public Sample(int numberOfInputNodes, int numberOfOutputNodes) {
		this.input = new double[numberOfInputNodes];
		this.output = new double[numberOfOutputNodes];
		this.nInput = numberOfInputNodes;
		this.nOutput = numberOfOutputNodes;
	}
	
	public double[] getInput() {
		return input.clone();
	}
	
	public double[] getOutput() {
		return output.clone();
	}
	
	public void setInput(double[] values) throws AutoencoderException {
		if (values.length == nInput) {
			input = values;
		} else {
			throw new AutoencoderException("Invalid argument length");
		}
	}
	
	public void setOutput(double[] values) throws AutoencoderException {
		if (values.length == nInput) {
			output = values;
		} else {
			throw new AutoencoderException("Invalid argument length");
		}
	}
	
	public void setInputValue(int index, double value) throws AutoencoderException {
		if (0 > index || index < nInput) {
			input[index] = value;
		} else {
			throw new AutoencoderException("Index out of bounds");
		}
	}
	
	public void setOutputValue(int index, double value) throws AutoencoderException {
		if (0 > index || index < nOutput) {
			output[index] = value;
		} else {
			throw new AutoencoderException("Index out of bounds");
		}
	}

	/* adjusts resolution of the samples by factor 'rate'. Not used. 
	 * might be useful for data with a very large number of input nodes,
	 * less calculations are needed to train or update the network, but
	 * precision might be compromised */
	public void downSample(int rate) {
		double[] input = new double[this.nInput / rate];
		for (int i = 0; i < this.nInput; i += rate) {
			double value = 0;
			for (int j = 0; j < rate; j++) {
				value += input[i+j] / rate;
			}
			input[i / rate] = value;
		}
		this.input = input;
		this.nInput /= rate;
	}
}
