package autoencoder.util;

import java.awt.image.BufferedImage;
import java.io.PrintStream;

public class ImagesToDataSet {

	PrintStream out;

	ImagesToDataSet() {
		out = new PrintStream(System.out);
	}

	double[] readImage() {
		double[] output = new double[256];		
		BufferedImage image = InputSelector.getImage();
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {			
                int color = image.getRGB(i, j); 
                int red   = (color & 0x00ff0000) >> 16;
                int green = (color & 0x0000ff00) >> 8;
                int blue  = (color & 0x000000ff) >> 0;
                //System.out.println(color + " " + red + " " + green + " " + blue);
                output[i + j] = (red + green + blue) / (3 * 255);
			}
		}
		return output;
	}

	void start() {
		for (int i = 0; i < 10; i++) {
			double[] input = readImage();
			int[] output = new int[10];
			output[i] = 1;
			for (int j = 0; j < 256; j++) {
				out.printf("%f ", input[j]);
			}
			for (int j = 0; j < 10; j++) {
				out.printf("%d ", output[j]);
			}
			out.println();
		}
	}

	public static void main(String[] argv) {
		new ImagesToDataSet().start();
	}
}
