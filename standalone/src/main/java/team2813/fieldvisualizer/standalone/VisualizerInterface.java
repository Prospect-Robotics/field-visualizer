package team2813.fieldvisualizer.standalone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import team2813.fieldvisualizer.base.FieldVisualizer;

public class VisualizerInterface extends JPanel implements KeyListener {

	private JPanel leftMenu, rightMenu, header, footer;

	private FieldVisualizer visualization;

	public VisualizerInterface(){
		setLayout(new BorderLayout());

		leftMenu = new JPanel();
		rightMenu = new JPanel();
		header = new JPanel();
		footer = new JPanel();
		visualization = new FieldVisualizer();


		//region left menu setup
		leftMenu.setBackground(Color.RED);
		leftMenu.setLayout(new BoxLayout(leftMenu, BoxLayout.Y_AXIS));
		add(
				BorderLayout.LINE_START,
				new JScrollPane(
						leftMenu,
						ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
						ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
				)
		);

		JComboBox<FieldVisualizer.ViewMode> viewModeComboBox = new JComboBox<>(FieldVisualizer.ViewMode.values());
		viewModeComboBox.setToolTipText("View Mode");
		viewModeComboBox.addItemListener(e -> {
			if(e.getStateChange() == ItemEvent.SELECTED){
				visualization.setViewMode((FieldVisualizer.ViewMode) e.getItem());
				requestFocus();
				repaint();
			}
		});
		viewModeComboBox.setSelectedIndex(1);
		viewModeComboBox.setSelectedIndex(0);
		JPanel temp = new JPanel();
		temp.add(viewModeComboBox);
		leftMenu.add(temp);

		JSpinner zoomFactorSpinner = new JSpinner(new SpinnerNumberModel(
				visualization.getZoomFactor(),
				.2,
				1e4,
				.2
		));
		zoomFactorSpinner.addChangeListener(e -> {
			visualization.setZoomFactor((double)zoomFactorSpinner.getValue());
			requestFocus();
			repaint();
		});
		zoomFactorSpinner.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {
				switch(e.getExtendedKeyCode()){
					case KeyEvent.VK_ESCAPE:
					case KeyEvent.VK_ENTER:
						requestFocus();
						break;
					case KeyEvent.VK_UP:
						zoomFactorSpinner.setValue(zoomFactorSpinner.getNextValue());
						break;
					case KeyEvent.VK_DOWN:
						zoomFactorSpinner.setValue(zoomFactorSpinner.getPreviousValue());
						break;
				}
			}
			@Override
			public void keyReleased(KeyEvent e) { }
		});
		leftMenu.add(zoomFactorSpinner);
		//endregion

		//region right menu setup
		rightMenu.setBackground(Color.GREEN);
		rightMenu.setLayout(new BoxLayout(rightMenu, BoxLayout.Y_AXIS));
		add(
				BorderLayout.LINE_END,
				new JScrollPane(
						rightMenu,
						ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
						ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
				)
		);
		//endregion

		//region header setup
		header.setBackground(Color.MAGENTA);
		add(BorderLayout.PAGE_START, header);
		//endregion

		//region footer setup
		footer.setBackground(Color.YELLOW);
		add(BorderLayout.PAGE_END, footer);
		//endregion

		//region visualization setup
		add(BorderLayout.CENTER, visualization);
		//endregion

		setPreferredSize(new Dimension(400, 400));

		//region event listeners
		setFocusable(true);
		addKeyListener(this);
		//endregion
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("key pressed" + e.getKeyCode());
		switch(e.getExtendedKeyCode()){
			case(KeyEvent.VK_W):
				visualization.moveRobot(1e1);
				break;
			case(KeyEvent.VK_S):
				visualization.moveRobot(-.75e1);
				break;
			case(KeyEvent.VK_A):
				visualization.turnRobot(Math.toRadians(-5));
				break;
			case(KeyEvent.VK_D):
				visualization.turnRobot(Math.toRadians(5));
				break;
			case(KeyEvent.VK_Q):
				visualization.turnRobot(Math.toRadians(-5));
				visualization.moveRobot(1e1);
				break;
			case(KeyEvent.VK_E):
				visualization.moveRobot(1e1);
				visualization.turnRobot(Math.toRadians(5));
				break;
			default:
				return;
		}
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}