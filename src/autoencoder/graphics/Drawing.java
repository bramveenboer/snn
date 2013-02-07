package nnocr.graphics;

import ui.DiagramTekenUserInterface;
import ui.Kleur;
import ui.UserInterfaceFactory;
import nnocr.neuralnet.NeuralNet;

public class Drawing {
	
	protected static final int UI_WIDTH = 800;
	protected static final int UI_HEIGHT = 400;
	
	protected static final double UPPER_SIGNIFICANCE_LEVEL = 0.3;
	protected static final double LOWER_SIGNIFICANCE_LEVEL = -0.3;
	
	public static void draw(NeuralNet net) {
		DiagramTekenUserInterface ui = UserInterfaceFactory.geefDiagramUI(UI_WIDTH, UI_HEIGHT);
		Kleur black = new Kleur(0,0,0);
		
		drawTitle(ui, net, black);
		drawHiddenNodes(ui, net, black);
		drawOutputNodes(ui, net, black);
		drawInput(ui, net, black);
		drawEdges(ui, net);
	}
	
	protected static void drawTitle(DiagramTekenUserInterface ui, NeuralNet net, Kleur colour){
		String title = "Neural Net";
		int x = (ui.geefBreedte()/2) - (ui.geefBreedteTekst(title)/2);
		int y = ((ui.geefHoogte() / (net.getNumberOfHiddenNodes() + 1))/2) - (ui.geefHoogteTekst(title)/2);
		ui.tekenTekst(x, y, title, colour);
		ui.toon();
	}

	protected static void drawInput(DiagramTekenUserInterface ui, NeuralNet net, Kleur colour) {
		int x = ui.geefBreedte()/15;
		int y = ui.geefHoogte()/5;
		int axisLength = 3 * (UI_HEIGHT/5);
		
		drawSquare(ui, x, y, axisLength, colour);
		drawInputDots(ui, net, colour);
	}
	
	protected static void drawHiddenNodes(DiagramTekenUserInterface ui, NeuralNet net, Kleur colour) {
		int x = ui.geefBreedte() / 2;
		int y = ui.geefHoogte() / (net.getNumberOfHiddenNodes() + 1);
		for(int i = 0; i < net.getNumberOfHiddenNodes(); i++){
			ui.tekenCirkel(x, y, 10, 10, colour, true);
			y += (ui.geefHoogte() / (net.getNumberOfHiddenNodes() + 1));
		}
		ui.toon();
	}
	
	protected static void drawOutputNodes(DiagramTekenUserInterface ui, NeuralNet net, Kleur colour) {
		int x = ui.geefBreedte() - (ui.geefBreedte() / 6);
		int y = ui.geefHoogte() / (net.getNumberOfOutputNodes() + 1);
		for(int i = 0; i < net.getNumberOfOutputNodes(); i++){
			ui.tekenCirkel(x, y, 10, 10, colour, true);
			y += (ui.geefHoogte() / (net.getNumberOfOutputNodes() + 1));
		}
		ui.toon();
	}
	
	protected static void drawEdges(DiagramTekenUserInterface ui, NeuralNet net){
		drawInputToHiddenEdges(ui, net);
		drawHiddenToOutputEdges(ui, net);
	}
	
	protected static void drawSquare(DiagramTekenUserInterface ui, int x, int y, int axisLength, Kleur colour) {
		ui.tekenLijn(x, y, x+axisLength, y, colour);
		ui.tekenLijn(x+axisLength, y, x+axisLength, y+axisLength, colour);
		ui.tekenLijn(x+axisLength, y+axisLength, x, y+axisLength, colour);
		ui.tekenLijn(x, y+axisLength, x, y, colour);
		ui.toon();
	}
	
	protected static void drawInputDots(DiagramTekenUserInterface ui, NeuralNet net, Kleur colour){
		int xBegin = ui.geefBreedte()/15;
		int xEnd = xBegin + (3 * (UI_HEIGHT/5));
		int width = xEnd - xBegin;
		int interDotSpaceX = (int) (width / (Math.sqrt(net.getNumberOfInputNodes()) + 1));
		
		int yBegin = ui.geefHoogte()/5;
		int yEnd = yBegin + (3 * (UI_HEIGHT/5));
		int height = yEnd - yBegin;
		int interDotSpaceY = (int) (height / (Math.sqrt(net.getNumberOfInputNodes()) + 1));
		
		int y = yBegin;
		for(int i = 0; i < Math.sqrt(net.getNumberOfInputNodes()); i++){
			int x = xBegin;
			y += interDotSpaceY;
			for(int j = 0; j < Math.sqrt(net.getNumberOfInputNodes()); j++){
				x += interDotSpaceX;
				ui.tekenCirkel(x, y, 5, 5, colour, true);				
			}			
		}
		ui.toon();
	}
	
	protected static void drawInputToHiddenEdges(DiagramTekenUserInterface ui, NeuralNet net){
		int xBegin = ui.geefBreedte()/15;
		int xEnd = xBegin + (3 * (UI_HEIGHT/5));
		int width = xEnd - xBegin;
		int interDotSpaceX = (int) (width / (Math.sqrt(net.getNumberOfInputNodes()) + 1));
		
		int yBegin = ui.geefHoogte()/5;
		int yEnd = yBegin + (3 * (UI_HEIGHT/5));
		int height = yEnd - yBegin;
		int interDotSpaceY = (int) (height / (Math.sqrt(net.getNumberOfInputNodes()) + 1));
		
		int y = yBegin;
		for(int i = 0; i < Math.sqrt(net.getNumberOfInputNodes()); i++){
			int x = xBegin;
			y += interDotSpaceY;
			for(int j = 0; j < Math.sqrt(net.getNumberOfInputNodes()); j++){
				x += interDotSpaceX;
				int inputNumber = ((int)Math.sqrt(net.getNumberOfInputNodes())*i) + j;
				drawEdgesToHidden(ui, net, x, y, inputNumber);
			}			
		}
	}
	
	protected static void drawHiddenToOutputEdges(DiagramTekenUserInterface ui, NeuralNet net){
		int x = ui.geefBreedte() / 2;
		int y = ui.geefHoogte() / (net.getNumberOfHiddenNodes() + 1);
		for(int i = 0; i < net.getNumberOfHiddenNodes(); i++){
			drawEdgesToOutput(ui, net, x, y, i);
			y += (ui.geefHoogte() / (net.getNumberOfHiddenNodes() + 1));
		}
		ui.toon();
	}
	
	protected static void drawEdgesToHidden(DiagramTekenUserInterface ui, NeuralNet net, int xBegin, int yBegin, int inputNumber){
		int xEnd = ui.geefBreedte() / 2;
		int yEnd = ui.geefHoogte() / (net.getNumberOfHiddenNodes() + 1);
		for(int i = 0; i < net.getNumberOfHiddenNodes(); i++){
			double weight = net.getInputWeight(inputNumber, i);
			if(significant(weight)){
				Kleur colour = determineColour(net.getInputWeight(inputNumber, i));
				ui.tekenLijn(xBegin, yBegin, xEnd, yEnd, colour);
			}
			yEnd += (ui.geefHoogte() / (net.getNumberOfHiddenNodes() + 1));
		}
		ui.toon();
	}
	
	protected static void drawEdgesToOutput(DiagramTekenUserInterface ui, NeuralNet net, int xBegin, int yBegin, int hiddenNumber){
		int xEnd = ui.geefBreedte() - (ui.geefBreedte() / 6);
		int yEnd = ui.geefHoogte() / (net.getNumberOfOutputNodes() + 1);
		for(int i = 0; i < net.getNumberOfOutputNodes(); i++){
			double weight = net.getOutputWeight(hiddenNumber, i);
			if(significant(weight)){
				Kleur colour = determineColour(weight);
				ui.tekenLijn(xBegin, yBegin, xEnd, yEnd, colour);
			}
			yEnd += (ui.geefHoogte() / (net.getNumberOfOutputNodes() + 1));
		}
		ui.toon();
	}
	
	protected static boolean significant(double weight){
		return (weight <= LOWER_SIGNIFICANCE_LEVEL || weight >= UPPER_SIGNIFICANCE_LEVEL);
	}
	
	protected static Kleur determineColour(double weight){
		if(weight <= LOWER_SIGNIFICANCE_LEVEL){
			return new Kleur(255,0,0);
		} else if (weight >= UPPER_SIGNIFICANCE_LEVEL){
			return new Kleur(0,255,0);
		} else {
			return new Kleur(0,0,0);
		}
	}
}
