package team2813.fieldvisualizer.smartdashboard.widgets;

import edu.wpi.first.networktables.*;
import edu.wpi.first.smartdashboard.gui.StaticWidget;
import edu.wpi.first.smartdashboard.properties.BooleanProperty;
import edu.wpi.first.smartdashboard.properties.NumberProperty;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.properties.MultiProperty;
import team2813.fieldvisualizer.base.FieldVisualizer;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class FieldVisualizerWidget extends StaticWidget implements ComponentListener {

	public final NumberProperty scale = new NumberProperty(this, "Scale");
	public final NumberProperty pointSize = new NumberProperty(this, "Point Radius (inches)");
	public final MultiProperty viewMode = new MultiProperty(this, "View Mode");
	public final BooleanProperty clearPoints = new BooleanProperty(this, "Clear Points");

	public final NumberProperty offsetX = new NumberProperty(this, "Offset X");
	public final NumberProperty offsetY = new NumberProperty(this, "Offset Y");

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
		pointSize.setDefault(6d);
		visualizer.setPointSize(pointSize.getValue().doubleValue());


		clearPoints.setDefault(false);

		setLayout(new BorderLayout());

		add(visualizer, BorderLayout.CENTER);
		setBackground(Color.RED);
		addComponentListener(this);

		//#region network tables
		NetworkTable visualizerTable = NetworkTableInstance.getDefault().getTable("visualizer");
		points = visualizerTable.getSubTable("points");
		points.addEntryListener(this::pointListener, EntryListenerFlags.kUpdate | EntryListenerFlags.kImmediate);
		NetworkTable robotTable = visualizerTable.getSubTable("robot");
		robotTable.addEntryListener(
				this::robotPositionListener,
				EntryListenerFlags.kUpdate
				//| EntryListenerFlags.kNew | EntryListenerFlags.kDelete
		);
		//#endregion

		offsetX.setDefault(0d);
		offsetY.setDefault(0d);

		revalidate();
		repaint();
	}

	private void pointListener(NetworkTable table, String key, NetworkTableEntry entry, NetworkTableValue value, int flags){
		if(key.equals("x")) visualizer.setPointX(value.getDouble());
		else if(key.equals("y")) visualizer.setPointY(value.getDouble());
		else if(key.equals("update")) {
			visualizer.putPoint();
			revalidate();
			repaint();
		}
	}

	private void robotPositionListener(NetworkTable table, String key, NetworkTableEntry entry, NetworkTableValue value, int flags){
		if(key.equals("x")) visualizer.setRobotX(value.getDouble());
		else if(key.equals("y")) visualizer.setRobotY(value.getDouble());
		else if(key.equals("angle")) visualizer.setRobotAngle(value.getDouble());
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
		else if(property == clearPoints){
			if(clearPoints.getValue()){
				visualizer.clearPoints();
				clearPoints.setValue(false);
			}
		}
		else if(property == pointSize){
			visualizer.setPointSize(pointSize.getValue().doubleValue());
		}
		else if(property == offsetX){
			visualizer.setOffsetX(offsetX.getValue().doubleValue());
		}
		else if(property == offsetY){
			visualizer.setOffsetY(offsetY.getValue().doubleValue());
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
