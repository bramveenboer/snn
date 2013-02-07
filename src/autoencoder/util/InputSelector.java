package autoencoder.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class InputSelector extends JFrame {
	protected static final long serialVersionUID = 1L;

	protected static final File DEFAULT_PATH = new File("resource");
	protected static final boolean MULTIPLE_INPUTS = false;

	private static File showDialog() 	{
		JFileChooser chooser = new JFileChooser(DEFAULT_PATH);
		chooser.setMultiSelectionEnabled(MULTIPLE_INPUTS);

		int option = chooser.showOpenDialog(new InputSelector());
		if (option == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		} else {
			return null;
		}
	}

	public static void setInput() 	{
		File input = showDialog();
		if (input != null) {
			try {
				System.setIn(new FileInputStream(input));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public static Scanner getInput() {
		File input = showDialog();
		if (input != null) {
			try {
				return new Scanner(input);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return new Scanner(System.in);
		}
	}

	public static BufferedImage getImage() {
		File input = showDialog();
		if (input != null) {
			try {
				return ImageIO.read(input);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
		//return new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
	}
}