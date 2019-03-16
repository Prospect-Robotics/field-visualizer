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
		viewModeComboBox.addItemListener(e -> {
			if(e.getStateChange() == ItemEvent.SELECTED){
				visualization.setViewMode((FieldVisualizer.ViewMode) e.getItem());
			}
		});
		JPanel temp = new JPanel();
		temp.add(viewModeComboBox);
		leftMenu.add(temp);
		viewModeComboBox.setSelectedIndex(1);
		viewModeComboBox.setSelectedIndex(0);
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
				visualization.moveRobot(10);
				break;
			case(KeyEvent.VK_S):
				visualization.moveRobot(-1);
				break;
			case(KeyEvent.VK_A):
				visualization.turnRobot(Math.toRadians(1));
				break;
			case(KeyEvent.VK_D):
				visualization.turnRobot(Math.toRadians(-1));
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