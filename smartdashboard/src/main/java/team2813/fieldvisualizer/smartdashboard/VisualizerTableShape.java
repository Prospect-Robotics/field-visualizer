package team2813.fieldvisualizer.smartdashboard;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableValue;
import team2813.fieldvisualizer.base.VisualizerShape;

import java.awt.*;

public abstract class VisualizerTableShape extends VisualizerShape {
	public VisualizerTableShape(NetworkTable myTable){
		myTable.addEntryListener(this::entryListener, EntryListenerFlags.kUpdate);
	}

	public void entryListener(NetworkTable table, String key, NetworkTableEntry entry, NetworkTableValue value, int flags){
		switch(key){
			case "x":
				position.x = value.getDouble();
				break;
			case "y":
				position.y = value.getDouble();
				break;
			case "angle":
				angle = value.getDouble();
				break;
		}
	}
}
