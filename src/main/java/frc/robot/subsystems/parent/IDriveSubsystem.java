package frc.robot.subsystems.parent;

import edu.wpi.first.math.geometry.Rotation2d;

public interface IDriveSubsystem {

    Rotation2d getGyroHeading();

    void resetGyro();

}
