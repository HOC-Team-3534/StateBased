package frc.robot.extras;

import edu.wpi.first.math.geometry.Translation2d;

public abstract class Utils {
	public static Translation2d
					getPositionAfterMotion(	Translation2d translation,
											Translation2d vector, double time) {
		return new Translation2d(	translation.getX()	+ vector.getX() * time,
									translation.getY() + vector.getY() * time);
	}

	public static Translation2d getCombinedMotion(	Translation2d vector1,
													Translation2d vector2) {
		return vector1.plus(vector2);
	}
}
