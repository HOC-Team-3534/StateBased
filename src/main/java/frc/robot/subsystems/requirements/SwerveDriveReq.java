package frc.robot.subsystems.requirements;

import frc.SubsystemRequirement;
import frc.robot.Robot;
import frc.robot.subsystems.SwerveDrive;
import frc.robot.subsystems.SwerveDriveState;

public class SwerveDriveReq extends SubsystemRequirement<SwerveDrive, SwerveDriveState> {
    public SwerveDriveReq(SwerveDriveState subsystemState) {
        super(Robot.swerveDrive, subsystemState);
    }
}
