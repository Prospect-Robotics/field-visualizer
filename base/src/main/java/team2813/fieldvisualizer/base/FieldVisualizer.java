package team2813.fieldvisualizer.base;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;

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
		System.out.println(Math.toDegrees(robotAngle));
	}

	//endregion

	private double zoomFactor = 2;

	public double getZoomFactor() {
		return zoomFactor;
	}

	public void setZoomFactor(double zoomFactor) {
		this.zoomFactor = zoomFactor;
	}

	public enum ViewMode {
		VIEW_ENTIRE_FIELD,
		VIEW_ROBOT,
		VIEW_ROBOT_NORTH_UP;
	}

	private ViewMode viewMode = ViewMode.VIEW_ROBOT;

	private BufferedImage fieldImage;

	// image and data from https://github.com/wpilibsuite/PathWeaver
	private static final String fieldImageResourcePath = "2019-field.jpg";
	// numbers from json file from path weaver
	// (field size width / (bottom right x - top left x)) * convert to inches
	private static final double fieldImagePixelToInch = (54d / (1372 - 217)) * 12;

	public FieldVisualizer(){
		try {
			fieldImage = ImageIO.read(FieldVisualizer.class.getClassLoader().getResourceAsStream(fieldImageResourcePath));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setViewMode(ViewMode viewMode) {
		System.out.println(viewMode);
		this.viewMode = viewMode;
	}

	public ViewMode getViewMode() {
		return viewMode;
	}

	@Override
	public void paint(Graphics g) {
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

//		if(viewMode == ViewMode.VIEW_ROBOT){
//			g2d.rotate(-robotAngle, robotX, robotY);
//		}

//
//		robotTransform.translate(robotX, robotY);
//		robotTransform.rotate(robotAngle);
//
//		if(viewMode == ViewMode.VIEW_ROBOT || viewMode == ViewMode.VIEW_ROBOT_NORTH_UP){
//
//		}
//
//		if(viewMode == ViewMode.VIEW_ROBOT){
//			fieldImageTransform.translate(robotX, robotY);
//			fieldImageTransform.rotate(robotAngle);
//		}


		robotTransform.rotate(robotAngle, robotX, robotY);
		robotTransform.translate(robotX, robotY);
		if(viewMode == ViewMode.VIEW_ROBOT){
//			globalTransform.rotate(-robotAngle, robotX, robotY);
		}
		if(viewMode == ViewMode.VIEW_ROBOT || viewMode == ViewMode.VIEW_ROBOT_NORTH_UP){
//			globalTransform.translate(robotX, robotY);
			globalTransform.translate(
					(getWidth() / 2 / scale) - robotX,
					(getHeight() / 2 / scale) - robotY
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

//		fieldImageTransform.scale(1/fieldImagePixelToInch, 1/fieldImagePixelToInch);

		g2d.drawImage(
				fieldImage,
				fieldImageTransform,
				null
		);

//		g2d.setColor(Color.MAGENTA);
//		g2d.drawLine(0,0,1,1);

//
		g2d.setColor(Color.RED);
//
//		g2d.draw(robotTransform.createTransformedShape(
//				new Rectangle2D.Double(
//						-100, -100, 100, 100
//				)
//		));

		g2d.draw(new Line2D.Double(
				robotX, robotY,
				robotX + Math.cos(robotAngle) * 100,
				robotY + Math.sin(robotAngle) * 100
		));

		g2d.setColor(Color.GREEN);
		g2d.draw(robotTransform.createTransformedShape(
				new Line2D.Double(
						0, 0, 100, 0
				)
		));
	}
}
