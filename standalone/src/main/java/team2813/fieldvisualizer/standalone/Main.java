package team2813.fieldvisualizer.standalone;

import javax.swing.*;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(Main::initGUI);
	}

	private static void initGUI(){
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		VisualizerInterface visualizerInterface = new VisualizerInterface();
		frame.getContentPane().add(visualizerInterface);

		frame.pack();
		frame.setVisible(true);
	}
}