package frc.robot.extras;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.wpiClasses.Vector2d;

public abstract class Utils {
	public static Translation2d getPositionAfterMotion(Translation2d translation, Vector2d vector,
	                                                   double time) {
		return new Translation2d(translation.getX() + vector.x * time,
		                         translation.getY() + vector.y * time);
	}

	public static Vector2d getCombinedMotion(Vector2d vector1, Vector2d vector2) {
		return new Vector2d(vector1.x + vector2.x, vector1.y + vector2.y);
	}

	public static Vector2d createVector2d(double magnitude, Rotation2d direction) {
		return new Vector2d(magnitude * direction.getCos(), magnitude * direction.getSin());
	}
}
