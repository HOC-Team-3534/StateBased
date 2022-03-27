package frc.robot.subsystems;

import frc.robot.Robot;
import frc.robot.subsystems.SwerveDrive;
import frc.robot.subsystems.parent.SubsystemRequirement;

public class SwerveDriveReq extends SubsystemRequirement<SwerveDrive, SwerveDriveState> {
    public SwerveDriveReq(SwerveDriveState subsystemState) {
        super(Robot.swerveDrive, subsystemState);
    }
}
