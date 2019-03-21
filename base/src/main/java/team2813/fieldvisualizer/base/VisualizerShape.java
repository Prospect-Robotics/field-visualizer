package team2813.fieldvisualizer.base;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public abstract class VisualizerShape {
	public Point2D.Double position = new Point2D.Double(0, 0);
	public double angle = 0;

	private static final Color DEFAULT_COLOR = Color.RED;

	/**
	 * <p>color of point</p>
	 * <p>default color will be used if it is null</p>
	 */
	public Color color = null;

	public Shape shape;
}
