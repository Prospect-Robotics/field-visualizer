package team2813.fieldvisualizer.smartdashboard;

import edu.wpi.first.networktables.NetworkTable;

import java.awt.geom.Ellipse2D;

public class VisualizerTablePoint extends VisualizerTableShape {
	public VisualizerTablePoint(NetworkTable myTable){
		super(myTable);
		shape = new Ellipse2D.Double(-6, -6, 6, 6);
	}
}
