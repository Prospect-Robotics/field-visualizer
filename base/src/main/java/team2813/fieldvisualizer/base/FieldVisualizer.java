package team2813.fieldvisualizer.base;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
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

	public void moveRobot(double distance){
		robotX += Math.cos(robotAngle) * distance;
		robotY += Math.sin(robotAngle) * distance;
	}

	public void turnRobot(double amount){
		robotAngle += amount;
	}

	//endregion

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


		//scale
		double hVal = (double)getHeight() / fieldImage.getHeight();
		double wVal = (double)getWidth() / fieldImage.getWidth();
		double scale;
		if(wVal < hVal) scale = wVal;
		else scale = hVal;
		g2d.scale(scale, scale);

		//center image
		if(viewMode == ViewMode.VIEW_ENTIRE_FIELD){
			g2d.translate(
					(getWidth() - scale * fieldImage.getWidth()) / scale / 2,
					(getHeight() - scale * fieldImage.getHeight()) / scale / 2
			);
		}

//		if(viewMode == ViewMode.VIEW_ROBOT || viewMode == ViewMode.VIEW_ROBOT_NORTH_UP){
//
//
//			g2d.translate(
//					fieldImage.getWidth()*scale-robotX,
//					fieldImage.getHeight()*scale-robotY
//			);
//
//			if(viewMode == ViewMode.VIEW_ROBOT){
//				g2d.rotate(robotAngle);
//			}
//		}

		if(viewMode == ViewMode.VIEW_ROBOT || viewMode == ViewMode.VIEW_ROBOT_NORTH_UP){
			g2d.translate(robotX, robotY);
		}
		if(viewMode == ViewMode.VIEW_ROBOT){
			g2d.rotate(-robotAngle, robotX, robotY);
		}

		// field size to irl size (inches)
		g2d.scale(fieldImagePixelToInch,fieldImagePixelToInch);
		g2d.setStroke(new BasicStroke(
				1/(float)fieldImagePixelToInch,
				BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_ROUND
		));

		AffineTransform fieldTransform = new AffineTransform();
		fieldTransform.scale(1/fieldImagePixelToInch, 1/fieldImagePixelToInch);

		g2d.drawImage(
				fieldImage,
				fieldTransform,
				null
		);

//		g2d.setColor(Color.MAGENTA);
//		g2d.drawLine(0,0,1,1);

//
		g2d.setColor(Color.RED);
//
		g2d.draw(new Line2D.Double(
				robotX,
				robotY,
				robotX + Math.cos(robotAngle) * 12*4,
				robotY + Math.sin(robotAngle) * 12*4
		));
	}
}
