package frc.robot.subsystems;

import frc.robot.Robot;
import frc.robot.subsystems.parent.SubsystemRequirement;

public class ShooterReq extends SubsystemRequirement<Shooter, ShooterState> {
    public ShooterReq(ShooterState subsystemState) {
        super(Robot.shooter, subsystemState);
    }
}
