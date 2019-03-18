package team2813.fieldvisualizer.smartdashboard.widgets;

import edu.wpi.first.networktables.*;
import edu.wpi.first.smartdashboard.gui.StaticWidget;
import edu.wpi.first.smartdashboard.gui.elements.bindings.AbstractTableWidget;
import edu.wpi.first.smartdashboard.properties.NumberProperty;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import edu.wpi.first.smartdashboard.properties.MultiProperty;
import edu.wpi.first.wpilibj.tables.ITable;
import team2813.fieldvisualizer.base.FieldVisualizer;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class FieldVisualizerWidget extends StaticWidget implements ComponentListener {
//	public static final DataType[] TYPES = {FieldVisualizerType.get()};

	public final NumberProperty scale = new NumberProperty(this, "Scale");
	public final MultiProperty viewMode = new MultiProperty(this, "View Mode");

	public final FieldVisualizer visualizer;

	NetworkTable points;

	public FieldVisualizerWidget(){
		super();
		visualizer = new FieldVisualizer();
	}

	@Override
	public void init() {
		for(FieldVisualizer.ViewMode mode : FieldVisualizer.ViewMode.values()){
			viewMode.add(mode.toString(), mode);
		}
		viewMode.setDefault(FieldVisualizer.ViewMode.VIEW_ROBOT);
		visualizer.setViewMode((FieldVisualizer.ViewMode) viewMode.getValue());

		scale.setDefault(2d);
		visualizer.setZoomFactor(scale.getValue().doubleValue());

		setLayout(new BorderLayout());

		add(visualizer, BorderLayout.CENTER);
		setBackground(Color.RED);
		addComponentListener(this);

		//#region network tables
		NetworkTable visualizerTable = NetworkTableInstance.getDefault().getTable("visualizer");
		points = visualizerTable.getSubTable("points");
		NetworkTable robot = points.getSubTable("robot");
		robot.addEntryListener("x", (networkTable, s, networkTableEntry, networkTableValue, i) -> {
			visualizer.setRobotX(networkTableValue.getDouble());
			revalidate();
			repaint();
		}, EntryListenerFlags.kUpdate);
		robot.addEntryListener("y", (networkTable, s, networkTableEntry, networkTableValue, i) -> {
			visualizer.setRobotY(networkTableValue.getDouble());
			revalidate();
			repaint();
		}, EntryListenerFlags.kUpdate);
		robot.addEntryListener("theta", (networkTable, s, networkTableEntry, networkTableValue, i) -> {
			visualizer.setRobotAngle(networkTableValue.getDouble());
			revalidate();
			repaint();
		}, EntryListenerFlags.kUpdate);
		//#endregion

		revalidate();
		repaint();
	}

//	public void robotEntryListener(NetworkTable var1, String var2, NetworkTableEntry var3, NetworkTableValue value, int flags){
//
//	}

	@Override
	public void propertyChanged(Property property) {
		if(property == viewMode){
			visualizer.setViewMode((FieldVisualizer.ViewMode) viewMode.getValue());
		}
		else if(property == scale){
			visualizer.setZoomFactor(scale.getValue().doubleValue());
		}
		revalidate();
		repaint();
	}

	@Override
	public void componentResized(ComponentEvent e) {
		visualizer.setSize(getSize());
	}

	@Override
	public void componentMoved(ComponentEvent e) {

	}

	@Override
	public void componentShown(ComponentEvent e) {

	}

	@Override
	public void componentHidden(ComponentEvent e) {

	}
}
