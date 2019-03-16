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

	/** in radians */
	private double robotAngle;

	private double robotX = 0, robotY = 0;

	public enum ViewMode {
		VIEW_ENTIRE_FIELD,
		VIEW_ROBOT,
		VIEW_ROBOT_NORTH_UP;
	}

	public void moveRobot(double distance){
		robotX += Math.cos(robotAngle) * distance;
		robotY += Math.sin(robotAngle) * distance;
	}

	public void turnRobot(double amount){
		robotAngle += amount;
	}

	private ViewMode viewMode = ViewMode.VIEW_ROBOT;

	private BufferedImage fieldImage;

	public FieldVisualizer(){
		try {
			fieldImage = ImageIO.read(FieldVisualizer.class.getClassLoader().getResourceAsStream("2019-field.jpg"));
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

		if(viewMode == ViewMode.VIEW_ROBOT || viewMode == ViewMode.VIEW_ROBOT_NORTH_UP){
			g2d.translate(
					fieldImage.getWidth()*scale-robotX,
					fieldImage.getHeight()*scale-robotY
			);
		}

		g2d.drawImage(fieldImage, 0, 0, null);



		g2d.setColor(Color.RED);

		g2d.draw(new Line2D.Double(
				robotX,
				robotY,
				robotX + Math.cos(robotAngle) * 20,
				robotY + Math.sin(robotAngle) * 20
		));
	}
}
