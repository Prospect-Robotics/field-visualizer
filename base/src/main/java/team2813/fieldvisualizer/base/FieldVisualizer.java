package team2813.fieldvisualizer.base;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class FieldVisualizer extends JPanel {

	//region robot position
	/** radians */
	private double robotAngle;

	/** inches */
	private double robotY = 0;
	/** inches */
	private double robotX = 0;

	private double pointX = 0;
	private double pointY = 0;

	/**
	 *
	 * @param x x position in inches
	 * @param y y position in inches
	 */
	public void setRobotPosition(double x, double y){
		robotX = x;
		robotY = y;
	}

	/**
	 *
	 * @param robotX x position in inches
	 */
	public void setRobotX(double robotX) {
		this.robotX = robotX;
	}

	/**
	 *
	 * @param robotY y position in inches
	 */
	public void setRobotY(double robotY) {
		this.robotY = robotY;
	}

	/**
	 *
	 * @param robotAngle angle in radians
	 */
	public void setRobotAngle(double robotAngle) {
		this.robotAngle = robotAngle;
	}

	/**
	 *
	 * @param robotX x position in inches
	 */
	public void setPointX(double robotX) {
		this.pointX = robotX;
	}

	/**
	 *
	 * @param robotY y position in inches
	 */
	public void setPointY(double robotY) {
		this.pointY = robotY;
	}


	public void putPoint(){
		points.add(new Point2D.Double(pointX, pointY));
		System.out.println("point put: ("+pointX+","+pointY+")");
	}

	public void clearPoints(){
		points.clear();
	}

	/**
	 *
	 * @return x position in inches
	 */
	public double getRobotX() {
		return robotX;
	}

	/**
	 *
	 * @return y position in inches
	 */
	public double getRobotY() {
		return robotY;
	}

	/**
	 *
	 * @return angle in radians
	 */
	public double getRobotAngle() {
		return robotAngle;
	}

	/**
	 *
	 * @param distance distance in inches
	 */
	public void moveRobot(double distance){
		robotX += Math.cos(robotAngle) * distance;
		robotY += Math.sin(robotAngle) * distance;
	}

	/**
	 *
	 * @param amount angle in radians
	 */
	public void turnRobot(double amount){
		robotAngle += amount;
		robotAngle %= Math.PI * 2;
		System.out.println(Math.toDegrees(robotAngle));
	}

	//endregion

	//#region offset
	private double offsetX = 0, offsetY = 0;

	public void setOffsetX(double offsetX) {
		this.offsetX = offsetX;
	}

	public void setOffsetY(double offsetY) {
		this.offsetY = offsetY;
	}
	//#endregion


	private double zoomFactor = 2;

	public double getZoomFactor() {
		return zoomFactor;
	}

	public void setZoomFactor(double zoomFactor) {
		this.zoomFactor = zoomFactor;
	}

	private double pointSize = 6;

	public void setPointSize(double doubleValue) {
		pointSize = doubleValue;
	}

	public enum ViewMode {
		VIEW_ENTIRE_FIELD,
		VIEW_ROBOT,
		VIEW_ROBOT_NORTH_UP;
	}

	private ViewMode viewMode = ViewMode.VIEW_ROBOT;

	private BufferedImage fieldImage;


	private double robotWidth = 25, robotLength = 30; //TODO get measurements
	public void setRobotWidth(double robotWidth) {
		this.robotWidth = robotWidth;
	}

	public void setRobotLength(double robotLength) {
		this.robotLength = robotLength;
	}

	private Area robotShape;

	private void updateRobotShape(){
		robotShape = new Area();
		// outline
		robotShape.add(new Area(new Rectangle2D.Double(
				-robotWidth/2, -robotLength/2,
				robotWidth, robotLength
		)));
//		double arrowWidth = 2, arrowLength = robotLength - 10;
//		robotShape.subtract(new Area(
//				new Rectangle2D.Double(arrowWidth / -2, arrowLength / -2, arrowWidth, arrowLength)
//		));
//		robotShape.subtract(new Area(
//				new Line2D.Double()
//		));
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.PI / 2);
		robotShape.transform(transform);
	}

	// image and data from https://github.com/wpilibsuite/PathWeaver
	private static final String fieldImageResourcePath = "2019-field.jpg";
	// numbers from json file from path weaver
	// (field size width / (bottom right x - top left x)) * convert to inches
	private static final double fieldImagePixelToInch = (54d / (1372 - 217)) * 12;

	public FieldVisualizer(){
		updateRobotShape();
		try {
			fieldImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream(fieldImageResourcePath));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setViewMode(ViewMode viewMode) {
		this.viewMode = viewMode;
	}

	public ViewMode getViewMode() {
		return viewMode;
	}

	List<Point2D.Double> points = new ArrayList<>();

	@Override
	public void paint(Graphics g) {
		//FIXME temp
		double robotX = this.robotX + offsetX;
		double robotY = this.robotY + offsetY;

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

		AffineTransform globalTransform = new AffineTransform();
		AffineTransform fieldImageTransform = new AffineTransform();
		AffineTransform robotTransform = new AffineTransform();

		//scale
		double hVal = (double)getHeight() / fieldImage.getHeight();
		double wVal = (double)getWidth() / fieldImage.getWidth();
		double scale;
		if(wVal < hVal) scale = wVal;
		else scale = hVal;
		if(viewMode == ViewMode.VIEW_ROBOT || viewMode == ViewMode.VIEW_ROBOT_NORTH_UP){
			scale*=zoomFactor;
		}
		globalTransform.scale(scale, scale);

		//center image
		if(viewMode == ViewMode.VIEW_ENTIRE_FIELD){
			globalTransform.translate(
					(getWidth() - scale * fieldImage.getWidth()) / scale / 2,
					(getHeight() - scale * fieldImage.getHeight()) / scale / 2
			);
		}

		robotTransform.rotate(robotAngle, robotX, robotY);
		robotTransform.translate(robotX, robotY);
		if(viewMode == ViewMode.VIEW_ROBOT || viewMode == ViewMode.VIEW_ROBOT_NORTH_UP){
			globalTransform.translate(
					(getWidth() / 2d / scale) - robotX,
					(getHeight() / 2d / scale) - robotY
			);
			if(viewMode == ViewMode.VIEW_ROBOT){
				globalTransform.rotate(-robotAngle-Math.PI/2, robotX, robotY);
			}
		}

		g2d.transform(globalTransform);

		g2d.setStroke(new BasicStroke(
				1/(float)fieldImagePixelToInch,
				BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_ROUND
		));

		g2d.drawImage(
				fieldImage,
				fieldImageTransform,
				null
		);

		g2d.setColor(Color.RED);
		g2d.fill(robotTransform.createTransformedShape(robotShape));

		g2d.setColor(Color.CYAN);
		for(Point2D.Double point : points){
			g2d.fill(new Ellipse2D.Double(
					point.x - pointSize/2 + offsetX,
					point.y - pointSize/2 + offsetY,
					pointSize, pointSize
			));
		}

	}
}
