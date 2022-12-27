package frc.robot.extras;

import edu.wpi.first.wpilibj.XboxController;

import java.util.ArrayList;
import java.util.List;

public class XboxPlusPOV extends XboxController {
	List<Integer> povRecords = new ArrayList<>();
	int[] clear = {};

	public XboxPlusPOV(int port) {
		super(port);
	}

	public void process(int povReading) {
		povRecords.add(povReading);
	}

	public boolean getPOVPressed(POV direction) {
		boolean found = false;
		for (int i = 0; i < povRecords.size() && !found; i++) {
			if (povRecords.get(i) == direction.value && povRecords.get(i - 1) != direction.value) {
				found = true;
				povRecords.clear();
			} else {
			}
		}
		return found;
	}

	public enum POV {
		North(0),
		East(90),
		South(180),
		West(270),
		NorthEast(45),
		SouthEast(135),
		SouthWest(225),
		NorthWest(315);

		int value;

		POV(int value) {
			this.value = value;
		}
	}
}