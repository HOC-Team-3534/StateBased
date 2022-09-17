package frc.robot.extras;

import edu.wpi.first.math.geometry.Rotation2d;

public class LimelightShootProjection {

    double distance;
    Rotation2d offset;

    public LimelightShootProjection(double distance, Rotation2d offset) {
        this.distance = distance;
        this.offset = offset;
    }

    public double getDistance() {
        return distance;
    }

    public Rotation2d getOffset() {
        return offset;
    }
}
